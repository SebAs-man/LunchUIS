// ===========================
// AUTH.JS - Sistema LunchUIS
// ===========================

// Configuración general del sistema
const API_CONFIG = {
    IDENTITY_SERVICE: "http://localhost:8081/api/v1",
    BUY_SERVICE: "http://localhost:8082/api/v1",
    NOTIFICATION_SERVICE: "http://localhost:8083/api/v1",
    REPORT_SERVICE: "http://localhost:8084/api/v1",
    QRCODE_SERVICE: "http://localhost:8085/api/v1",
  };
  
  // Función para construir URLs dinámicas
  function getServiceUrl(service, endpoint) {
    return `${API_CONFIG[service]}${endpoint}`;
  }
  
  // ===========================
  // Manejo de Token (JWT)
  // ===========================
  function saveToken(token) {
    localStorage.setItem("jwtToken", token);
  }
  
  function getToken() {
    return localStorage.getItem("jwtToken");
  }
  
  function clearToken() {
    localStorage.removeItem("jwtToken");
  }
  
  // ===========================
  // Peticiones a la API
  // ===========================
  async function apiRequest(url, method = "GET", data = null) {
    const token = getToken();
  
    const options = {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
    };
  
    if (token) {
      options.headers["Authorization"] = `Bearer ${token}`;
    }
  
    if (data) {
      options.body = JSON.stringify(data);
    }
  
    const response = await fetch(url, options);
  
    // Manejo de errores
    if (!response.ok) {
      const errorText = await response.text();
      console.error("❌ Error en la API:", errorText);
  
      // Si no hay sesión, no mostrar mensaje emergente en login
      if (!window.location.pathname.includes("login.html")) {
        alert("⚠️ No tienes permisos de administrador o tu sesión expiró.");
      }
  
      throw new Error(errorText);
    }
  
    return response.json();
  }
  
  // ===========================
  // Funciones de Autenticación
  // ===========================
  async function login(email, password) {
    try {
      const response = await apiRequest(
        getServiceUrl("IDENTITY_SERVICE", "/user/login"),
        "POST",
        { email, password }
      );
  
      if (response && response.token) {
        saveToken(response.token);
        alert("✅ Inicio de sesión exitoso");
        window.location.href = "dashboard.html";
      } else {
        alert("⚠️ Error al iniciar sesión: token no recibido");
      }
    } catch (error) {
      console.error("Error al iniciar sesión:", error);
      alert("❌ Credenciales incorrectas o servidor no disponible");
    }
  }
  
  function logout() {
    clearToken();
    alert("👋 Sesión cerrada correctamente");
    window.location.href = "login.html";
  }
  
  // ===========================
  // Inicialización Condicional
  // ===========================
  
  // Detectar si estás en login.html
  const currentPath = window.location.pathname;
  const isLoginPage = currentPath.includes("login.html");
  
  // Solo ejecutar lógica de backend si NO estás en login.html
  if (!isLoginPage) {
    console.log("🚀 auth.js cargado fuera de login, inicializando datos...");
  
    // Funciones que antes causaban errores por ejecutarse sin token
    initializeDefaultData();
    debugAuth();
  
    console.log("✅ auth.js completamente cargado fuera de login");
  } else {
    console.log("🟡 auth.js cargado en login.html — sin llamadas al backend");
  }
  
  // ===========================
  // Funciones de Depuración y Configuración
  // ===========================
  function initializeDefaultData() {
    console.log("🧩 Datos por defecto inicializados (solo si no estás en login).");
  }
  
  function debugAuth() {
    const token = getToken();
    if (token) {
      console.log("🔐 Token JWT encontrado:", token);
    } else {
      console.log("🚫 No hay token guardado, usuario sin sesión.");
    }
  }
  