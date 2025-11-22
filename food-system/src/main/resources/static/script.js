// ---------------------------
//  BASE BACKEND URL
// ---------------------------
const BASE_URL = "http://localhost:8080/api/auth";


// ---------------------------
//  REGISTER USER
// ---------------------------
document.addEventListener("DOMContentLoaded", () => {

  const registerForm = document.getElementById("registerForm");
  if (registerForm) {
    registerForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const username = document.getElementById("regUsername").value;
      const email = document.getElementById("regEmail").value;
      const password = document.getElementById("regPassword").value;
      const phoneNumber = document.getElementById("regPhone").value;
      const address = document.getElementById("regAddress").value;

      try {
        const response = await fetch(`${BASE_URL}/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username,
            email,
            password,
            phoneNumber,
            address
          })
        });

        const data = await response.json();

        if (response.status === 201) {
          alert("Registration successful!");
          window.location.href = "login.html";
        } else {
          alert(data.message || "Registration failed");
        }

      } catch (error) {
        alert("Error connecting to server");
      }
    });
  }


  // ---------------------------
  //  LOGIN USER
  // ---------------------------
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    loginForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const email = document.getElementById("loginEmail").value;
      const password = document.getElementById("loginPassword").value;

      try {
        const response = await fetch(`${BASE_URL}/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.status === 200) {
          alert("Login successful!");

          // Store session token
          localStorage.setItem("sessionToken", data.sessionToken);

          window.location.href = "profile.html"; // redirect to profile/dashboard
        } else {
          alert(data.message || "Invalid credentials");
        }

      } catch (error) {
        alert("Error connecting to server");
      }
    });
  }

});
