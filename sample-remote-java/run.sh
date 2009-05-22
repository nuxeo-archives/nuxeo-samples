#!/bin/sh

MAIN_JAR=target/*.jar
DEP_JARS=target/classes/lib/*.jar

CP=.
for I in $DEP_JARS
    do
        CP=$CP:$I
    done

# XXX: maybe skip sources jar
for I in $MAIN_JAR
    do
        CP=$CP:$I
    done

#OPTS=" -Xdebug -Xrunjdwp:transport=dt_socket,address=127.0.0.1:8788,server=y,suspend=y "
#OPTS=$OPTS" -Dlog4j.debug -Dlog4j.configuration=log4j.properties "

java $OPTS -cp $CP org.nuxeo.ecm.core.client.Launcher