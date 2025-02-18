document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    const phoneNumber = localStorage.getItem("phoneNumber");
    const accessLevel = localStorage.getItem("accessLevel");

    if (!token || !phoneNumber || !accessLevel) {
        alert("Erro: Usuário não autenticado.");
        window.location.href = "../../index.html"; 
        return;
    }

    let selectedUser = localStorage.getItem("selectedUser");
    selectedUser = selectedUser ? JSON.parse(selectedUser) : null;

    if (selectedUser && selectedUser.phoneNumber !== phoneNumber) { 
        preencherCampos(selectedUser);
        return;
    }    


    let apiUrl;
    if (accessLevel === "ROLE_SUPER_ADMIN") {
        apiUrl = `http://localhost:8080/api/super-admin/user?phoneNumber=${phoneNumber}`;
    } else if (accessLevel === "ROLE_ADMIN") {
        apiUrl = `http://localhost:8080/api/admin/user?phoneNumber=${phoneNumber}`;
    } else if (accessLevel === "ROLE_CLIENT") {
        apiUrl = `http://localhost:8080/api/client/user?phoneNumber=${phoneNumber}`;
    } else {
        alert("Erro: Nível de acesso desconhecido.");
        return;
    }

    try {
        const response = await fetch(apiUrl, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        const result = await response.json();
        if (response.ok) {
            preencherCampos(result.data);
        } else {
            alert("Erro ao carregar perfil: " + (result.message || "Erro desconhecido."));
        }
    } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        alert("Não foi possível carregar os dados do perfil.");
    }

   
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


function preencherCampos(user) {
    if (!user) return; // 🔹 Se `user` for `null`, não faz nada

    document.querySelectorAll(".list-item")[1].textContent = user.name || "Sem nome"; 
    document.querySelectorAll(".list-item")[3].textContent = user.phoneNumber || "Sem número"; 
    document.querySelectorAll(".list-item")[5].textContent = user.email || "Sem email"; 
    document.querySelectorAll(".list-item")[7].textContent = formatarNivelAcesso(user.accessLevel); 
    document.querySelectorAll(".list-item")[9].textContent = formatarData(user.lastLogin); 

    const imagemPerfil = document.querySelector(".img-wrapper.utilizador-1 img");

    if (imagemPerfil && user.imageUrl) {
        imagemPerfil.src = user.imageUrl;
    }
}


function formatarData(dataISO) {
    if (!dataISO || dataISO === 'null') return "Sem registro";

    const data = new Date(dataISO);
    const horas = data.getHours().toString().padStart(2, '0');
    const minutos = data.getMinutes().toString().padStart(2, '0');
    const dia = data.getDate().toString().padStart(2, '0');
    const mes = (data.getMonth() + 1).toString().padStart(2, '0');
    const ano = data.getFullYear();

    return `${horas}:${minutos} | ${dia}/${mes}/${ano}`;
}


function formatarNivelAcesso(role) {
    const rolesMap = {
        "ROLE_SUPER_ADMIN": "Super Admin",
        "ROLE_ADMIN": "Admin",
        "ROLE_CLIENT": "Cliente"
    };
    return rolesMap[role] || "Desconhecido";
}
