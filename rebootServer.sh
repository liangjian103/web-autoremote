#!/bin/bash

LANG="zh_CN.UTF-8"
export LANG

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v ' tail '|grep -v "rebootServer.sh"|awk '{print $2}'`
echo "pid:$pid"

kill -9 $pid
echo "kill -9 $pid"

echo "sleep 3s..."
sleep 3s

echo "bash start.sh $1"
bash start.sh $1

echo "sleep 1s ..."
sleep 1s

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v tail|grep -v "web-autoremote-setup.sh"|awk '{print $2}'`
echo "rebootServer is OK! pid:$pid"

echo "rebootServer is OK!"
