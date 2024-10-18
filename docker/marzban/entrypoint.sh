#!/bin/bash

# Определение внешнего IP
export EXTERNAL_IP=$(curl -s https://api.ipify.org)

echo "Внешний IP: $EXTERNAL_IP"

# Настройка Marzban
marzban config set XRAY_SUBSCRIPTION_URL_PREFIX="vless://${EXTERNAL_IP}:8443"

# Запуск Marzban
marzban start

# Держим контейнер запущенным
tail -f /dev/null
