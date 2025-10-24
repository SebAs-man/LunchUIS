// admin-crud-backend.js - CONECTADO CON BACKEND
console.log('✅ admin-crud-backend.js cargado - Modo Backend');

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Inicializando admin...');
    
    // Verificar que estemos en una página de administración
    if (!document.getElementById('adminName')) {
        console.log('⚠️ No es una página de administración, saliendo...');
        return;
    }
    
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

// Cargar combos desde la API
async function loadCombos() {
    console.log('📦 Cargando combos...');
    
    try {
        const combos = await getCombosHybrid();
        console.log('✅ Combos cargados:', combos);
        
        const combosList = document.getElementById('combosList');
        if (!combosList) return;
        
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
                    <h3>${combo.nombre || combo.name}</h3>
                    <div class="combo-actions">
                        <button class="btn-edit" onclick="editCombo(${combo.id})">
                            ✏️ Editar
                        </button>
                        <button class="btn-delete" onclick="deleteCombo(${combo.id})">
                            🗑️ Eliminar
                        </button>
                        <button class="btn-toggle" onclick="toggleCombo(${combo.id})">
                            ${combo.activo !== false ? '❌ Desactivar' : '✅ Activar'}
                        </button>
                    </div>
                </div>
                <div class="combo-content">
                    <p class="combo-description">${combo.descripcion || combo.description}</p>
                    <div class="combo-prices">
                        <span class="price-daily">Diario: $${(combo.precioDiario || combo.price).toLocaleString()}</span>
                        <span class="price-monthly">Mensual: $${(combo.precioMensual || combo.monthlyPrice).toLocaleString()}</span>
                    </div>
                    <div class="combo-stock">
                        <span class="stock-label">Disponibles:</span>
                        <span class="stock-value">${combo.disponibles || combo.availableQuota}</span>
                    </div>
                    <div class="combo-status">
                        <span class="status ${combo.activo !== false ? 'active' : 'inactive'}">
                            ${combo.activo !== false ? 'Activo' : 'Inactivo'}
                        </span>
                    </div>
                </div>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('❌ Error cargando combos:', error);
        showMessage('Error cargando combos', 'error');
    }
}

// Mostrar modal de combo
function showComboModal(comboId = null) {
    const modal = document.getElementById('comboModal');
    const form = document.getElementById('comboForm');
    
    if (comboId) {
        // Modo edición
        const combos = JSON.parse(localStorage.getItem('elementioCombos') || '[]');
        const combo = combos.find(c => c.id === comboId);
        
        if (combo) {
            document.getElementById('comboNombre').value = combo.nombre;
            document.getElementById('comboDescripcion').value = combo.descripcion;
            document.getElementById('comboPrecioDiario').value = combo.precioDiario;
            document.getElementById('comboPrecioMensual').value = combo.precioMensual;
            document.getElementById('comboDisponibles').value = combo.disponibles;
            document.getElementById('comboActivo').checked = combo.activo;
            
            form.dataset.mode = 'edit';
            form.dataset.comboId = comboId;
        }
    } else {
        // Modo creación
        form.reset();
        form.dataset.mode = 'create';
        delete form.dataset.comboId;
    }
    
    modal.style.display = 'flex';
}

// Ocultar modal de combo
function hideComboModal() {
    const modal = document.getElementById('comboModal');
    modal.style.display = 'none';
}

// Guardar combo (crear o editar)
async function saveCombo(e) {
    e.preventDefault();
    
    const form = e.target;
    const mode = form.dataset.mode;
    const comboId = form.dataset.comboId;
    
    const comboData = {
        nombre: document.getElementById('comboNombre').value,
        descripcion: document.getElementById('comboDescripcion').value,
        precioDiario: parseInt(document.getElementById('comboPrecioDiario').value),
        precioMensual: parseInt(document.getElementById('comboPrecioMensual').value),
        disponibles: parseInt(document.getElementById('comboDisponibles').value),
        activo: document.getElementById('comboActivo').checked
    };
    
    try {
        let result;
        
        if (mode === 'edit') {
            result = await updateCombo(comboId, comboData);
        } else {
            result = await createCombo(comboData);
        }
        
        console.log('✅ Combo guardado:', result);
        showMessage('Combo guardado exitosamente', 'success');
        hideComboModal();
        loadCombos();
        
    } catch (error) {
        console.error('❌ Error guardando combo:', error);
        showMessage('Error guardando combo', 'error');
    }
}

// Editar combo
function editCombo(id) {
    showComboModal(id);
}

// Eliminar combo
async function deleteCombo(id) {
    if (!confirm('¿Estás seguro de que quieres eliminar este combo?')) {
        return;
    }
    
    try {
        await deleteCombo(id);
        console.log('✅ Combo eliminado');
        showMessage('Combo eliminado exitosamente', 'success');
        loadCombos();
    } catch (error) {
        console.error('❌ Error eliminando combo:', error);
        showMessage('Error eliminando combo', 'error');
    }
}

