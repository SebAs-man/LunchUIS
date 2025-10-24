// admin-crud.js - CONECTADO CON BACKEND
console.log('✅ admin-crud.js cargado - Modo Backend');

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Inicializando admin...');
    
    // Verificar que sea admin
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (!currentUser || currentUser.type !== 'admin') {
        alert('No tienes permisos de administrador');
        window.location.href = 'login.html';
        return;
    }

    console.log('✅ Admin verificado:', currentUser.username);
    document.getElementById('adminName').textContent = currentUser.fullName;
    
    initializeAdmin();
});

function initializeAdmin() {
    console.log('🔧 Configurando admin...');
    
    // Navegación entre tabs
    initializeTabs();
    
    // Botón nuevo combo
    document.getElementById('nuevoComboBtn').addEventListener('click', showComboModal);
    
    // Formulario combo
    document.getElementById('comboForm').addEventListener('submit', saveCombo);
    
    // Botón cancelar
    document.getElementById('cancelComboBtn').addEventListener('click', hideComboModal);
    
    // Cargar datos iniciales
    loadCombos();
    loadPedidos();
    loadEstadisticas();
    
    console.log('✅ Admin listo');
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
            
            // Recargar datos
            if (this.dataset.tab === 'combos') {
                loadCombos();
            } else if (this.dataset.tab === 'pedidos') {
                loadPedidos();
            } else if (this.dataset.tab === 'estadisticas') {
                loadEstadisticas();
            }
        });
    });
}

function loadCombos() {
    console.log('📦 Cargando combos...');
    
    try {
        const combos = getCombos();
        const combosList = document.getElementById('combosList');
        
        if (combos.length === 0) {
            combosList.innerHTML = `
                <div class="empty-state">
                    <p>No hay combos creados</p>
                    <button class="btn-primary" onclick="showComboModal()">Crear Primer Combo</button>
                </div>
            `;
            return;
        }
        
        combosList.innerHTML = combos.map(combo => `
            <div class="combo-card ${!combo.activo ? 'inactive' : ''}">
                <div class="combo-header">
                    <h3>${combo.nombre}</h3>
                    <div class="combo-actions">
                        <button class="btn-edit" onclick="editCombo(${combo.id})">
                            ✏️ Editar
                        </button>
                        <button class="btn-delete" onclick="deleteCombo(${combo.id})">
                            🗑️ Eliminar
                        </button>
                        <button class="btn-toggle" onclick="toggleCombo(${combo.id})">
                            ${combo.activo ? '❌ Desactivar' : '✅ Activar'}
                        </button>
                    </div>
                </div>
                <p class="combo-desc">${combo.descripcion}</p>
                <div class="combo-details">
                    <div class="detail-item">
                        <span class="label">Precio Diario:</span>
                        <span class="value">$${combo.precioDiario?.toLocaleString() || '0'}</span>
                    </div>
                    <div class="detail-item">
                        <span class="label">Precio Mensual:</span>
                        <span class="value">$${combo.precioMensual?.toLocaleString() || '0'}</span>
                    </div>
                    <div class="detail-item">
                        <span class="label">Disponibles:</span>
                        <span class="value ${combo.disponibles <= 10 ? 'low-stock' : ''}">
                            ${combo.disponibles}
                        </span>
                    </div>
                    <div class="detail-item">
                        <span class="label">Estado:</span>
                        <span class="value ${combo.activo ? 'active' : 'inactive'}">
                            ${combo.activo ? '✅ Activo' : '❌ Inactivo'}
                        </span>
                    </div>
                </div>
                <div class="combo-footer">
                    <small>Creado: ${new Date(combo.fechaCreacion).toLocaleDateString()}</small>
                </div>
            </div>
        `).join('');
        
        console.log('✅ Combos cargados:', combos.length);
    } catch (error) {
        console.error('❌ Error cargando combos:', error);
        showNotification('Error al cargar combos', 'error');
    }
}

function showComboModal(combo = null) {
    const modal = document.getElementById('comboModal');
    const title = document.getElementById('modalTitle');
    
    if (combo) {
        title.textContent = 'Editar Combo';
        document.getElementById('comboId').value = combo.id;
        document.getElementById('comboNombre').value = combo.nombre;
        document.getElementById('comboDescripcion').value = combo.descripcion;
        document.getElementById('precioDiario').value = combo.precioDiario;
        document.getElementById('precioMensual').value = combo.precioMensual;
        document.getElementById('disponibles').value = combo.disponibles;
    } else {
        title.textContent = 'Nuevo Combo';
        document.getElementById('comboForm').reset();
        document.getElementById('comboId').value = '';
    }
    
    modal.style.display = 'flex';
}

