# Tickets Application

## Установка

Для использования приложения Tickets выполните следующие шаги:

1. Убедитесь, что у вас установлена Java 8 или выше
2. Установите Maven для управления зависимостями и сборки проекта
3. Скопируйте или скачайте файлы исходного кода

## Быстрый старт

### Использование bash-скрипта:
```bash
chmod +x run.sh
./run.sh
```

### Ручной запуск:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="tickets.json"
```

## Структура проекта

```
ticket-analyzer/
├── src/
│   └── main/
│       └── java/
│           └── TicketClass.java
|           └── Tickets.java
|           └── TicketWrapper.java
├── pom.xml
├── run.sh
├── tickets.json
└── README.md
```

## Скрипт run.sh

```bash
#!/bin/bash

# Очистка и компиляция проекта
mvn clean compile

# Запуск приложения с файлом tickets.json в качестве аргумента
mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="tickets.json"
```

## Использование

1. **Убедитесь, что JSON-файл существует** в директории проекта:
   ```bash
   cp /путь/к/вашему/tickets.json ./
   ```

2. **Запуск с помощью bash-скрипта:**
   ```bash
   chmod +x run.sh
   ./run.sh
   ```

3. **Или ручной запуск:**
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="tickets.json"
   ```

4. **Для указания другого пути к файлу:**
   ```bash
   mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="/путь/к/вашему/tickets.json"
   ```

## Зависимости

Проект использует Maven со следующими зависимостями:
- Jackson Core 2.13.0 (для парсинга JSON)
- Maven Exec Plugin (для прямого выполнения)

## Конфигурация

### Требования к pom.xml:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
        </plugin>
    </plugins>
</build>
```

## Пример вывода

```
Минимальное время полета для каждого авиаперевозчика:
Перевозчик Aeroflot: 10 часов 15 минут (615 минут)
Перевозчик S7: 9 часов 45 минут (585 минут)
Перевозчик Ural Airlines: 11 часов 30 минут (690 минут)
Перевозчик Turkish Airlines: 8 часов 20 минут (500 минут)

Средняя цена: 25430.75 рублей
Медианная цена: 23150.00 рублей
Разница между средней и медианной ценой: 2280.75 рублей
```

## Решение проблем

1. **Файл не найден:**
   ```bash
   # Убедитесь, что tickets.json существует в текущей директории
   ls -la tickets.json
   ```

2. **Maven не установлен:**
   ```bash
   # Установка Maven на Ubuntu/Debian
   sudo apt-get install maven
   ```

3. **Отказано в доступе для run.sh:**
   ```bash
   chmod +x run.sh
   ```

4. **Java не установлена:**
   ```bash
   # Установка Java на Ubuntu/Debian
   sudo apt-get install openjdk-8-jdk
   ```

## Формат входных данных

Приложение ожидает JSON-файл `tickets.json` в формате:
```json
{
  "tickets": [
    {
      "origin": "VVO",
      "origin_name": "Владивосток",
      "destination": "TLV",
      "destination_name": "Тель-Авив",
      "departure_date": "12.05.18",
      "departure_time": "16:20",
      "arrival_date": "12.05.18",
      "arrival_time": "22:10",
      "carrier": "TK",
      "stops": 3,
      "price": 12400
    }
  ]
}
```

## Функциональность

Приложение анализирует билеты и предоставляет:
- Минимальное время полета для каждого авиаперевозчика между Владивостоком и Тель-Авивом
- Разницу между средней ценой и медианной ценой билетов

## Исходный код

Полный исходный код доступен по ссылке: [https://github.com/your-username/ticket-analyzer](https://github.com/your-username/ticket-analyzer)

## Вклад в проект

Если вы хотите внести вклад в приложение Tickets, выполните следующие шаги:

1. Сделайте форк репозитория
2. Создайте новую ветку для вашей функции или исправления ошибки
3. Внесите изменения и убедитесь, что приложение работает должным образом
4. Отправьте pull request с описанием ваших изменений

Ваш вклад будет рассмотрен, и после одобрения будет объединен с основным репозиторием.
