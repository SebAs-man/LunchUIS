// user-crud.js - Funcionalidades para el usuario
console.log('✅ user-crud.js cargado');

let carrito = [];

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Inicializando usuario...');
    
    // Verificar que esté logueado
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (!currentUser) {
        window.location.href = 'login.html';
        return;
    }

    console.log('✅ Usuario verificado:', currentUser.username);
    document.getElementById('userName').textContent = currentUser.fullName;
    
    initializeUser();
});

function initializeUser() {
    console.log('🔧 Configurando funciones de usuario...');
    
    // Cargar carrito desde localStorage
    carrito = JSON.parse(localStorage.getItem('userCarrito') || '[]');
    
    // Navegación entre tabs
    initializeTabs();
    
    // Botones del carrito
    document.getElementById('vaciarCarritoBtn').addEventListener('click', vaciarCarrito);
    document.getElementById('confirmarPedidoBtn').addEventListener('click', mostrarConfirmacion);
    
    // Botones del modal de confirmación
    document.getElementById('cancelConfirmBtn').addEventListener('click', cerrarConfirmacion);
    document.getElementById('confirmPedidoBtn').addEventListener('click', confirmarPedido);
    
    // Cargar datos iniciales
    loadCombosDisponibles();
    loadCarrito();
    loadHistorial();
    
    console.log('✅ Usuario listo');
}

function initializeTabs() {
    const tabButtons = document.querySelectorAll('.nav-btn');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const tabId = this.dataset.tab + '-tab';
            
            // Remover activo de todos
            tabButtons.forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            
            // Activar actual
            this.classList.add('active');
            document.getElementById(tabId).classList.add('active');
            
            // Recargar datos según tab
            if (this.dataset.tab === 'combos') {
                loadCombosDisponibles();
            } else if (this.dataset.tab === 'carrito') {
                loadCarrito();
            } else if (this.dataset.tab === 'historial') {
                loadHistorial();
            }
        });
    });
}

function loadCombosDisponibles() {
    console.log('📦 Cargando combos disponibles...');
    
    try {
        const combos = getCombosDisponibles();
        const combosContainer = document.getElementById('combosDisponibles');
        
        if (combos.length === 0) {
            combosContainer.innerHTML = `
                <div class="empty-state">
                    <p>No hay combos disponibles en este momento</p>
                </div>
            `;
            return;
        }
        
        combosContainer.innerHTML = combos.map(combo => `
            <div class="combo-card">
                <div class="combo-header">
                    <h3>${combo.nombre}</h3>
                    <div class="combo-precios">
                        <div class="precio-diario">$${combo.precioDiario?.toLocaleString()}</div>
                        <div class="precio-mensual">Mensual: $${combo.precioMensual?.toLocaleString()}</div>
                    </div>
                </div>
                <p class="combo-desc">${combo.descripcion}</p>
                <div class="combo-stock">
                    <span class="stock-info">Disponibles:</span>
                    <span class="stock-disponible ${combo.disponibles <= 10 ? 'stock-bajo' : ''}">
                        ${combo.disponibles} unidades
                    </span>
                </div>
                <div class="combo-actions">
                    <div class="cantidad-control">
                        <button class="cantidad-btn" onclick="cambiarCantidad(${combo.id}, -1)">-</button>
                        <input type="number" id="cantidad-${combo.id}" value="1" min="1" max="${combo.disponibles}" class="cantidad-input">
                        <button class="cantidad-btn" onclick="cambiarCantidad(${combo.id}, 1)">+</button>
                    </div>
                    <button class="btn-agregar" onclick="agregarAlCarrito(${combo.id})" 
                            ${combo.disponibles === 0 ? 'disabled' : ''}>
                        ${combo.disponibles === 0 ? 'Agotado' : 'Agregar al Carrito'}
                    </button>
                </div>
            </div>
        `).join('');
        
        console.log('✅ Combos cargados:', combos.length);
    } catch (error) {
        console.error('❌ Error cargando combos:', error);
        showNotification('Error al cargar combos disponibles', 'error');
    }
}

function cambiarCantidad(comboId, cambio) {
    const input = document.getElementById(`cantidad-${comboId}`);
    let cantidad = parseInt(input.value) + cambio;
    const combo = getComboById(comboId);
    
    if (cantidad < 1) cantidad = 1;
    if (cantidad > combo.disponibles) cantidad = combo.disponibles;
    
    input.value = cantidad;
}

