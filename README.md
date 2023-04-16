# Предварительные настройки проекта

Экспорт переменных окружения для Unix ОС

```shell
export DB_USER=mobiledev \
&& export DB_NAME=mobiledev \
&& export DB_PASS=mobiledev \
&& export DB_URL=jdbc:postgresql://localhost:5432/mobiledev?currentSchema=workflow'
```

Экспорт переменных окружения для Windows

```shell
$env:DB_USER = 'mobiledev'
$env:DB_NAME = 'mobiledev'
$env:DB_PASS = 'mobiledev'
$env:DB_URL = 'jdbc:postgresql://localhost:5432/mobiledev?currentSchema=workflow'
```

> Переменные окружения видны только в той консоли, в которую были экспортированы!

# База данных

## Создание БД

Запуск docker compose

```shell
docker compose up -d
```

## Миграции
Все изменения в структуре базы оформляем через миграции, при старте приложения они автоматически
применятся.

Накатить миграции

```shell
mvn flyway:migrate
```

Пример миграции:

```
./src/main/resources/db/migration/V0_1_0__create_table_role.sql
```

Имя файла должно следовать соглашению:

* Сначала префикс `V0_1_X`, где `V0_1` это номер версии приложения, `X` номер минграции.
* Потом краткое описание, что там за изменения `create_table_test`
* Префикс отделяется от описания двойным подчеркиванием `__`

# Сборка проекта

Для очистки от файлов сборки

```
./mvnw clean
```

Для сборки

```
./mvnw package
```

Для проверки и запуска тестов

```
./mvnw verify
```
