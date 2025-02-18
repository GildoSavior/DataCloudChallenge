document.addEventListener("DOMContentLoaded", async function () {
    const tableBody = document.querySelector(".table");
    const token = localStorage.getItem("token");
    const accessLevel = localStorage.getItem("accessLevel");

    if (!token) {
        alert("Token não encontrado! Faça login novamente.");
        window.location.href = "../../index.html";
        return;
    }

    let apiUrl = "";
    if (accessLevel === "ROLE_SUPER_ADMIN") {
        apiUrl = "http://localhost:8080/api/super-admin/users";
    } else if (accessLevel === "ROLE_ADMIN") {
        apiUrl = "http://localhost:8080/api/admin/users";
    } else {
        alert("Acesso negado!");
        return;
    }

    try {
        const response = await fetchWithTimeout(apiUrl, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        }, 10000); // Timeout de 10 segundos

        if (!response.ok) throw new Error(`Erro ao buscar utilizadores: ${response.status}`);

        const result = await response.json();
        const users = result.data;

        // 🔹 Limpa a tabela sem remover os cabeçalhos
        document.querySelectorAll(".table-row:not(.cell-label-wrapper)").forEach(row => row.remove());

        // 🔹 Insere os usuários na tabela
        users.forEach(user => {
            const row = document.createElement("div");
            row.classList.add("table-row");

            row.innerHTML = `
                <label class="w-checkbox table-checkbox-wrapper">
                    <input type="checkbox" class="user-checkbox">
                </label>
                <a href="../../cazio/utilizadores/perfil-do-utilizador.html?phoneNumber=${user.phoneNumber}" class="table-row-content item-link w-inline-block">
                    <div class="table-cell _10">
                        <div class="table-image-wrapper">
                            <img src="${user.imageUrl || '../../images/default-avatar.png'}" alt="Imagem do usuário" class="img is--contain utilizador">
                        </div>
                    </div>
                    <div class="table-cell _20">
                        <div class="table-cell-text">${user.name}</div>
                    </div>
                    <div class="table-cell _15" data-raw-date="${user.lastLogin}">
                        <div class="table-cell-text">${formatDate(user.lastLogin)}</div>
                    </div>
                    <div class="table-cell _15">
                        <div class="table-cell-text">${formatPhoneNumber(user.phoneNumber)}</div>
                    </div>
                    <div class="table-cell _20">
                        <div class="table-cell-text">${user.email}</div>
                    </div>
                    <div class="table-cell _20" data-raw-accessLevel="${user.accessLevel}">
                        <div class="table-cell-text">${formatAccessLevel(user.accessLevel)}</div>
                    </div>
                </a>
            `;


            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Erro ao carregar utilizadores:", error);
        alert("Erro ao carregar os utilizadores.");
    }
   

    document.querySelectorAll(".table-row-content").forEach(row => {
        row.addEventListener("click", function (event) {
            event.preventDefault(); 

            const cells = this.querySelectorAll(".table-cell-text");

            const lastLogin = this.querySelector(".table-cell[data-raw-date]")?.getAttribute("data-raw-date") || "";
            const accessLevel = this.querySelector(".table-cell[data-raw-accessLevel]")?.getAttribute("data-raw-accessLevel") || "";

            const userData = {
                name: cells[0]?.textContent.trim() || "",
                lastLogin: lastLogin, 
                phoneNumber: cells[2]?.textContent.replace(/\s+/g, "").trim() || "",
                email: cells[3]?.textContent.trim() || "",
                accessLevel: accessLevel,
                imageUrl: this.querySelector(".table-image-wrapper img")?.src || ""
            };

            localStorage.setItem("selectedUser", JSON.stringify(userData));

            window.location.href = this.href;
        });
    });

    const filterSelect = document.getElementById("field-2");

    filterSelect.addEventListener("change", function () {
        const selectedValue = filterSelect.value;
        const rows = document.querySelectorAll(".table-row");

        rows.forEach(row => {
            const accessLevelCell = row.querySelector(".table-cell[data-raw-accessLevel]");

            if (!accessLevelCell) return; // Se não encontrar a célula, ignora

            const accessLevel = accessLevelCell.getAttribute("data-raw-accessLevel");

            if (selectedValue === "" || accessLevel === selectedValue) {
                row.style.display = ""; // Mostra a linha
            } else {
                row.style.display = "none"; // Esconde a linha
            }
        });
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

// 🔹 Adiciona Timeout para `fetch()`
async function fetchWithTimeout(url, options, timeout = 5000) {
    const controller = new AbortController();
    const signal = controller.signal;
    const timeoutId = setTimeout(() => controller.abort(), timeout);

    try {
        const response = await fetch(url, { ...options, signal });
        return response;
    } catch (error) {
        if (error.name === "AbortError") {
            throw new Error("A requisição demorou muito e foi cancelada.");
        }
        throw error;
    } finally {
        clearTimeout(timeoutId);
    }
}

// 🔹 Formata datas no padrão "12/03/2022"
function formatDate(dateString) {
    if (!dateString) return "Sem registro";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-PT", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
    });
}

// 🔹 Formata números de telefone no padrão "900 900 900"
function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return "Sem número";
    
    let cleaned = phoneNumber.replace(/\D/g, "");
    if (cleaned.length !== 9) return phoneNumber;

    return cleaned.replace(/(\d{3})(\d{3})(\d{3})/, "$1 $2 $3");
}

// 🔹 Formata nível de acesso
function formatAccessLevel(accessLevel) {
    const levels = {
        "ROLE_SUPER_ADMIN": "Super Admin",
        "ROLE_ADMIN": "Admin",
        "ROLE_CLIENT": "Cliente"
    };
    return levels[accessLevel] || "Desconhecido";
}
