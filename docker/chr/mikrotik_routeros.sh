#!/bin/bash

# Обновление списка пакетов и установка зависимостей
apt update -y
apt install -y qemu-kvm qemu-utils wget unzip

# Создание рабочей директории
mkdir -p /opt/routeros
cd /opt/routeros

# Скачиваем образ RouterOS (укажите актуальную версию)
wget https://download.mikrotik.com/routeros/6.49.6/chr-6.49.6.img.zip

# Распаковываем образ
unzip chr-6.49.6.img.zip

# Запуск RouterOS с помощью QEMU
qemu-system-x86_64 -hda chr-6.49.6.img -boot d -m 256M -nographic \
  -device e1000,netdev=net0 -netdev user,id=net0,hostfwd=tcp::2222-:22

# Ожидание завершения работы
echo "RouterOS запущен. Подключитесь через SSH на порт 2222"
