#!/bin/sh

cd build/osgi

JAVA="java"

# if JAVA_HOME exists, use it
if [ -x "$JAVA_HOME/bin/java" ]
then
  JAVA="$JAVA_HOME/bin/java"
else
  if [ -x "$JAVA_HOME/jre/bin/java" ]
  then
    JAVA="$JAVA_HOME/jre/bin/java"
  fi
fi

"$JAVA" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar org.apache.felix.main-5.4.0.jar
