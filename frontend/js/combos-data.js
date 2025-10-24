// combos-data.js - SOLO localStorage
console.log('✅ combos-data.js cargado');

// Obtener combos
function getCombos() {
    const combos = JSON.parse(localStorage.getItem('elementioCombos') || '[]');
    console.log('📦 Combos encontrados:', combos.length);
    return combos;
}

// Guardar combos
function saveCombos(combos) {
    localStorage.setItem('elementioCombos', JSON.stringify(combos));
    console.log('💾 Combos guardados:', combos.length);
}

// Obtener pedidos
function getPedidos() {
    return JSON.parse(localStorage.getItem('elementioPedidos') || '[]');
}

// Guardar pedidos
function savePedidos(pedidos) {
    localStorage.setItem('elementioPedidos', JSON.stringify(pedidos));
}

// Crear nuevo combo
function crearCombo(comboData) {
    console.log('🆕 Creando combo:', comboData.nombre);
    
    try {
        const combos = getCombos();
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        
        const nuevoCombo = {
            id: Date.now(),
            ...comboData,
            activo: true,
            creadoPor: currentUser?.username || 'admin',
            fechaCreacion: new Date().toISOString()
        };

        combos.push(nuevoCombo);
        saveCombos(combos);
        
        console.log('✅ Combo creado:', nuevoCombo.nombre);
        return { 
            success: true, 
            message: 'Combo creado exitosamente', 
            combo: nuevoCombo 
        };
    } catch (error) {
        console.error('❌ Error creando combo:', error);
        return { 
            success: false, 
            message: 'Error al crear combo' 
        };
    }
}

// Editar combo
function editarCombo(id, comboData) {
    console.log('✏️ Editando combo ID:', id);
    
    try {
        const combos = getCombos();
        const index = combos.findIndex(c => c.id === id);
        
        if (index === -1) {
            return { success: false, message: 'Combo no encontrado' };
        }
        
        combos[index] = { 
            ...combos[index], 
            ...comboData,
            fechaActualizacion: new Date().toISOString()
        };
        
        saveCombos(combos);
        
        console.log('✅ Combo editado:', combos[index].nombre);
        return { success: true, message: 'Combo actualizado exitosamente' };
    } catch (error) {
        console.error('❌ Error editando combo:', error);
        return { success: false, message: 'Error al actualizar combo' };
    }
}

// Eliminar combo
function eliminarCombo(id) {
    console.log('🗑️ Eliminando combo ID:', id);
    
    try {
        const combos = getCombos();
        const updatedCombos = combos.filter(c => c.id !== id);
        saveCombos(updatedCombos);
        
        console.log('✅ Combo eliminado');
        return { success: true, message: 'Combo eliminado exitosamente' };
    } catch (error) {
        console.error('❌ Error eliminando combo:', error);
        return { success: false, message: 'Error al eliminar combo' };
    }
}

// Obtener combo por ID
function getComboById(id) {
    const combos = getCombos();
    return combos.find(c => c.id === id);
}

// Obtener combos disponibles
function getCombosDisponibles() {
    const combos = getCombos();
    return combos.filter(combo => combo.activo && combo.disponibles > 0);
}

// Obtener pedidos del usuario actual
function getPedidosUsuario() {
    const pedidos = getPedidos();
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    
    if (!currentUser) return [];
    
    return pedidos.filter(pedido => pedido.usuario === currentUser.username);
}

// Obtener todos los pedidos (Admin)
function getAllPedidos() {
    return getPedidos();
}