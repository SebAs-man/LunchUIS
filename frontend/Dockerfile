# Dockerfile para Frontend
FROM nginx:alpine

# Copiar archivos del frontend
COPY . /usr/share/nginx/html

# Configurar nginx para SPA
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    \
    # Configurar CORS para desarrollo \
    add_header Access-Control-Allow-Origin "*" always; \
    add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS" always; \
    add_header Access-Control-Allow-Headers "Authorization, Content-Type" always; \
    \
    # Manejar rutas de SPA \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
    \
    # Cache para archivos estáticos \
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ { \
        expires 1y; \
        add_header Cache-Control "public, immutable"; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
