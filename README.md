# Network Chat

[ТЗ на Проект](https://github.com/netology-code/jd-homeworks/blob/master/diploma/networkchat.md)

## Как это работает:
- Склонировать проект.
- Запустить ChatServer через класс Main из IDE.
- Запустить ChatClient1 из IDE. В консоли появиться "Enter your name: " - ввести имя и нажать ENTER.
- Запустить ChatClient2 из IDE. В консоли появиться "Enter your name: " - ввести имя и нажать ENTER.
- Запустить ChatClient3 из IDE. В консоли появиться "Enter your name: " - ввести имя и нажать ENTER.
- Вводить сообщения поочередно из консоли ChatClient1, ChatClient2, ChatClient3 - сообщения будут отправляться в консоль подключенным клиентам.
- Для отключения клиента ввести "/exit" (в любом регистре, можно даже "/eXiT").
- Для остановки сервера нажать кнопку остановки в IDE (красный квадратик).
- После остановки Клиентов и Сервера в корне проекта появится папка logs с файлом file.log куда записались все логи и Клиентов и Сервера (при повторном запуске этот файл дополняется новыми логами)

## Структура Проекта
```
network-chat/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── client/
│   │   │   │   ├── BaseClient.java
│   │   │   │   ├── ChatClient1.java
│   │   │   │   ├── ChatClient2.java
│   │   │   │   └── ChatClient3.java
│   │   │   │ 
│   │   │   ├── clientSocket/
│   │   │   │   ├── ClientSocket.java
│   │   │   │   └── ClientSocketImpl.java
│   │   │   │ 
│   │   │   ├── server/
│   │   │   │   ├── ChatServer.java
│   │   │   │   ├── ClientHandler.java
│   │   │   │   ├── Main.java
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
 
- **src.main.java.client**
  - **BaseClient.java**: Главный класс клиента. Управляет подключением к серверу и взаимодействием с пользователем. Принимает и отправляет сообщения.
  - **ChatClient1.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент1 через метод main.
  - **ChatClient2.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент2 через метод main.
  - **ChatClient3.java**: Класс клиента для запуска. Наследник BaseClient. Запускает клиент3 через метод main.

- **src.main.java.clientSocket**
  - **ClientSocket.java**: Интерфейс, который содержит методы для управления потоками ввода-вывода клиента.
  - **ClientSocketImpl.java**: Класс, который управляет потоками ввода-вывода клиента на клиентской стороне и на серверной строне. Реализует методы SocketClient.

- **src.main.java.server**
  - **ChatServer.java**: Главный класс сервера. Отвечает за запуск сервера и управление клиентскими подключениями. Запускает новый поток ClientHandler на каждое клиентское подключение.
  - **ClientHandler.java**: Класс-поток, который управляет взаимодействием с каждым подключенным клиентом. Обрабатывает входящие сообщения от клиента и добавляет их в очередь сообщений.
  - **Main.java**: Класс, через который происходит запуск сервера.
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

## Зависимости

- **log4j-core + log4j-api**: Для логирования
- **junit-jupiter-engine + junit-jupiter-engine + mockito-core + mockito-junit-jupiter**: Для тестирования
