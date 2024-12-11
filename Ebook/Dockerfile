# Sử dụng image của Java làm base
FROM openjdk:21-jdk-slim

# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file JAR vào container
COPY target/Ebook-0.0.1-SNAPSHOT.jar app.jar

# Expose cổng 8080 (hoặc cổng khác bạn sử dụng)
EXPOSE 8080

# Câu lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
