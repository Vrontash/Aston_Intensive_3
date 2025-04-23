### [EN](#EN) | [RU](#RU)

# <a name="RU"></a>Требования к запуску проекта
## Требования к запуску тестов
 - Необходимо иметь Docker environment для testcontainers. Работоспособность программы 
была проверена при запущенном Docker Desktop (Docker Engine is running). 
## Требования к запуску программы
- Создать базу данных.
- Необходимо заполнить файл hibernate.properties в src.main.resources для доступа к БД:
    - **Адрес БД** hibernate.connection.url: \
      Пример: hibernate.connection.url = jdbc:postgresql://localhost:5432/Users
    - **Имя пользователя БД** hibernate.connection.username
    - **Пароль данного пользователя** hibernate.connection.password
# Требования к проекту
Написать юнит-тесты и интеграционные тесты для user-service.

Использовать JUnit 5, Mockito и Testcontainers.

Для тестирования DAO-слоя написать интеграционные тесты с использованием Testcontainers.

Для тестирования Service-слоя написать юнит-тесты с использованием Mockito.

Тесты должны быть изолированы друг от друга.

# <a name="EN"></a>Requirements for project to run
## Requirements for tests to run
 - It is necessary to have a Docker environment for the testcontainer to work. Performance 
of the program was checked using Docker Desktop (Docker Engine is running)
## Requirements for program to run
- Create DB.
- Fill out missing parts in hibernate.properties located in src.main.resources to access your DB:
    - Location of your created DB hibernate.connection.url\
      Example: hibernate.connection.url = jdbc:postgresql://localhost:5432/Users
    - DB User's name hibernate.connection.username
    - DB User's password hibernate.connection.password 

