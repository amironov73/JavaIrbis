### Класс IrbisConnection

Класс `IrbisConnection` - "рабочая лошадка". Он осуществляет связь с сервером и всю необходимую перепаковку данных из клиентского представления в сетевое.

Экземпляр клиента создается конструктором:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static void ExampleMethod() {
        IrbisConnection client = new IrbisConnection();
    }
}
```

При создании клиента можно указать (некоторые) настройки:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static void ExampleMethod() {
        IrbisConnection client = new IrbisConnection();
        client.host = "irbis.rsl.ru";
        client.port = 5555;
        client.username = "ninja";
        client.password = "i_am_invisible";
    }
}
```

Поле|Тип|Назначение|Значение по умолчанию
----|---|----------|---------------------
host        |String | Адрес сервера|'127.0.0.1'
port        |int | Порт|6666
username    |String | Имя (логин) пользователя|`null`
password    |String | Пароль пользователя|`null`
database    |String | Имя базы данных|'IBIS'
workstation |char | Тип АРМа (см. таблицу ниже)| 'C'

Типы АРМов

Обозначение|Тип
-----------|---
'R' | Читатель
'C' | Каталогизатор
'M' | Комплектатор
'B' | Книговыдача
'K' | Книгообеспеченность
'A' | Администратор

Обратите внимание, что адрес сервера задается строкой, так что может принимать как значения вроде `192.168.1.1`, так и `irbis.yourlib.com`.

Если какой-либо из вышеперечисленных параметров не задан явно, используется значение по умолчанию.

#### Подключение к серверу и отключение от него

Только что созданный клиент еще не подключен к серверу. Подключаться необходимо явно с помощью метода `connect`, при этом можно указать параметры подключения:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static void ExampleMethod() {
        IrbisConnection client = new IrbisConnection();
        client.connect();
    }
}
```

Отключаться от сервера можно двумя способами: во-первых, с помощью метода `disconnect`:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static IrbisConnection client;
    static void ExampleMethod() {
        client.disconnect();
    }
}

```

во-вторых, с помощью контекста, задаваемого блоком `try`:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static void ExampleMethod() {
        try(IrbisConnection client = new IrbisConnection()) {
            client.host = "192.168.1.1";
            client.username = "ninja";
            client.password = "ajnin";
            client.connect();
            
            // Выполняем некие действия
            // По выходу из блока отключение от сервера
            // произойдет автоматически, даже при
            // возникновении исключения
        }
    }
}
```

При подключении клиент получает с сервера INI-файл с настройками, которые могут понадобиться в процессе работы:

```java
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.infrastructure.IniFile;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        IniFile iniFile = connection.connect();
        String formatMenuName = iniFile.getValue("Main", "FmtMnu", "FMT31.MNU");
    }
}
```

Полученный с сервера INI-файл также хранится в поле `iniFile`.

Повторная попытка подключения с помощью того же экземпляра `IrbisConnection` игнорируется. При необходимости можно создать другой экземпляр и подключиться с его помощью (если позволяют клиентские лицензии). Аналогично игнорируются повторные попытки отключения от сервера.

Проверить статус "клиент подключен или нет" можно с помощью метода `isConnected`:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        if (!connection.isConnected()) {
            // В настоящее время мы не подключены к серверу
        }
    }
}
```

Вместо индивидуального задания каждого из полей `host`, `port`, `username`, `password` и `database`, можно использовать метод `parseConnectionString`:

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        connection.parseConnectionString("host=192.168.1.4;port=5555;" +
         "username=itsme;password=secret;");
        connection.connect();
    }
}
``` 

#### Многопоточность

Клиент написан в наивном однопоточном стиле, поэтому не поддерживает одновременный вызов методов из разных потоков.

Для одновременной отсылки на сервер нескольких команд необходимо создать соответствующее количество экземпляров подключений (если подобное позволяет лицензия сервера).

#### Подтверждение подключения

`ru.arsmagna` самостоятельно не посылает на сервер подтверждений того, что клиент все еще подключен. Этим должно заниматься приложение, например, по таймеру. 

Подтверждение посылается серверу методом `nop`:
 
```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        connection.noOp();        
    }
}
```

#### Чтение записей с сервера

```java
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.MarcRecord;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        int mfn = 123;
        MarcRecord record = connection.readRecord(mfn);        
    }
}
```

#### Сохранение записи на сервере

```java
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.MarcRecord;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod(MarcRecord record) {
        connection.writeRecord(record);
    }
}
```

#### Поиск записей

```java
import ru.arsmagna.IrbisConnection;

class ExampleClass {
    static IrbisConnection connection;
    static void ExampleMethod() {
        int[] found = connection.search("\"A=ПУШКИН$\"");        
    }
}
```

Обратите внимание, что поисковый запрос заключен в дополнительные кавычки. Эти кавычки явлются элементом синтаксиса поисковых запросов ИРБИС64, и лучше их не опускать.