function agregarAlCarrito(comboId) {
    const input = document.getElementById(`cantidad-${comboId}`);
    const cantidad = parseInt(input.value);
    const combo = getComboById(comboId);
    
    if (!combo) {
        showNotification('Combo no encontrado', 'error');
        return;
    }
    
    if (cantidad > combo.disponibles) {
        showNotification(`Solo quedan ${combo.disponibles} unidades disponibles`, 'error');
        return;
    }
    
    // Verificar si ya está en el carrito
    const itemIndex = carrito.findIndex(item => item.comboId === comboId);
    
    if (itemIndex > -1) {
        // Actualizar cantidad
        carrito[itemIndex].cantidad += cantidad;
    } else {
        // Agregar nuevo item
        carrito.push({
            comboId: comboId,
            nombre: combo.nombre,
            precioDiario: combo.precioDiario,
            precioMensual: combo.precioMensual,
            cantidad: cantidad,
            tipoPago: 'diario' // Por defecto pago diario
        });
    }
    
    // Guardar carrito
    guardarCarrito();
    
    showNotification(`${cantidad} ${combo.nombre} agregado(s) al carrito`, 'success');
    input.value = 1; // Resetear cantidad
    
    // Actualizar contador del carrito
    actualizarContadorCarrito();
}

function guardarCarrito() {
    localStorage.setItem('userCarrito', JSON.stringify(carrito));
}

function loadCarrito() {
    const carritoContainer = document.getElementById('carritoItems');
    const carritoTotal = document.getElementById('carritoTotal');
    
    if (carrito.length === 0) {
        carritoContainer.innerHTML = `
            <div class="empty-state">
                <p>Tu carrito está vacío</p>
                <p>Agrega algunos combos para continuar</p>
            </div>
        `;
        carritoTotal.textContent = '0';
        return;
    }
    
    let total = 0;
    
    carritoContainer.innerHTML = carrito.map((item, index) => {
        const subtotal = item.cantidad * (item.tipoPago === 'diario' ? item.precioDiario : item.precioMensual);
        total += subtotal;
        
        return `
            <div class="carrito-item">
                <div class="item-info">
                    <h4>${item.nombre}</h4>
                    <div class="item-details">
                        <span>Cantidad: ${item.cantidad}</span>
                        <span>Tipo: ${item.tipoPago === 'diario' ? 'Diario' : 'Mensual'}</span>
                        <span class="item-precio">Subtotal: $${subtotal.toLocaleString()}</span>
                    </div>
                </div>
                <div class="item-actions">
                    <select onchange="cambiarTipoPago(${index}, this.value)">
                        <option value="diario" ${item.tipoPago === 'diario' ? 'selected' : ''}>Pago Diario</option>
                        <option value="mensual" ${item.tipoPago === 'mensual' ? 'selected' : ''}>Pago Mensual</option>
                    </select>
                    <button class="btn-eliminar" onclick="eliminarDelCarrito(${index})">Eliminar</button>
                </div>
            </div>
        `;
    }).join('');
    
    carritoTotal.textContent = total.toLocaleString();
    actualizarContadorCarrito();
}

function cambiarTipoPago(index, tipoPago) {
    carrito[index].tipoPago = tipoPago;
    guardarCarrito();
    loadCarrito(); // Recargar para actualizar totales
}

function eliminarDelCarrito(index) {
    carrito.splice(index, 1);
    guardarCarrito();
    loadCarrito();
    loadCombosDisponibles(); // Recargar combos por si cambia disponibilidad
    showNotification('Item eliminado del carrito', 'success');
}

function vaciarCarrito() {
    if (carrito.length === 0) {
        showNotification('El carrito ya está vacío', 'info');
        return;
    }
    
    if (confirm('¿Estás seguro de que quieres vaciar el carrito?')) {
        carrito = [];
        guardarCarrito();
        loadCarrito();
        loadCombosDisponibles();
        showNotification('Carrito vaciado', 'success');
    }
}

function actualizarContadorCarrito() {
    const totalItems = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    document.getElementById('carritoCount').textContent = totalItems;
}

