FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="axisbanking"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE ${SERVER_PORT:-8080}
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:${SERVER_PORT:-8080}/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
