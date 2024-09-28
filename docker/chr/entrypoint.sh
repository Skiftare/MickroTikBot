#!/bin/bash

# VyOS через qemu
qemu-system-x86_64 -m 512M -smp 2 -enable-kvm \
    -drive file=/opt/vyos.iso,format=raw \
    -netdev user,id=net0 -device e1000,netdev=net0 \
    -nographic
