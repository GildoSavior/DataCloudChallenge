document.addEventListener("DOMContentLoaded", function () {
    const signupForm = document.getElementById("signup-form");

    signupForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const name = document.getElementById("nome").value.trim();
        const phoneNumber = document.getElementById("telemovel").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("senha").value.trim();

        if (!nome || !telemovel || !email || !senha) {
            alert("Por favor, preencha todos os campos.");
            return;
        }

        const payload = {
            name: name,
            phoneNumber: phoneNumber,
            email: email,
            password: password
        };

        try {
            const response = await fetch("http://localhost:8080/api/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (response.ok) {
                alert(result.message);
                localStorage.setItem("token", result.data.jwtToken);
                localStorage.setItem("phoneNumber", result.data.phoneNumber);
                localStorage.setItem("accessLevel", result.data.accessLevel);


                window.location.href = "cazio/utilizadores/perfil-do-utilizador.html";

            } else {
                alert("Erro: " + result.message);
            }
           
            
        } catch (error) {
            console.error("Erro:", error);
            alert(error.message || "Ocorreu um erro. Tente novamente mais tarde.");
        }
    });
});
