#!/bin/bash

# Функция для запуска RouterOS
start_routeros() {
    echo "Запуск RouterOS..."
    
    # Скачиваем образ RouterOS
    wget https://download.mikrotik.com/routeros/6.49.6/chr-6.49.6.img.zip
    unzip chr-6.49.6.img.zip

    # Запуск RouterOS с помощью QEMU
    qemu-system-x86_64 -drive file=chr-6.49.6.img,format=raw -boot d -m 256M -nographic \
      -device e1000,netdev=net0 -netdev user,id=net0,hostfwd=tcp::2222-:22 &

    # Ждем, пока RouterOS полностью загрузится
    sleep 60

    # Изменение учетных данных
    change_router_credentials
}

# Функция для изменения учетных данных RouterOS
change_router_credentials() {
    echo "Изменение учетных данных RouterOS..."
    
    # Используем утилиту RouterOS CLI для изменения учетных данных
    # Предполагается, что по умолчанию логин 'admin' без пароля
    ssh -p 2222 admin@localhost "/user set admin password=$NEW_ROUTER_PASS;" 

    echo "Учетные данные RouterOS успешно изменены."
}

# Функция для запуска CentOS (оставлена без изменений)
start_centos() {
    echo "Запуск CentOS..."
    
    qemu-system-x86_64 -m 1024M -smp 2 -enable-kvm \
        -cdrom /opt/centos.iso \
        -drive file=/opt/centos-disk.img,format=qcow2,size=10G \
        -netdev user,id=net0 -device e1000,netdev=net0 \
        -boot d \
        -nographic
}

# Основной код
echo "Начало работы скрипта..."

# Проверяем наличие переменных окружения
if [ -z "$NEW_ROUTER_LOGIN" ] || [ -z "$NEW_ROUTER_PASS" ]; then
    echo "Ошибка: NEW_ROUTER_LOGIN или NEW_ROUTER_PASS не установлены"
    exit 1
fi

# Запускаем RouterOS
start_routeros

# Если RouterOS не удалось запустить, переключаемся на CentOS
if [ $? -ne 0 ]; then
    echo "RouterOS не удалось запустить. Переключение на CentOS..."
    start_centos
fi

echo "Скрипт завершен."

# Держим контейнер запущенным
tail -f /dev/null
