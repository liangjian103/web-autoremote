#!/bin/bash

LANG="zh_CN.UTF-8"
export LANG

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v ' tail '|grep -v "rebootServer.sh"|awk '{print $2}'`
echo "pid:$pid"

if [ $pid != '' ]
then
    kill -9 $pid
    echo "kill -9 $pid"
fi

echo "bash start.sh $1"
bash start.sh $1

echo "rebootServer is OK!"
