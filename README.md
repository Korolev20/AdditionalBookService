
БД Oracle

Таблица human:

CREATE TABLE Human (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    username VARCHAR2(100) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    CONSTRAINT human_pk PRIMARY KEY (id)
);

Таблица book:

CREATE TABLE book (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    isbn NUMBER NOT NULL,
    name VARCHAR2(100) NOT NULL,
    genre VARCHAR2(100) NOT NULL,
    description VARCHAR2(100) NOT NULL,
    author VARCHAR2(100) NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY (id)
);

     -isbn:код книги по международнмоу стандарту
     -name:название книги
     -genre:жанр книги
     -description: описание книги
     -author: автор книги
Таблица accountingOfBooks (Учет свободных книг):

CREATE TABLE AccountingOfBooks
(id int GENERATED by default as identity primary key,
    book_id     int,
    date_taken  TIMESTAMP,
    date_return TIMESTAMP);
ALTER TABLE AccountingOfBooks
    ADD CONSTRAINT fk_book_id
        FOREIGN KEY (book_id)
            REFERENCES book (id)
            ON DELETE CASCADE;
            
    - book_id: id книги таблицы book
    - date_taken: время когда книга была взята
    - date_return: время когда книга была возвращенна 

ПЕРВЫЙ СЕРВИС

2 Регистрация http://localhost:8080/auth/registration

Post-запрос:
{"username":"…", "password":"…”}.

Если такое имя уже имеется в БД - {"message": "Человек с таким именем уже найден"}.
При успешной регистрации, в БД добавляется username и зашифрованный пароль от BCryptPasswordEncoder и создается JWT токен на основе username и возвращается клиенту. Токен действителен 1 месяц.

3 Аутентификация http://localhost:8080/auth/login

Post-запрос:
For example {"username":"user", "password":"12345”}.
Если username и password совпадают, то создается новый токен и возвращается клиенту.

4 Работа с библиотекой

Для выполнения запросов, нужно сначала зарегистрироваться, чтобы получить JWT токен, после добавлять в header: key – Authorization и value – Bearer {JWT  токен} после чего можно выполнять запросы:

4.1 Getting a list of all books (GET) - http://localhost:8080/books/findAll

4.2 Receiving a book by id (GET) - http://localhost:8080/books/findById/{id}

4.3 Deleting a book by id (POST) - http://localhost:8080/books/deleteById/{id}
На втором сервисе происходит автоматическое удаление книги.

4.4 Receiving a book by Isbn (POST) - http://localhost:8080/books/findByIsbn/{isbn}
Получение книги по полю Isbn

4.5 Creating books and sending id books to a second server (service) (POST) http://localhost:8080/books/createBook
{
    "isbn":…,
    "name":"…",
    "genre":"…",
    "description":"…",
    "author":"…"
}

При создании книги, отправляется на второй сервер (сервис) Id, только что созданной книги.

4.6 Updating a book by id (POST) - http://localhost:8080/books/updateBook/{id}

{
    "isbn":…,
    "name":"…",
    "genre":"…",
    "description":"…",
    "author":"…"
}


4.7 Taking a book (POST) - http://localhost:8080/books/takeBook/{id}
На второй сервер (сервис) посылается объект, состоящий из: bookId, времени взятия книги (текущие дата и время), и времени, когда книгу надо вернуть (текущие дата и время + один месяц).

4.8 Returning a book (POST) - http://localhost:8080/books/returnBook/{id}

На второй сервер (сервис) посылается объект, состоящий из bookId. На втором сервере (сервисе) будет обнуление времени взятия и времени, когда книгу нужно вернуть.
Реализованы некоторые исключительные ситуации по типу: book not found, book not created, валидация по типу notEmpty, notNull.

ВТОРОЙ СЕРВИС

1 Создание книги учета (POST) http://localhost:8081/freeBook/createAccountingOfBooks

На данный запрос посылается bookId при выполнении запроса http://localhost:8081/books/createBook на основном (первом) сервисе.
По умолчанию книга учета имеет bookId и два NULL(пустых) поля (data_taken и data_return), представляющие время взятия книги и время возвращения. Данные характеристики книги указывают на то, что книга есть, и она свободна.

2 Изменение статуса книги (POST) http://localhost:8081/freeBook/updateStatusBook

На данный запрос посылается bookId, время взятия и время возвращения при выполнении запроса http://localhost:8081/books/takeBook/{id} на основном (первом) сервисе.
Определенная книга изменяет свой статус на “занята”, присваивается время взятия и время возвращения.

3 Обнуление статуса книги (POST) http://localhost:8081/freeBook/resetTimeBook

На данный запрос посылается bookId, при выполнении запроса http://localhost:8081/books/ returnBook/{id}} на основном (первом) сервисе.
Книга учета становится по умолчанию, книга становится свободной.

4 Методы прямого вызова (из самого второго сервиса)

4.1 Получение списка (книг учета) всех свободных книг (GET) http://localhost:8081/freeBook/findAllFreeBooks

4.2 Изменение существующей книги учета по Id (POST) http://localhost:8081/freeBook/updateBookById/{id}

Реализованы некоторые исключительные ситуации по типу: book not found, no free books, валидация по типу notEmpty, notNull.

OpenAPI 3 & Spring Boot -для первого сервера http://localhost:8080/swagger-ui/index.html#/

OpenAPI 3 & Spring Boot - для второго сервера http://localhost:8081/swagger-ui/index.html#/