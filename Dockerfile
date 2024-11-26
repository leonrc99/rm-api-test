FROM openjdk:17-jdk-slim

# Adicionar argumentos
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ARG MERCADO_PAGO_ACCESS_TOKEN

# Configurar as variáveis no ambiente do container
ENV DB_URL=$DB_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV MERCADO_PAGO_ACCESS_TOKEN=$MERCADO_PAGO_ACCESS_TOKEN

# Adicionar o JAR da aplicação
ARG JAR_FILE=target/minha-aplicacao.jar
COPY ${JAR_FILE} app.jar

# Expor a porta
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]