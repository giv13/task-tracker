# Планировщик задач

Многопользовательский планировщик задач, написанный в учебных целях на основе [данного ТЗ](sow.md) с небольшими отклонениями и улучшениями.

- [Ссылка](http://tt.giv13.beget.tech/)
- [API-документация](http://tt.giv13.beget.tech/api/swagger-ui/index.html) (Swagger)

## Используемый стек

### Backend

![Java](https://img.shields.io/badge/Java-ED8B00.svg?logo=openjdk&logoColor=fff)&nbsp;
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)&nbsp;
![Spring Security (JWT)](https://img.shields.io/badge/Spring%20Security%20(JWT)-6DB33F?logo=springsecurity&logoColor=fff)&nbsp;
![Spring Web](https://img.shields.io/badge/Spring%20Web-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Spring Data JDBC](https://img.shields.io/badge/Spring%20Data%20JDBC-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Spring for Apache Kafka](https://img.shields.io/badge/Spring%20for%20Apache%20Kafka-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Spring Scheduler](https://img.shields.io/badge/Spring%20Scheduler-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Spring Mail](https://img.shields.io/badge/Spring%20Mail-6DB33F?logo=spring&logoColor=fff)&nbsp;
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?logo=hibernate&logoColor=fff)&nbsp;
![Postgres](https://img.shields.io/badge/Postgres-316192.svg?logo=postgresql&logoColor=fff)&nbsp;
![Liquibase](https://img.shields.io/badge/Liquibase-2962FF.svg?logo=liquibase&logoColor=fff)&nbsp;
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=fff)&nbsp;
![Kafka](https://img.shields.io/badge/Kafka-231F20?logo=apachekafka&logoColor=fff)&nbsp;
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)&nbsp;
![Git](https://img.shields.io/badge/Git-F05032?logo=git&logoColor=fff)&nbsp;
![OpenAPI](https://img.shields.io/badge/OpenAPI-6BA539?logo=openapiinitiative&logoColor=white)&nbsp;
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=000)

### Frontend

![Nginx](https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=fff)&nbsp;
![HTML](https://img.shields.io/badge/HTML-E34F26?logo=html5&logoColor=fff)&nbsp;
![CSS](https://img.shields.io/badge/CSS-639?logo=css&logoColor=fff)&nbsp;
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=000)&nbsp;
![jQuery](https://img.shields.io/badge/jQuery-0769AD?logo=jquery&logoColor=fff)

## Особенности

- Проект разделен на несколько сервисов: Frontend, Backend, Scheduler (планировщик), Mailer (рассыльщик писем).
- Использование брокера сообщений (Apache Kafka) для обмена информацией между сервисами.
- Авторизация на основе JWT. В том числе реализованы Refresh-токены. Хранение токенов в HttpOnly Cookie.
- Валидация пароля с помощью Passay. Кастомные валидаторы: подтверждение пароля, проверка на уникальность, проверка часового пояса.
- Стандартизация ответов от бэкенда (RestControllerAdvice).
- Использование Liquibase для миграций.
- Swagger для API-документации c рабочим решением авторизации на основе JWT.
- Adminer (веб-интерфейс для управления базами данных) и Kafbat UI (веб-интерфейс для Apache Kafka).
- Сборка приложения осуществляется через Docker. При этом наружу торчит только Nginx, который выступает в роли обратного прокси.

## Что улучшено по сравнению с [ТЗ](sow.md)

- Добавлены категории.
- Добавлены цвета и возможность выбора цвета для категории и задачи.
- Добавлено перетаскивание категорий и задач для быстрой сортировки. Задачу, в том числе, можно перетащить в другую категорию или в "Выполненные".
- Добавлена возможность менять настройки профиля.
- В том числе можно отключить ежедневные отчеты.
- В том числе добавлена возможность выбрать часовой пояс. На основе часового пояса пользователь видит даты создания и завершения задач.
- Изменена логика планировщика. Сервис формирует отчеты не раз в сутки, а каждые 30 минут. При этом в выборку попадают только те пользователи, у которых сейчас полночь. То есть каждому пользователю отчет приходит в полночь по отношению к его выбранному часовому поясу.
- Для каждого события свой топик в брокере сообщений. Всего 3 события: регистрация, смена пароля, ежедневный отчет по задачам. Также сделан dlt-топик для сохранения недоставленных сообщений.
- При регистрации и смене пароля пользователю на почту приходит сообщение с доступами. Пароль при этом передается в топик в зашифрованном виде для безопасности.

## Установка (Docker)

1. Склонировать репозиторий

    ```bash
    git clone https://github.com/giv13/task-tracker.git
    ```

2. Перейти в папку с репозиторием

    ```bash
    cd task-tracker
    ```

3. Скопировать пример файла переменных окружения

    ```bash
    cp .env.example .env
    ```

4. Открыть скопированный файл. В моем случае я открываю файл стандартным блокнотом Windows.

    ```bash
    notepad .env
    ```

5. В файле задать основной порт для доступа к приложению, настройки для БД, Kafka, Kafka UI, SMTP, в `JWT_SECRET` задать [сгенерированный секрет](https://jwtsecrets.com/#generator), в `ENCRYPTOR_PASSWORD` и `ENCRYPTOR_SALT` задать произвольные пароль и соль для шифрования данных для передачи в Kafka. Пример файла:

    ```txt
    PORT=80
   
    DB_NAME=task-tracker-db
    DB_USER=task-tracker-user
    DB_PASSWORD=task-tracker-pass
    
    JWT_SECRET=8927666ce0f474650328f3f905aeccd0211598a986e72fad36172474ec2a32f6
    
    KAFKA_USER_REGISTERED_TOPIC_NAME=task-tracker.user.registered
    KAFKA_USER_PASSWORD_CHANGED_TOPIC_NAME=task-tracker.user.password-changed
    KAFKA_USER_TASK_SUMMARY_TOPIC_NAME=task-tracker.user.task-summary
    KAFKA_USER_DLT_TOPIC_NAME=task-tracker.user.dlt
    
    KAFKA_UI_USER=kafka-ui-user
    KAFKA_UI_PASSWORD=kafka-ui-pass
    
    SMTP_HOST=smtp.gmail.com
    SMTP_PORT=587
    SMTP_USERNAME=your-username@gmail.com
    SMTP_PASSWORD=your-password
    
    ENCRYPTOR_PASSWORD=task-tracker
    ENCRYPTOR_SALT=8927666ce0f474650328f3f905aeccd0211598a986e72fad36172474ec2a32f6
    ```

6. Развернуть приложение

    ```bash
    docker-compose up -d --build
    ```

7. Приложение будет доступно по пути `http://localhost:80/` (или на другом порту, указанном в переменной `PORT`). Чтобы остановить приложение и удалить все сервисы, выполните команду:

    ```bash
    docker-compose down
    ```