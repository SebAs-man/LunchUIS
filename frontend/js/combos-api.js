// combos-api.js - APIs para gestión de combos
console.log('✅ combos-api.js cargado');

// Configuración de APIs
const API_CONFIG = {
    COMBO_SERVICE: 'http://localhost:8082/api/v1',
    ORDER_SERVICE: 'http://localhost:8083/api/v1'
};

// Función para hacer requests a la API
async function apiRequest(url, options = {}) {
    try {
        console.log('🌐 API Request:', url, options);
        
        const defaultHeaders = {
            'Content-Type': 'application/json'
        };
        
        // Agregar token JWT si existe
        const token = localStorage.getItem('jwtToken');
        if (token) {
            defaultHeaders['Authorization'] = `Bearer ${token}`;
        }
        
        const response = await fetch(url, {
            ...options,
            headers: { ...defaultHeaders, ...options.headers }
        });
        
        console.log('📡 API Response:', response.status, response.statusText);
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('❌ API Error:', error);
        throw error;
    }
}

// ========== FUNCIONES DE COMBOS ==========

// Obtener todos los combos
async function getCombos() {
    try {
        const response = await apiRequest(`${API_CONFIG.COMBO_SERVICE}/combos`);
        return response;
    } catch (error) {
        console.error('❌ Error obteniendo combos:', error);
        // Fallback a datos locales
        return getCombosLocal();
    }
}

// Obtener combo por ID
async function getComboById(id) {
    try {
        const response = await apiRequest(`${API_CONFIG.COMBO_SERVICE}/combos/${id}`);
        return response;
    } catch (error) {
        console.error('❌ Error obteniendo combo:', error);
        throw error;
    }
}

// Crear nuevo combo
async function createCombo(comboData) {
    try {
        const response = await apiRequest(`${API_CONFIG.COMBO_SERVICE}/combos`, {
            method: 'POST',
            body: JSON.stringify(comboData)
        });
        return response;
    } catch (error) {
        console.error('❌ Error creando combo:', error);
        throw error;
    }
}

// Actualizar combo
async function updateCombo(id, comboData) {
    try {
        const response = await apiRequest(`${API_CONFIG.COMBO_SERVICE}/combos/${id}`, {
            method: 'PUT',
            body: JSON.stringify(comboData)
        });
        return response;
    } catch (error) {
        console.error('❌ Error actualizando combo:', error);
        throw error;
    }
}

// Eliminar combo
async function deleteCombo(id) {
    try {
        const response = await apiRequest(`${API_CONFIG.COMBO_SERVICE}/combos/${id}`, {
            method: 'DELETE'
        });
        return response;
    } catch (error) {
        console.error('❌ Error eliminando combo:', error);
        throw error;
    }
}

// ========== FUNCIONES DE PEDIDOS ==========

// Obtener pedidos del usuario
async function getUserOrders() {
    try {
        const response = await apiRequest(`${API_CONFIG.ORDER_SERVICE}/orders`);
        return response;
    } catch (error) {
        console.error('❌ Error obteniendo pedidos:', error);
        // Fallback a datos locales
        return getOrdersLocal();
    }
}

// Crear nuevo pedido
async function createOrder(orderData) {
    try {
        const response = await apiRequest(`${API_CONFIG.ORDER_SERVICE}/orders`, {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
        return response;
    } catch (error) {
        console.error('❌ Error creando pedido:', error);
        throw error;
    }
}

// ========== FUNCIONES FALLBACK (LOCALES) ==========

// Obtener combos locales (fallback)
function getCombosLocal() {
    const combos = JSON.parse(localStorage.getItem('elementioCombos') || '[]');
    console.log('📦 Usando combos locales:', combos);
    return combos;
}

// Obtener pedidos locales (fallback)
function getOrdersLocal() {
    const orders = JSON.parse(localStorage.getItem('elementioPedidos') || '[]');
    console.log('📋 Usando pedidos locales:', orders);
    return orders;
}

// Guardar combo localmente (fallback)
function saveComboLocal(comboData) {
    const combos = getCombosLocal();
    const newCombo = {
        id: Date.now(),
        ...comboData,
        fechaCreacion: new Date().toISOString()
    };
    combos.push(newCombo);
    localStorage.setItem('elementioCombos', JSON.stringify(combos));
    return newCombo;
}

// Actualizar combo localmente (fallback)
function updateComboLocal(id, comboData) {
    const combos = getCombosLocal();
    const index = combos.findIndex(c => c.id === id);
    if (index !== -1) {
        combos[index] = { ...combos[index], ...comboData };
        localStorage.setItem('elementioCombos', JSON.stringify(combos));
        return combos[index];
    }
    throw new Error('Combo no encontrado');
}

// Eliminar combo localmente (fallback)
function deleteComboLocal(id) {
    const combos = getCombosLocal();
    const filteredCombos = combos.filter(c => c.id !== id);
    localStorage.setItem('elementioCombos', JSON.stringify(filteredCombos));
    return true;
}

// ========== FUNCIONES DE UTILIDAD ==========

// Verificar si el backend está disponible
async function checkBackendConnection() {
    try {
        await apiRequest(`${API_CONFIG.COMBO_SERVICE}/actuator/health`);
        return true;
    } catch (error) {
        console.log('⚠️ Backend no disponible, usando modo local');
        return false;
    }
}

// Función híbrida para obtener combos (backend + fallback)
async function getCombosHybrid() {
    const isBackendAvailable = await checkBackendConnection();
    
    if (isBackendAvailable) {
        try {
            return await getCombos();
        } catch (error) {
            console.log('⚠️ Error con backend, usando datos locales');
            return getCombosLocal();
        }
    } else {
        return getCombosLocal();
    }
}

console.log('✅ combos-api.js completamente cargado');
