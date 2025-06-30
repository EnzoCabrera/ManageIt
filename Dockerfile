# Use a imagem oficial do Java como base
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR gerado pelo Maven para dentro do contêiner
COPY target/estoque-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que seu Spring Boot usa (geralmente 8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
