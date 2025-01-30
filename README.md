# Проект на основе Docker Compose

## Описание
Этот проект представляет собой кластеризированную систему, состоящую из нескольких сервисов, взаимодействующих друг с другом. Для запуска требуется поднять кластер с помощью `docker-compose`.

## Сервисы

- **db** — PostgreSQL 17, содержит основную базу данных.
- **rabbitmq** — RabbitMQ с поддержкой Management UI.
- **elasticsearch** — Elasticsearch 8.16.0 для хранения и поиска данных.
- **jobmanager** — Менеджер задач Flink.
- **taskmanager** — Исполнитель задач Flink.
- **dbi-core** — Основной сервис приложения на Spring Boot.
- **dbi-transactions** — Сервис обработки транзакций.

## Запуск проекта

Для старта требуется только задать необходимые параметры окружения для dbi-core перед запуском `docker-compose up -d`:

```env
CURRENCY_API_URL=https://v6.exchangerate-api.com/v6/<ваш_ключ>/latest/
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_YANDEX_CLIENT_ID=<ваш_ключ>
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_YANDEX_CLIENT_SECRET=<ваш_ключ>
```

## Запуск

1. Убедитесь, что у вас установлен Docker и Docker Compose.
2. Внесите необходимые изменения в `.env` файл или передайте переменные окружения напрямую.
3. Запустите контейнеры:
   ```sh
   docker-compose up -d
   ```
4. Откройте `http://localhost:8080` для доступа к основному сервису.

## Полезные команды

- Остановка всех сервисов:
  ```sh
  docker-compose down
  ```
- Перезапуск контейнера:
  ```sh
  docker-compose restart <service_name>
  ```
- Просмотр логов:
  ```sh
  docker-compose logs -f <service_name>
  ```

## Дополнительно

Для корректной работы убедитесь, что выделено достаточно ресурсов для Elasticsearch и Flink. При необходимости настройте ограничения CPU/RAM для контейнеров в `docker-compose.yml`.

