FROM debian:latest

# зависимости
RUN apt-get update && apt-get install -y qemu-kvm curl unzip wget

# CentOS образ
RUN wget https://mirrors.centos.org/mirrorlist?path=/9-stream/BaseOS/x86_64/iso/CentOS-Stream-9-latest-x86_64-dvd1.iso&redirect=1&protocol=https

RUN apt-get update && apt-get install -y qemu-kvm wget unzip qemu-utils

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]

COPY mikrotik_routeros.sh /mikrotik_routeros.sh
RUN chmod +x mikrotik_routeros.sh

# Создаем директорию для выходных данных
RUN mkdir -p /output

CMD ["/mikrotik_routeros.sh"]





