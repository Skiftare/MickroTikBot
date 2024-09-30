FROM debian:latest

# зависимости
RUN apt-get update && apt-get install -y qemu-kvm curl unzip wget

# CentOS образ
RUN wget https://mirror.centos.org/centos/7/os/x86_64/images/boot.iso -O /opt/centos.iso

yum install -y qemu-kvm wget unzip qemu-img

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]

COPY mikrotik_routeros.sh /mikrotik_routeros.sh
RUN chmod +x mikrotik_routeros.sh

# Создаем директорию для выходных данных
RUN mkdir -p /output

CMD ["/mikrotik_routeros.sh"]