// Alternar estado del combo
async function toggleCombo(id) {
    try {
        const combos = await getCombosHybrid();
        const combo = combos.find(c => c.id === id);
        
        if (combo) {
            const newStatus = !combo.activo;
            await updateCombo(id, { activo: newStatus });
            console.log('✅ Estado del combo actualizado');
            showMessage(`Combo ${newStatus ? 'activado' : 'desactivado'}`, 'success');
            loadCombos();
        }
    } catch (error) {
        console.error('❌ Error actualizando estado:', error);
        showMessage('Error actualizando estado', 'error');
    }
}

// Cargar pedidos
async function loadPedidos() {
    console.log('📋 Cargando pedidos...');
    
    try {
        const pedidos = await getUserOrders();
        console.log('✅ Pedidos cargados:', pedidos);
        
        const pedidosList = document.getElementById('pedidosList');
        if (!pedidosList) return;
        
        if (pedidos.length === 0) {
            pedidosList.innerHTML = `
                <div class="empty-state">
                    <p>No hay pedidos realizados</p>
                </div>
            `;
            return;
        }
        
        pedidosList.innerHTML = pedidos.map(pedido => `
            <div class="pedido-card">
                <div class="pedido-header">
                    <h4>Pedido #${pedido.id}</h4>
                    <span class="pedido-fecha">${new Date(pedido.fechaCreacion).toLocaleDateString()}</span>
                </div>
                <div class="pedido-content">
                    <p><strong>Usuario:</strong> ${pedido.usuario}</p>
                    <p><strong>Combo:</strong> ${pedido.combo}</p>
                    <p><strong>Tipo:</strong> ${pedido.tipo}</p>
                    <p><strong>Total:</strong> $${pedido.total.toLocaleString()}</p>
                    <p><strong>Estado:</strong> <span class="status ${pedido.estado}">${pedido.estado}</span></p>
                </div>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('❌ Error cargando pedidos:', error);
        showMessage('Error cargando pedidos', 'error');
    }
}

// Cargar estadísticas
function loadEstadisticas() {
    console.log('📊 Cargando estadísticas...');
    
    const statsContainer = document.getElementById('estadisticas-tab');
    if (!statsContainer) return;
    
    // Estadísticas básicas (se pueden mejorar con datos reales)
    statsContainer.innerHTML = `
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total Combos</h3>
                <div class="stat-value" id="totalCombos">-</div>
            </div>
            <div class="stat-card">
                <h3>Pedidos Hoy</h3>
                <div class="stat-value" id="pedidosHoy">-</div>
            </div>
            <div class="stat-card">
                <h3>Ingresos Mensuales</h3>
                <div class="stat-value" id="ingresosMensuales">-</div>
            </div>
            <div class="stat-card">
                <h3>Usuarios Activos</h3>
                <div class="stat-value" id="usuariosActivos">-</div>
            </div>
        </div>
    `;
    
    // Cargar estadísticas reales
    loadStatsData();
}

// Cargar datos de estadísticas
async function loadStatsData() {
    try {
        const combos = await getCombosHybrid();
        const pedidos = await getUserOrders();
        
        document.getElementById('totalCombos').textContent = combos.length;
        document.getElementById('pedidosHoy').textContent = pedidos.filter(p => 
            new Date(p.fechaCreacion).toDateString() === new Date().toDateString()
        ).length;
        
        const ingresosMensuales = pedidos.reduce((total, pedido) => {
            const fechaPedido = new Date(pedido.fechaCreacion);
            const fechaActual = new Date();
            
            if (fechaPedido.getMonth() === fechaActual.getMonth() && 
                fechaPedido.getFullYear() === fechaActual.getFullYear()) {
                return total + pedido.total;
            }
            return total;
        }, 0);
        
        document.getElementById('ingresosMensuales').textContent = `$${ingresosMensuales.toLocaleString()}`;
        
    } catch (error) {
        console.error('❌ Error cargando estadísticas:', error);
    }
}

// Mostrar mensaje
function showMessage(text, type) {
    // Crear elemento de mensaje si no existe
    let messageEl = document.getElementById('message');
    if (!messageEl) {
        messageEl = document.createElement('div');
        messageEl.id = 'message';
        messageEl.className = 'message';
        document.body.appendChild(messageEl);
    }
    
    messageEl.textContent = text;
    messageEl.className = 'message ' + type;
    messageEl.style.display = 'block';
    
    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 5000);
}

// Logout
document.getElementById('logoutBtn').addEventListener('click', function() {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        logout();
    }
});

console.log('✅ admin-crud-backend.js completamente cargado');
