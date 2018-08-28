#!/bin/bash

LANG="zh_CN.UTF-8"
#JAVA_HOME="/opt/web_app/jdk"
#JAVA_HOME="/opt/web_app/jdk1.7.0_79"
JAVA_HOME="/opt/web_app/jdk1.8.0_74"

serverPath=`pwd`
serverPort=`cat $serverPath/application-$1.properties|grep "server.port"`

export LANG

if [ $# -lt 1 ] ; then
echo "please add param profile"
echo "profile: [pro|test]"
echo "example:"
echo "sh runWebToolMain.sh test"
echo "sh runWebToolMain.sh pro"
exit 0
fi

$JAVA_HOME/bin/java -classpath .:conf/*:lib/* com.lj.main.WebAutoRemoteMain --spring.profiles.active=$1 $serverPath [$serverPort]
#java -classpath lib\* com.lj.main.WebAutoRemoteMain

echo "Execute-OK!-> com.lj.main.WebAutoRemoteMain $1"
