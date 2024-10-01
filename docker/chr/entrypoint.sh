#!/bin/bash

# Функция для запуска RouterOS
start_routeros() {
    echo "Запуск RouterOS..."
    
    # Скачиваем образ RouterOS
    wget https://download.mikrotik.com/routeros/6.49.6/chr-6.49.6.img.zip
    unzip chr-6.49.6.img.zip

    # Запуск RouterOS с помощью QEMU
    qemu-system-x86_64 -drive file=chr-6.49.6.img,format=raw -boot d -m 256M -nographic \
      -device e1000,netdev=net0 -netdev user,id=net0,hostfwd=tcp::2222-:22
}

# Функция для запуска CentOS
start_centos() {
    echo "Запуск CentOS..."
    
    # CentOS через qemu
    qemu-system-x86_64 -m 1024M -smp 2 -enable-kvm \
        -cdrom /opt/centos.iso \
        -drive file=/opt/centos-disk.img,format=qcow2,size=10G \
        -netdev user,id=net0 -device e1000,netdev=net0 \
        -boot d \
        -nographic
}

# Основной код
echo "Начало работы скрипта..."

# Сначала пытаемся запустить RouterOS
start_routeros

# Если RouterOS завершился с ошибкой, запускаем CentOS
if [ $? -ne 0 ]; then
    echo "RouterOS не удалось запустить. Переключение на CentOS..."
    start_centos
fi

echo "Скрипт завершен."
