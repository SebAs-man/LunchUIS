# ============================================
# STAGE 1: Build all modules (builder)
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the parent pom.xml
COPY pom.xml .

# Copy all module directories.
COPY common-library common-library/
COPY config-server config-server/
COPY identity-server identity-server/
COPY combo-server combo-server/
COPY order-server order-server/

# Build all modules at once
RUN mvn clean install -DskipTests -B

# ============================================
# STAGE 2: Config Server Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine AS config-server-runtime
WORKDIR /app
COPY --from=builder /app/config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]

# ============================================
# STAGE 3: Identity Server Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine AS identity-server-runtime
WORKDIR /app
COPY --from=builder /app/identity-server/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

# ============================================
# STAGE 4: Combo Server Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine AS combo-server-runtime
WORKDIR /app
COPY --from=builder /app/combo-server/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

# ============================================
# STAGE 5: Order Server Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine AS order-server-runtime
WORKDIR /app
COPY --from=builder /app/order-server/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
