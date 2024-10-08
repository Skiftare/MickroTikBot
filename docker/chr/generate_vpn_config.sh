#!/bin/bash

# Генерация нового UUID для пользователя
UUID=$(uuidgen)

# Создание конфигурационного файла для VLESS
cat <<EOF > /etc/xray/config/vless_${UUID}.json
{
  "v": "2",
  "ps": "NewUser",
  "add": "vpn.example.com",
  "port": "443",
  "id": "${UUID}",
  "aid": "0",
  "net": "tcp",
  "type": "none",
  "host": "",
  "path": "/",
  "tls": "tls"
}
EOF

echo "VLESS profile created for user with UUID: ${UUID}"
