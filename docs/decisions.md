

Sprint 2

Сделаем красивую обработку ошибок.

Например, если кто-нибудь удалит local.properties, пользователь увидит:

 local.properties not found.

Create the file and specify:

BOT_TOKEN=
RUSSIAN_POST_LOGIN=
RUSSIAN_POST_PASSWORD=

Поскольку сначала бот будет жить на твоём домашнем ПК, можно сделать небольшую утилиту-обёртку.

Например:

TrackBot
│
├── StartBot.bat
├── StopBot.bat
└── TrackBot.jar
