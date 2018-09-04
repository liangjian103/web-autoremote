#!/bin/bash

LANG="zh_CN.UTF-8"
#JAVA_HOME="/opt/web_app/jdk"
#JAVA_HOME="/opt/web_app/jdk1.7.0_79"
#JAVA_HOME="/opt/web_app/jdk1.8.0_74"
JAVA_HOME="/home/yanfa_ro/jre1.8.0_74"

serverPath=`pwd`
serverPort=`cat $serverPath/application-$1.properties|grep "server.port"`

export LANG

if [ $# -lt 1 ] ; then
echo "please add param profile"
echo "profile: [pro|test]"
echo "example:"
echo "sh start.sh test"
echo "sh start.sh pro"
exit 0
fi

nohup $JAVA_HOME/bin/java -classpath .:conf/*:lib/* com.lj.main.WebAutoRemoteMain --spring.profiles.active=$1 $serverPath [$serverPort] > $serverPath/../web-autoremote-start.log 2>&1 &

echo "Execute-OK!-> com.lj.main.WebAutoRemoteMain $1"
