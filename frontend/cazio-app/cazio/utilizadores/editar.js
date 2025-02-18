document.addEventListener("DOMContentLoaded", async function () {
    const nomeInput = document.getElementById("field");
    const telefoneInput = document.getElementById("field-2");
    const emailInput = document.getElementById("field-4");
    const tipoUtilizadorSelect = document.getElementById("field-3");

    const token = localStorage.getItem("token");
    const phoneNumber = localStorage.getItem("phoneNumber");
    const accessLevel = localStorage.getItem("accessLevel");


    // 🔹 Obtém `selectedUser` e verifica se ele existe
    let selectedUser = localStorage.getItem("selectedUser");
    selectedUser = selectedUser ? JSON.parse(selectedUser) : null;

    if (!token || !phoneNumber) {
        alert("Você precisa estar logado para acessar esta página.");
        window.location.href = "../../index.html";
        return;
    }

    telefoneInput.disabled = true;
    emailInput.disabled = true;

    const isSuperAdmin = accessLevel === "ROLE_SUPER_ADMIN";
    
    let isCurrentUser = null;

    if(!selectedUser) isCurrentUser = true;
    else if(selectedUser && selectedUser.phoneNumber === phoneNumber) isCurrentUser = true;
    else isCurrentUser = false;

    // 🔹 Define as opções do select de Tipo de Utilizador
    const allowedOptions = isSuperAdmin && selectedUser
        ? { "ROLE_ADMIN": "Admin", "ROLE_CLIENT": "Cliente" }
        : accessLevels;

    function populateUserFields(user) {
        if (!user) return; // 🔹 Se `user` for `null`, não executa nada

        nomeInput.value = user.name || "";
        telefoneInput.value = user.phoneNumber || "";
        emailInput.value = user.email || "";

        tipoUtilizadorSelect.innerHTML = "";
        Object.entries(allowedOptions).forEach(([key, value]) => {
            const option = new Option(value, key);
            if (user.accessLevel === key) option.selected = true;
            tipoUtilizadorSelect.appendChild(option);
        });
    }

    // 🔹 Função para obter a URL correta da API
    function getApiUrl(basePath, userPhoneNumber) {
        if (isSuperAdmin) return `http://localhost:8080/api/super-admin/${basePath}?phoneNumber=${userPhoneNumber}`;
        if (accessLevel === "ROLE_ADMIN") return `http://localhost:8080/api/admin/${basePath}?phoneNumber=${userPhoneNumber}`;
        return `http://localhost:8080/api/client/${basePath}?phoneNumber=${userPhoneNumber}`;
    }

    // 🔹 Se `selectedUser` não existir, busca os dados da API
    async function fetchUserData(userPhoneNumber) {
        try {
            const apiUrl = getApiUrl("user", userPhoneNumber);
            if (!apiUrl) return;

            const response = await fetch(apiUrl, {
                headers: { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" }
            });

            const result = await response.json();
            if (response.ok) {
                populateUserFields(result.data);
            } else {
                console.error("Erro ao buscar dados do usuário:", result.message);
            }
        } catch (error) {
            console.error("Erro ao comunicar com o servidor:", error);
        }
    }

    if (selectedUser) {
        populateUserFields(selectedUser);
    } else {
        await fetchUserData(phoneNumber); // 🔹 Busca os dados da API
    }

    if (!isSuperAdmin) tipoUtilizadorSelect.disabled = true;

    // 🔹 Upload de imagem
    let file = null;
    const uploadField = document.querySelector(".photo-upload-input-field");
    const fileInput = Object.assign(document.createElement("input"), {
        type: "file",
        accept: "image/*",
        style: "display: none"
    });

    document.body.appendChild(fileInput);

    uploadField.addEventListener("click", () => {

        fileInput.click();
    });

    fileInput.addEventListener("change", function () {
        if (fileInput.files.length > 0) {
            file = fileInput.files[0];
            alert("Imagem carregada com sucesso!");
        }
    });

    // 🔹 Salvar alterações
    document.querySelector(".button.main.w-button").addEventListener("click", async function (event) {
        event.preventDefault();
        tipoUtilizadorSelect.disabled = false;

        const userData = {
            name: nomeInput.value.trim(),
            accessLevel: tipoUtilizadorSelect.value
        };

        try {
            const apiUrl = getApiUrl("user", selectedUser ? selectedUser.phoneNumber : phoneNumber);
            if (!apiUrl) return;

            const response = await fetch(apiUrl, {
                method: "PUT",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
                body: JSON.stringify(userData)
            });

            const result = await response.json();
            if (response.ok) {
                alert("Perfil atualizado com sucesso!");
                if(!file){
                    localStorage.setItem("selectedUser", null); 
                    window.location.href =  accessLevel == "ROLE_CLIENT" ? "perfil-do-utilizador.html" : "todos-utilizadores.html" ;
                }
            } else {
                alert("Erro ao atualizar perfil: " + (result.message || "Erro desconhecido."));
                return;
            }
        } catch (error) {
            console.error("Erro ao enviar requisição:", error);
            alert("Erro ao comunicar com o servidor.");
            return
        }

        if(file) {
            const formData = new FormData();
            formData.append("file", file);

            try {
                const response = await fetch(`http://localhost:8080/api/upload/${selectedUser ? selectedUser.phoneNumber : phoneNumber}`, {
                    method: "POST",
                    headers: { "Authorization": `Bearer ${token}` },
                    body: formData
                });

                if (response.ok) {
                    console.log("Imagem enviada com sucesso!");
                    localStorage.setItem("selectedUser", null); 
                    window.location.href = accessLevel == "ROLE_CLIENT" ? "perfil-do-utilizador.html" : "todos-utilizadores.html" ;
                
                } else {
                    const result = await response.json();
                    alert("Erro ao enviar imagem: " + (result.message || "Erro desconhecido."));
                }
            } catch (error) {
                console.error("Erro ao enviar imagem:", error);
                alert("Erro ao comunicar com o servidor.");
            }
        }

    });

    // 🔹 Deletar usuário
    const deleteButton = document.querySelector(".action-button.cncel.w-inline-block");

    if (isSuperAdmin && isCurrentUser) {
        tipoUtilizadorSelect.disabled = true;
        deleteButton.style.display = "none";
    }

    deleteButton.addEventListener("click", async function (event) {
        event.preventDefault();

        if (!confirm("Tem certeza que deseja apagar este utilizador?")) return;

        try {
            const apiUrl = getApiUrl("user", selectedUser ? selectedUser.phoneNumber : phoneNumber);
            if (!apiUrl) return;

            const response = await fetch(apiUrl, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });

            if (response.ok) {
                alert("Usuário apagado com sucesso!");
                localStorage.setItem("selectedUser", null); // 🔹 Reseta `selectedUser`
                if (!isCurrentUser) {
                    window.location.href = "todos-utilizadores.html";
                } else {
                    localStorage.clear();
                    window.location.href = "../../index.html";
                }
            } else {
                const result = await response.json();
                alert("Erro ao apagar utilizador: " + (result.message || "Erro desconhecido."));
            }
        } catch (error) {
            console.error("Erro ao enviar requisição:", error);
            alert("Erro ao comunicar com o servidor.");
        }
    });

    const logoutLink = document.getElementById("logout");

    if (logoutLink) {
        logoutLink.addEventListener("click", function (event) {
            event.preventDefault(); 
            localStorage.clear(); 
            alert("Sessão encerrada com sucesso.");
            window.location.href = "../../index.html"; 
        });
    }
});

// 🔹 Definição dos níveis de acesso
const accessLevels = {
    "ROLE_SUPER_ADMIN": "Super Admin",
    "ROLE_ADMIN": "Admin",
    "ROLE_CLIENT": "Cliente"
};

// 🔹 Atualiza `selectedUser` para `null` ao sair da página
window.addEventListener("beforeunload", function () {
    localStorage.setItem("selectedUser", null);
});
