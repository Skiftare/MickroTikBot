FROM debian:latest

# Установка зависимостей
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    apt-utils \
    qemu-kvm \
    qemu-utils \
    wget \
    unzip \
    iproute2 \
    curl \
    expect \
    openssh-client \
    ca-certificates \
    gnupg && \
    rm -rf /var/lib/apt/lists/*

# Создание рабочих директорий
RUN mkdir -p /opt/routeros /output

# Скачивание CentOS образа
#RUN wget -O /opt/centos.iso "https://mirrors.centos.org/mirrorlist?path=/9-stream/BaseOS/x86_64/iso/CentOS-Stream-9-latest-x86_64-dvd1.iso&redirect=1&protocol=https"

# Копирование скриптов
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Установка рабочей директории
WORKDIR /opt/routeros

# Запуск entrypoint скрипта
CMD ["/entrypoint.sh"]
