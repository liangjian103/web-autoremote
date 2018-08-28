#!/bin/bash

LANG="zh_CN.UTF-8"
export LANG
cd /home/yanfa_ro
rm -rf /home/yanfa_ro/web-autoremote.tar.gz
cp /home/yanfa_ro/web-autoRemote-up/web-autoremote.tar.gz /home/yanfa_ro/
rm -rf /home/yanfa_ro/web-autoremote
tar -zxvf web-autoremote.tar.gz
cd /home/yanfa_ro/web-autoremote
echo "nohup bash rebootServer.sh $1 &"
nohup bash rebootServer.sh $1 &

echo "setup is OK!"
