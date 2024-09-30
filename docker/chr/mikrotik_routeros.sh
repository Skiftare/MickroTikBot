#!/bin/bash

# Проверяем и устанавливаем зависимости, если их нет
yum install -y qemu-kvm qemu-img wget unzip

# Константы
ROUTEROS_ISO_URL="https://download.mikrotik.com/routeros/7.10.2/chr-7.10.2.img.zip"
ROUTEROS_ZIP="/tmp/chr-routeros.zip"
ROUTEROS_IMG="/tmp/chr-routeros.img"
DISK_IMG="/opt/mikrotik-routeros-disk.qcow2"
DISK_SIZE="2G"
MEMORY="512M"
CPUS="1"

# 1. Скачивание образа MikroTik RouterOS
if [ ! -f "$ROUTEROS_IMG" ]; then
    wget -O "$ROUTEROS_ZIP" "$ROUTEROS_ISO_URL"
    # 2. Распаковка образа
    unzip "$ROUTEROS_ZIP" -d /tmp/
    # Удаление zip-файла после распаковки
    rm -f "$ROUTEROS_ZIP"
fi

# 3. Создание виртуального диска для RouterOS, если его нет
if [ ! -f "$DISK_IMG" ]; then
    qemu-img create -f qcow2 "$DISK_IMG" "$DISK_SIZE"
fi

# 4. Запуск виртуальной машины с MikroTik RouterOS
qemu-system-x86_64 -m "$MEMORY" -smp "$CPUS" -enable-kvm \
    -drive file="$DISK_IMG",format=qcow2 \
    -drive file="$ROUTEROS_IMG",format=raw \
    -netdev user,id=net0,hostfwd=tcp::2222-:22, hostfwd=tcp::8291-:8291 -device e1000,netdev=net0 \
    -nographic \
    -boot d

# 5. Возврат IP для SSH соединения
HOST_IP=$(hostname -I | awk '{print $1}')
echo "$HOST_IP" > /output/container_ip.txt