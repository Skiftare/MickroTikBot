FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    curl \
    sudo \
    && rm -rf /var/lib/apt/lists/*

RUN curl -sL https://github.com/Gozargah/Marzban-scripts/raw/master/marzban.sh | sudo bash

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]
