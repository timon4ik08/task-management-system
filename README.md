# Task Manager System

Task Manager System — это API-сервис для управления задачами с поддержкой аутентификации, авторизации и ролей пользователей.

## Контроллеры

- **AuthController** — отвечает за регистрацию и аутентификацию пользователей.
- **CommentController** — управление комментариями к задачам.
- **TaskController** — управление задачами.
- **UserController** — управление пользователями.

## Технологии и версии

- **Java:** 17
- **Spring Boot:** 3.4.0
- **PostgreSQL:** 13
- **Docker Compose:** 3.8
- **Maven:** 3.9.8

## Требования

Перед запуском убедитесь, что у вас установлены:
- **Docker**

## Сборка и запуск проекта

1. Клонируйте репозиторий:
   ```sh
   git clone https://github.com/your-repo/task-manager-system.git
   cd task-manager-system
   ```

2. Запустите сервисы с помощью Docker Compose:
   ```sh
   docker-compose up --build
   ```

3. Приложение будет доступно по адресу: `http://localhost:8080`

### Переменные окружения

Docker Compose уже включает в себя базу данных PostgreSQL и задает необходимые переменные окружения:

```yaml
SPRING_PROFILES_ACTIVE: prod
SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/task-manager-system
SPRING_DATASOURCE_USERNAME: myuser
SPRING_DATASOURCE_PASSWORD: mypassword
JWT_SECRET: 9294413b9e6581917a02f42b77ac1606a76cc11368697abf0ef6a9f0675db4816ca7ec58d1f12a9526de209c1b0466148603ef7b39dcc5e1a9873559ac519cce642e8e724e86ccf2c88721d3e0b79a309821b80db82d3caf14dfe61609d54801b756c4ea1d3d2d42f1a98940ee03bf08f8ba90da8cddeeca34d0c52171f1acc79b5f0360daa18f140472b274cd5b7df315328a8bbc46789caa9cca12469a6298b9138e861cc46d95fdb49e321c5d4940a66f0f30790ac2c6d3c11440c21433a1a3584e87d646a7275f4f4e7ad9bd97a21a428ba5590e01093b21a7d2730fa95c1cbe600f626fe0ff9cde783e3e4b012ef0150ff162be7e796e68ff5076a161d6
JWT_EXPIRATION_MS: 86400000
SWAGGER_UI_PATH: /swagger-ui/index.html
SERVER_PORT: 8080
```

## Первоначальная настройка

При первом запуске автоматически создаются:
- База данных `task-manager-system`
- Администратор:
    - Логин: `admin@admin.admin`
    - Пароль: `admin`
- Роли пользователей: `ROLE_USER`, `ROLE_ADMIN`

## Документация API

Документация API доступна по адресу:
```
http://localhost:8080/swagger-ui/index.html
```

## Postman Collection

Для удобства тестирования в корне проекта находится коллекция Postman:
```
task-manager-system.postman_collection.json
```
Ее можно импортировать в Postman и использовать готовые запросы.

## Завершение работы

Чтобы остановить сервисы, выполните:
```sh
docker-compose down
```