function mostrarConfirmacion() {
    if (carrito.length === 0) {
        showNotification('El carrito está vacío', 'error');
        return;
    }
    
    const modal = document.getElementById('confirmModal');
    const details = document.getElementById('confirmDetails');
    
    let total = 0;
    let html = '<div class="confirm-items">';
    
    carrito.forEach(item => {
        const subtotal = item.cantidad * (item.tipoPago === 'diario' ? item.precioDiario : item.precioMensual);
        total += subtotal;
        
        html += `
            <div class="confirm-item">
                <strong>${item.nombre}</strong><br>
                Cantidad: ${item.cantidad} | Tipo: ${item.tipoPago === 'diario' ? 'Diario' : 'Mensual'} | Subtotal: $${subtotal.toLocaleString()}
            </div>
        `;
    });
    
    html += `</div><div class="confirm-total"><strong>Total: $${total.toLocaleString()}</strong></div>`;
    details.innerHTML = html;
    
    modal.style.display = 'flex';
}

function cerrarConfirmacion() {
    document.getElementById('confirmModal').style.display = 'none';
}

function confirmarPedido() {
    console.log('✅ Confirmando pedido...');
    
    try {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        
        // Procesar cada item del carrito
        carrito.forEach(item => {
            const resultado = realizarPedido({
                comboId: item.comboId,
                tipoPago: item.tipoPago,
                cantidad: item.cantidad
            });
            
            if (!resultado.success) {
                throw new Error(resultado.message);
            }
        });
        
        // Vaciar carrito después de confirmar
        carrito = [];
        guardarCarrito();
        
        cerrarConfirmacion();
        showNotification('¡Pedido confirmado exitosamente!', 'success');
        
        // Recargar todas las vistas
        loadCombosDisponibles();
        loadCarrito();
        loadHistorial();
        
    } catch (error) {
        console.error('❌ Error confirmando pedido:', error);
        showNotification('Error al confirmar pedido: ' + error.message, 'error');
    }
}

function loadHistorial() {
    console.log('📋 Cargando historial...');
    
    try {
        const pedidos = getPedidosUsuario();
        const historialContainer = document.getElementById('historialPedidos');
        
        // Ordenar por fecha más reciente
        pedidos.sort((a, b) => new Date(b.fechaPedido) - new Date(a.fechaPedido));
        
        if (pedidos.length === 0) {
            historialContainer.innerHTML = `
                <div class="empty-state">
                    <p>No tienes pedidos realizados</p>
                    <p>Realiza tu primer pedido para ver tu historial</p>
                </div>
            `;
            return;
        }
        
        historialContainer.innerHTML = pedidos.map(pedido => `
            <div class="historial-item">
                <div class="historial-header">
                    <h4>${pedido.comboNombre}</h4>
                    <div class="historial-info">
                        <span class="historial-fecha">${new Date(pedido.fechaPedido).toLocaleDateString()}</span>
                        <span class="historial-estado estado-${pedido.estado}">${pedido.estado}</span>
                    </div>
                </div>
                <div class="historial-details">
                    <div class="historial-detail">
                        <span class="detail-label">Tipo de Pago</span>
                        <span class="detail-value">${pedido.tipoPago === 'diario' ? 'Diario' : 'Mensual'}</span>
                    </div>
                    <div class="historial-detail">
                        <span class="detail-label">Cantidad</span>
                        <span class="detail-value">${pedido.cantidad}</span>
                    </div>
                    <div class="historial-detail">
                        <span class="detail-label">Precio Unitario</span>
                        <span class="detail-value">$${pedido.precioUnitario?.toLocaleString()}</span>
                    </div>
                    <div class="historial-detail">
                        <span class="detail-label">Total</span>
                        <span class="detail-value">$${pedido.total?.toLocaleString()}</span>
                    </div>
                </div>
            </div>
        `).join('');
        
        console.log('✅ Historial cargado:', pedidos.length);
    } catch (error) {
        console.error('❌ Error cargando historial:', error);
        showNotification('Error al cargar historial', 'error');
    }
}

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 8px;
        color: white;
        font-weight: 500;
        z-index: 1000;
        background: ${type === 'success' ? '#38a169' : 
                     type === 'error' ? '#e53e3e' : 
                     type === 'info' ? '#3182ce' : '#d69e2e'};
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Logout
document.getElementById('logoutBtn').addEventListener('click', function() {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        logout();
    }
});