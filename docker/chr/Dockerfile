FROM debian:latest

# зависимости
RUN apt-get update && apt-get install -y qemu-kvm curl unzip wget

# VyOS образ, потому что хз, я не хочу тут ещё прописывать какой-то кряк MickroTik лицензии
RUN wget https://downloads.vyos.io/release/rolling/current/amd64/vyos-rolling-latest.iso -O /opt/vyos.iso


COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]

