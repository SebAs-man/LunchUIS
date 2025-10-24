# Frontend LunchUIS - Conectado con Backend

## 🚀 **Integración Completa Frontend + Backend**

El frontend de LunchUIS ahora está completamente integrado con el backend de microservicios. Puedes ejecutar todo el sistema usando Docker Compose.

## 📋 **Características Implementadas**

### ✅ **Autenticación Integrada**
- **Login con Identity Service** - Conectado con el backend
- **JWT Tokens** - Manejo automático de tokens
- **Fallback Local** - Funciona sin backend para desarrollo
- **Roles de Usuario** - Admin y Student

### ✅ **Gestión de Combos**
- **CRUD Completo** - Crear, leer, actualizar, eliminar combos
- **API Integration** - Conectado con Combo Service
- **Modo Híbrido** - Backend + LocalStorage como fallback

### ✅ **Docker Integration**
- **Contenedor Nginx** - Servidor web optimizado
- **CORS Configurado** - Comunicación entre servicios
- **Health Checks** - Monitoreo de servicios

## 🛠️ **Cómo Ejecutar el Sistema Completo**

### **Opción 1: Docker Compose (Recomendado)**

```bash
# 1. Crear archivo .env en la raíz del proyecto
cat > .env << EOF
DB_USERNAME=lunchuis_user
DB_PASSWORD=lunchuis_password
DB_NAME=lunchuis_db
DB_PORT=5432
JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXPIRATION=86400000
EOF

# 2. Ejecutar todo el sistema
docker-compose up --build

# 3. Acceder a la aplicación
# Frontend: http://localhost:3000
# Identity Service: http://localhost:8081
# Combo Service: http://localhost:8082
# Order Service: http://localhost:8083
```

### **Opción 2: Desarrollo Local**

```bash
# 1. Ejecutar solo el backend
docker-compose up postgres config-server identity-server combo-server order-server

# 2. Servir frontend localmente
cd frontend
python -m http.server 3000
# O usar Live Server en VS Code
```

## 🔐 **Credenciales de Prueba**

### **Administrador**
- **Código Institucional:** `9999999`
- **Contraseña:** `@dmIn123`

### **Estudiante**
- **Código Institucional:** `2180001`
- **Contraseña:** `Password123!`

## 🌐 **URLs del Sistema**

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | http://localhost:3000 | Interfaz de usuario |
| **Identity Service** | http://localhost:8081 | Autenticación y usuarios |
| **Combo Service** | http://localhost:8082 | Gestión de combos |
| **Order Service** | http://localhost:8083 | Gestión de pedidos |
| **Config Server** | http://localhost:8888 | Configuración centralizada |
| **PostgreSQL** | localhost:5432 | Base de datos |

## 📱 **Funcionalidades por Rol**

### **👨‍💼 Administrador**
- ✅ **Gestión de Combos** - CRUD completo
- ✅ **Visualización de Pedidos** - Todos los pedidos
- ✅ **Estadísticas** - Métricas del sistema
- ✅ **Control de Precios** - Diario y mensual

### **👤 Estudiante**
- ✅ **Catálogo de Combos** - Ver combos disponibles
- ✅ **Carrito de Compras** - Agregar combos
- ✅ **Selección de Pago** - Diario o mensual
- ✅ **Historial Personal** - Sus pedidos

## 🔧 **Arquitectura Técnica**

### **Frontend**
- **HTML5** - Estructura semántica
- **CSS3** - Diseño moderno con variables CSS
- **JavaScript Vanilla** - Sin frameworks, máximo rendimiento
- **Nginx** - Servidor web optimizado

### **Backend Integration**
- **REST APIs** - Comunicación con microservicios
- **JWT Authentication** - Tokens seguros
- **CORS** - Configuración para desarrollo
- **Error Handling** - Manejo robusto de errores

### **Modo Híbrido**
- **Backend First** - Intenta conectar con APIs
- **LocalStorage Fallback** - Funciona sin backend
- **Detección Automática** - Cambia modo según disponibilidad

## 🐛 **Debugging y Desarrollo**

### **Logs del Frontend**
```javascript
// Abrir DevTools (F12) para ver logs detallados
console.log('✅ Frontend conectado con backend');
console.log('🌐 API Request:', url);
console.log('📡 API Response:', response);
```

### **Verificar Conexión Backend**
```bash
# Verificar que los servicios estén corriendo
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

### **Modo Desarrollo**
- El frontend detecta automáticamente si el backend está disponible
- Si no hay conexión, usa datos locales para desarrollo
- Los logs indican qué modo está usando

## 📊 **Monitoreo**

### **Health Checks**
```bash
# Verificar estado de todos los servicios
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f frontend
docker-compose logs -f identity-server
docker-compose logs -f combo-server
```

### **Métricas**
- **Frontend:** http://localhost:3000
- **Backend APIs:** Swagger UI disponible en cada servicio
- **Database:** PostgreSQL en puerto 5432

## 🚨 **Solución de Problemas**

### **Error de CORS**
- Verificar que CORS esté configurado en el backend
- Revisar que las URLs sean correctas

### **Error de Autenticación**
- Verificar que el Identity Service esté corriendo
- Comprobar credenciales de prueba

### **Error de Conexión**
- Verificar que todos los servicios estén corriendo
- Revisar logs de Docker Compose

## 🎯 **Próximos Pasos**

1. **Implementar Order Service** - Gestión completa de pedidos
2. **Agregar Tests** - Pruebas unitarias y de integración
3. **Optimizar Performance** - Caching y lazy loading
4. **Mejorar UX** - Animaciones y feedback visual
5. **Deploy en Producción** - Configuración para producción

---

**¡El sistema está listo para usar!** 🎉

Ejecuta `docker-compose up --build` y accede a http://localhost:3000 para comenzar.