function hideComboModal() {
    document.getElementById('comboModal').style.display = 'none';
}

function saveCombo(e) {
    e.preventDefault();
    
    const comboId = document.getElementById('comboId').value;
    const comboData = {
        nombre: document.getElementById('comboNombre').value,
        descripcion: document.getElementById('comboDescripcion').value,
        precioDiario: parseInt(document.getElementById('precioDiario').value),
        precioMensual: parseInt(document.getElementById('precioMensual').value),
        disponibles: parseInt(document.getElementById('disponibles').value)
    };
    
    let result;
    if (comboId) {
        result = editarCombo(parseInt(comboId), comboData);
    } else {
        result = crearCombo(comboData);
    }
    
    if (result.success) {
        hideComboModal();
        loadCombos();
        showNotification(result.message, 'success');
    } else {
        showNotification(result.message, 'error');
    }
}

function editCombo(id) {
    const combo = getComboById(id);
    if (combo) {
        showComboModal(combo);
    }
}

function deleteCombo(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este combo?')) {
        const result = eliminarCombo(id);
        
        if (result.success) {
            loadCombos();
            showNotification(result.message, 'success');
        } else {
            showNotification(result.message, 'error');
        }
    }
}

function toggleCombo(id) {
    const combo = getComboById(id);
    if (combo) {
        const result = editarCombo(id, {
            activo: !combo.activo
        });
        
        if (result.success) {
            loadCombos();
            showNotification(`Combo ${!combo.activo ? 'activado' : 'desactivado'}`, 'success');
        }
    }
}

function loadPedidos() {
    const pedidos = getAllPedidos();
    const pedidosList = document.getElementById('pedidosList');
    
    if (pedidos.length === 0) {
        pedidosList.innerHTML = `
            <div class="empty-state">
                <p>No hay pedidos realizados</p>
            </div>
        `;
        return;
    }
    
    pedidos.sort((a, b) => new Date(b.fechaPedido) - new Date(a.fechaPedido));
    
    pedidosList.innerHTML = pedidos.map(pedido => `
        <div class="pedido-card">
            <div class="pedido-header">
                <h4>${pedido.comboNombre}</h4>
                <span class="pedido-fecha">${new Date(pedido.fechaPedido).toLocaleDateString()}</span>
            </div>
            <div class="pedido-details">
                <div class="detail">
                    <span class="label">Usuario:</span>
                    <span class="value">${pedido.usuario}</span>
                </div>
                <div class="detail">
                    <span class="label">Tipo Pago:</span>
                    <span class="value ${pedido.tipoPago}">${pedido.tipoPago === 'diario' ? 'Diario' : 'Mensual'}</span>
                </div>
                <div class="detail">
                    <span class="label">Cantidad:</span>
                    <span class="value">${pedido.cantidad}</span>
                </div>
                <div class="detail">
                    <span class="label">Total:</span>
                    <span class="value">$${(pedido.total || 0).toLocaleString()}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function loadEstadisticas() {
    const combos = getCombos();
    const pedidos = getAllPedidos();
    
    // Combos activos
    const combosActivos = combos.filter(c => c.activo);
    document.getElementById('totalCombos').textContent = combosActivos.length;
    
    // Pedidos de hoy
    const hoy = new Date().toDateString();
    const pedidosHoy = pedidos.filter(p => new Date(p.fechaPedido).toDateString() === hoy);
    document.getElementById('pedidosHoy').textContent = pedidosHoy.length;
    
    // Ingresos mensuales
    const mesActual = new Date().getMonth();
    const pedidosMes = pedidos.filter(p => new Date(p.fechaPedido).getMonth() === mesActual);
    const ingresos = pedidosMes.reduce((sum, pedido) => sum + (pedido.total || 0), 0);
    document.getElementById('ingresosMensuales').textContent = `$${ingresos.toLocaleString()}`;
}

function showNotification(message, type) {
    // Crear notificación
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
        background: ${type === 'success' ? '#38a169' : '#e53e3e'};
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Cerrar modal haciendo click fuera
document.addEventListener('click', function(e) {
    const modal = document.getElementById('comboModal');
    if (e.target === modal) {
        hideComboModal();
    }
});

// Logout
document.getElementById('logoutBtn').addEventListener('click', function() {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        logout();
    }
});