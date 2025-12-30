
# Базовый образ с JDK
FROM eclipse-temurin:17-jre-alpine

# Добавим информацию о владельце
LABEL maintainer="Alexander Kuziv <makklays@gmail.com>"

# Создаём рабочую директорию
WORKDIR /app

# Копируем jar-файл
COPY target/java-credit-cards-microservice-1.0-SNAPSHOT.jar app.jar

# Открываем порт, на котором работает сервис
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java",
  "-XX:+UseContainerSupport",
  "-XX:MaxRAMPercentage=75",
  "-jar",
  "app.jar"
]

# Комментарии:
# 1. Используется образ Eclipse Temurin с JDK 17 на базе Alpine Linux для минимального размера.
# 2. Рабочая директория устанавливается в /app.
# 3. Копируется скомпилированный jar-файл из локальной директории target в контейнер.
# 4. Открывается порт 8080 для доступа к микросервису.
# 5. Устанавливается команда запуска приложения при старте контейнера.

