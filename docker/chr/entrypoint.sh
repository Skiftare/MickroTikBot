#!/bin/bash

# CentOS через qemu
qemu-system-x86_64 -m 1024M -smp 2 -enable-kvm \
    -cdrom /opt/centos.iso \
    -drive file=/opt/centos-disk.img,format=qcow2,size=10G \
    -netdev user,id=net0 -device e1000,netdev=net0 \
    -boot d \
    -nographic
