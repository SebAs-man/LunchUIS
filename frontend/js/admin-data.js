// En admin-data.js - Asegúrate de tener estas funciones:

// Obtener todos los combos (para admin)
function getCombos() {
    const combos = JSON.parse(localStorage.getItem('elementioCombos') || '[]');
    console.log('📦 Todos los combos:', combos.length);
    return combos;
}

// Obtener combos disponibles (para usuario)
function getCombosDisponibles() {
    const combos = getCombos();
    const disponibles = combos.filter(combo => combo.activo && combo.disponibles > 0);
    console.log('🛒 Combos disponibles para usuario:', disponibles.length);
    return disponibles;
}

// Obtener combo por ID
function getComboById(id) {
    const combos = getCombos();
    const combo = combos.find(c => c.id === id);
    console.log('🔍 Combo encontrado por ID:', id, combo);
    return combo;
}

// Realizar pedido
function realizarPedido(pedidoData) {
    console.log('🛒 Realizando pedido:', pedidoData);
    
    try {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        
        if (!currentUser) {
            return { success: false, message: 'Debes iniciar sesión' };
        }

        const combo = getComboById(pedidoData.comboId);
        
        if (!combo) {
            return { success: false, message: 'Combo no encontrado' };
        }
        
        if (!combo.activo) {
            return { success: false, message: 'Combo no disponible' };
        }
        
        if (combo.disponibles < pedidoData.cantidad) {
            return { success: false, message: `Solo quedan ${combo.disponibles} disponibles` };
        }

        // Obtener pedidos actuales
        const pedidos = JSON.parse(localStorage.getItem('elementioPedidos') || '[]');
        
        const nuevoPedido = {
            id: Date.now(),
            usuario: currentUser.username,
            comboId: pedidoData.comboId,
            comboNombre: combo.nombre,
            tipoPago: pedidoData.tipoPago,
            cantidad: pedidoData.cantidad,
            precioUnitario: pedidoData.tipoPago === 'diario' ? combo.precioDiario : combo.precioMensual,
            total: (pedidoData.tipoPago === 'diario' ? combo.precioDiario : combo.precioMensual) * pedidoData.cantidad,
            fechaPedido: new Date().toISOString(),
            estado: 'completado'
        };

        // Actualizar disponibilidad del combo
        const combos = getCombos();
        const comboIndex = combos.findIndex(c => c.id === pedidoData.comboId);
        if (comboIndex !== -1) {
            combos[comboIndex].disponibles -= pedidoData.cantidad;
            localStorage.setItem('elementioCombos', JSON.stringify(combos));
        }

        // Guardar pedido
        pedidos.push(nuevoPedido);
        localStorage.setItem('elementioPedidos', JSON.stringify(pedidos));
        
        console.log('✅ Pedido realizado:', nuevoPedido);
        return { 
            success: true, 
            message: 'Pedido realizado exitosamente', 
            pedido: nuevoPedido 
        };
        
    } catch (error) {
        console.error('❌ Error realizando pedido:', error);
        return { success: false, message: 'Error al realizar pedido' };
    }
}

// Obtener pedidos del usuario actual
function getPedidosUsuario() {
    const pedidos = JSON.parse(localStorage.getItem('elementioPedidos') || '[]');
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    
    if (!currentUser) return [];
    
    const userPedidos = pedidos.filter(pedido => pedido.usuario === currentUser.username);
    console.log('📋 Pedidos del usuario:', userPedidos.length);
    return userPedidos;
}