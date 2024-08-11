# Network Chat

[ТЗ на Проект](https://github.com/netology-code/jd-homeworks/blob/master/diploma/networkchat.md)

## Структура Проекта
```
network-chat/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── client/
│   │   │   │   ├── baseClient/
│   │   │   │   │   ├── BaseClient.java
│   │   │   │   │   ├── RealSocketClient.java
│   │   │   │   │   └── SocketClient.java
│   │   │   │   ├── ChatClient1.java
│   │   │   │   ├── ChatClient2.java
│   │   │   │   └── ChatClient3.java
│   │   │   │ 
│   │   │   ├── server/
│   │   │   │   ├── ChatServer.java
│   │   │   │   ├── ClientHandler.java
│   │   │   │   └── MessageSender.java
│   │   │   │ 
│   │   │   └── utils/
│   │   │       ├── Message.java
│   │   │       └── PropertiesLoader.java
│   │   │ 
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.xml
│   │ 
│   └── test/
│       └── java/
│           ├── client/
│           └── server/
│
├── README.md
└── pom.xml
```

## Описание Пакетов и Классов

- **src.main.java.client.baseClient**
  - **BaseClient.java**: Главный класс клиента. Управляет подключением к серверу и взаимодействием с пользователем. Принимает и отправляет сообщения.
  - **RealSocketClient.java**: Класс, который управляет потоками ввода-вывода на клиенте. Реализует методы SocketClient.
  - **SocketClient.java**: Интерфейс, который содержит методы для управления потоками ввода-вывода на клиенте.
 
- **src.main.java.client**
  - **ChatClient1.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент1 через метод main.
  - **ChatClient2.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент2 через метод main.
  - **ChatClient3.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент3 через метод main.

- **src.main.java.server**
  - **ChatServer.java**: Главный класс сервера. Отвечает за управление клиентскими подключениями. Запускает новый поток ClientHandler на каждое клиентское подключение.
  - **ClientHandler.java**: Класс-поток, который управляет взаимодействием с каждым подключенным клиентом. Обрабатывает входящие сообщения от клиента и добавляет их в очередь сообщений.
  - **MessageSender.java**: Класс-поток, который рассылает сообщения из очереди сообщений всем активным клиентам.

- **src.main.java.utils**
  - **Message.java**: Класс-модель, представляющий сообщение, которое передается между клиентом и сервером. Содержит поля для порта отправителя, имени отправителя, текста сообщения и времени отправки.
  - **PropertiesLoader.java**: Класс, загружает properties из файла с конфигурацией сервера (config.properties).

- **src.main.java.resources**
  - **config.properties**: Файл, где прописана конфигурация сервера (порт).
  - **log4j2.xml**: Файл, где прописана конфигурация логирования.
 
- **src.test.java.client**
  - тут будут тесты для клиентской части

- **src.test.java.server**
  - тут будут тесты для серверной части
