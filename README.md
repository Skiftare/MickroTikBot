# MicroTik Admin Bot

Этот репозиторий содержит тестовый проект для удобного администрирования роутера **MicroTik** через телеграм-бота. Проект создан для изучения принципов объектно-ориентированного программирования (ООП).

## О проекте

Бот предназначен для упрощения администрирования роутера **MicroTik** через удобный интерфейс в Telegram. Проект был разработан с целью:

- Изучения и применения принципов ООП на практике.
- Упрощения задач по управлению и мониторингу сети.
- Создания телеграм-бота, взаимодействующего с роутером MicroTik.

## Основные функции

- Подключение и управление роутером через Telegram.
- Выполнение сетевых команд.
- Получение актуальной информации о состоянии сети.

## Стек технологий

- **Язык программирования:** Java
- **Платформа для разработки:** IntelliJ IDEA
- **API для взаимодействия с Telegram:** Telegram Bot API
- **Работа с роутером:** MicroTik API

## Разработчики

Проект выполняют:

- **Бередичевский Артём**
- **Братанов Кирилл**

## Цели

- Улучшить навыки работы с ООП.
- Освоить взаимодействие с внешними API.
- Оптимизировать сетевое администрирование через бота.

## Rodmap

- [x] дз1: базовый бот с 3-мя командами
- [x] дз2: добавление БД, регситрации пользователя.
- [x] дз3: dataflow между БД и роутером
- [x] дз4: MVP
- [x] дз5: добавление оплаты в криптовалюте
- [x] дз6: создание сущности "профиля" в боте (т.е. у пользователя будет ещё и личный кабинет, он когда-то пополняет наш кошелек, и мы это помним. Запоминаем также его креды для подключения), обновление БД под оплату.
- [x] дз7: рефакторинг, добавление транзакционной оплаты
- [x] дз8: добавление картинок, тестов и рефакторинг
- [ ] дз9: gRPC

## Установка

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/Skiftare/MickroTikBot.git
    ```
   2. Создайте .env файл в корне проекта и добавьте в него переменные окружения:
      ```bash
      TELEGRAM_BOT_TOKEN=your_telegram_bot_token
      NEW_ROUTER_LOGIN=your_chr_login
      NEW_ROUTER_PASS=your_chr_password
      CHR_ROUTER_IP=your_chr_IP_address
      STELLAR_PUBLIC_KEY=your_public_key_for_payment_proceeding
      STELLAR_SECRET_KEY=your_secret_key_for_payment_proceeding
      SERVER_IP=your_radius_server_public_IP
      STELLAR_NETWORK=(testnet/real)
      NEW_DATABASE_LOGIN=your_database_login
      NEW_DATABASE_PASS=your_database_password
      KEY_FOR_AES=your_AES_key
      TELEGRAM_CHANNEL_ID=your_tg_channel_ID_for_trial
      DB_ENCRYPTION_KEY=your_encryption_key
      ROUTER_BEHAVIOUR=program_launch_option(test/production)
      ```
