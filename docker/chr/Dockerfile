FROM debian:latest

# Установка зависимостей
RUN apt-get update && apt-get install -y \
    qemu-kvm \
    qemu-utils \
    wget \
    unzip \
    iproute2 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Создание рабочих директорий
RUN mkdir -p /opt/routeros /output


# Копирование скриптов
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Установка рабочей директории
WORKDIR /opt/routeros

# Запуск entrypoint скрипта
CMD ["/entrypoint.sh"]





