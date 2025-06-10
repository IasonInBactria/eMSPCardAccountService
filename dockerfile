# 使用OpenJDK 8作为基础镜像
FROM openjdk:8-jdk-slim

# 设置工作目录
WORKDIR /app

# 暴露应用程序运行的端口
EXPOSE 8080

# 复制Maven构建生成的JAR文件到容器中
COPY target/eMSPCardAccountService-*.jar app.jar

# 定义容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
