#!/bin/bash

LANG="zh_CN.UTF-8"
export LANG

serverPath=/home/yanfa_ro

cd $serverPath
rm -rf $serverPath/web-autoremote.tar.gz
cp $serverPath/web-autoremote-up/web-autoremote.tar.gz $serverPath/
rm -rf $serverPath/web-autoremote
tar -zxvf $serverPath/web-autoremote-up/web-autoremote.tar.gz -C $serverPath/
cd $serverPath/web-autoremote

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v tail|grep -v "web-autoremote-setup.sh"|awk '{print $2}'`
echo "pid:$pid"

if [ $pid != '' ]
then
    kill -9 $pid
    echo "kill -9 $pid"
fi

bash start.sh $1

echo "sleep 5s ..."
sleep 5s

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v tail|grep -v "web-autoremote-setup.sh"|awk '{print $2}'`
echo "rebootServer is OK! pid:$pid"

echo "web-autoremote-setup is OK!"
