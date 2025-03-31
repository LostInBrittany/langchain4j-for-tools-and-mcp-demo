#!/bin/bash


# Log start
echo "Starting wrapper script" > /tmp/jbang-debug.log

source /Users/horacio/.zshenv
source /Users/horacio/.zprofile

echo "JAVA_HOME=$JAVA_HOME" > /tmp/jbang-debug.log
echo "PATH=$PATH" >> /tmp/jbang-debug.log
/Users/horacio/.sdkman/candidates/jbang/current/bin/jbang "$@" 2>> /tmp/jbang-debug.log
