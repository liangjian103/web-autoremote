#!/bin/bash

LANG="zh_CN.UTF-8"
export LANG

pid=`ps -ef|grep "web-autoremote"|grep -v grep|grep -v PPID|grep -v tail|awk '{print $2}'`
echo "pid:$pid"

kill -9 $pid
echo "kill -9 $pid"

echo "nohup bash start.sh $1"
nohup bash start.sh $1 &

echo "rebootServer is OK!"
