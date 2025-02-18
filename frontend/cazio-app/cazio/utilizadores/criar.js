document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");
    const accessLevel = localStorage.getItem("accessLevel");

    // Elementos do formulário
    const nomeInput = document.getElementById("field");
    const telefoneInput = document.getElementById("field-2");
    const emailInput = document.getElementById("field-4");
    const tipoSelect = document.getElementById("field-3");
    const criarBtn = document.querySelector(".button.main");

    // 🔹 Popula as opções do select de Tipo de Usuário
    const accessLevels = {
        "ROLE_ADMIN": "Admin",
        "ROLE_CLIENT": "Cliente"
    };

    const isSuperAdmin = accessLevel === "ROLE_SUPER_ADMIN";
    const allowedOptions = !isSuperAdmin 
        ? { "ROLE_CLIENT": "Cliente" }
        : accessLevels;

    tipoSelect.innerHTML = `<option value="">Selecione...</option>`; 
    for (const key in allowedOptions) {
        const option = document.createElement("option");
        option.value = key;
        option.textContent = allowedOptions[key];
        tipoSelect.appendChild(option);
    }

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

    // 🔹 Evento de criação do usuário
    criarBtn.addEventListener("click", async function (event) {
        event.preventDefault();

        const userData = {
            name: nomeInput.value.trim(),
            phoneNumber: telefoneInput.value.replace(/\s+/g, ""),
            email: emailInput.value.trim(),
            accessLevel: tipoSelect.value
        };

        // 🔹 Validação simples
        if (!userData.name || !userData.phoneNumber || !userData.email || !userData.accessLevel) {
            alert("Preencha todos os campos!");
            return;
        }

        try {
            const apiUrl = isSuperAdmin
                ? "http://localhost:8080/api/super-admin/user"
                : "http://localhost:8080/api/admin/user";

            const response = await fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userData)
            });

            if (!response.ok) throw new Error("Erro ao criar utilizador.");

            alert("Usuário criado com sucesso!");
            if(!file){
                window.location.href = "../../cazio/utilizadores/todos-utilizadores.html";
            }

        } catch (error) {
            console.error("Erro ao criar utilizador:", error);
            alert("Erro ao criar utilizador.");
            return
        }

        if(file) {
            const formData = new FormData();
            formData.append("file", file);

            try {
                const response = await fetch(`http://localhost:8080/api/upload/${userData.phoneNumber}`, {
                    method: "POST",
                    headers: { "Authorization": `Bearer ${token}` },
                    body: formData
                });

                if (response.ok) {
                    console.log("Imagem enviada com sucesso!");
                    localStorage.setItem("selectedUser", null); 
                    window.location.href = isSuperAdmin || accessLevel === "ROLE_ADMIN" ? "todos-utilizadores.html" : "../../index.html";
                    
                } else {
                    const result = await response.json();
                    console("Erro ao fazer upload da imagem: " + (result.message || "Erro desconhecido."));
                }
            } catch (error) {
                console.error("Erro ao fazer upload da  imagem:", error);
                alert("Erro ao comunicar com o servidor.");
            }
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
