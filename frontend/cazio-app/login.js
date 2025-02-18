document.addEventListener("DOMContentLoaded", function () {
    console.log("Script carregado corretamente!");

    const loginForm = document.getElementById("wf-form-Login");
    
    if (!loginForm) {
        console.error("Elemento do formulário não encontrado!");
        return;
    }

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        console.log("Formulário submetido!");

        const phoneNumber = document.getElementById("Telemovel").value;
        const password = document.getElementById("Senha").value;

        if (!phoneNumber || !password) {
            alert("Preencha todos os campos!");
            return;
        }

        const loginData = { phoneNumber, password };

        try {
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginData)
            });

            const result = await response.json();

            if (response.ok) {
                alert(result.message);
                localStorage.setItem("token", result.data.jwtToken);
                localStorage.setItem("phoneNumber", result.data.phoneNumber);
                localStorage.setItem("accessLevel", result.data.accessLevel);

                window.location.href = result.data.accessLevel.includes("ROLE_CLIENT") 
                    ? "cazio/utilizadores/perfil-do-utilizador.html" :
                     "cazio/utilizadores/todos-utilizadores.html" ;
                
            } else {
                alert("Erro: " + result.message);
            }
        } catch (error) {
            console.error("Erro ao tentar fazer login:", error);
        }
    });
});
