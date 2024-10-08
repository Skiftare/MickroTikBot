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

    # Увеличиваем время ожидания на 120 секунд, чтобы RouterOS успел полностью загрузиться
    sleep 120

    # Изменение учетных данных
    change_router_credentials
}

# Функция для изменения учетных данных RouterOS и работы с первым запуском
change_router_credentials() {
    echo "Первый запуск RouterOS. Принятие лицензионного соглашения и установка пароля..."

    # Используем expect для автоматического взаимодействия через SSH
    # Используем expect для автоматического взаимодействия через SSH
    /usr/bin/expect << EOF
    set timeout 20

    # Подключаемся к RouterOS по SSH (порт проброшен на 2222)
        spawn ssh -o StrictHostKeyChecking=no admin@chr_router -p 2222

    # Ожидание первого запуска и принятие лицензионного соглашения
        expect "Do you want to see the software license?"
        send "n\r"

    # После принятия лицензии RouterOS предложит установить новый пароль
        expect "new password>"
        send "$NEW_ROUTER_PASS\r"

        expect "repeat new password>"
        send "$NEW_ROUTER_PASS\r"

    # Логируем создание нового пользователя
        expect "] >"
        send "/user add name=$NEW_ROUTER_LOGIN password=$NEW_ROUTER_PASS group=full\r"

    # Опционально, удаляем стандартного пользователя admin для безопасности
        send "/user remove admin\r"
        expect "] >"

    # Завершаем сессию
        send "quit\r"
        expect eof
EOF

    echo "Учетные данные успешно изменены."
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
