#!/bin/sh

DIRBIN=bin

if [ ! -z "$1" ] ; then
	if [ $1 = "clean" ]; then
		echo "Cleaning dir directory"
		/bin/rm -R $DIRBIN
	fi
fi

if [ ! -d "$DIRBIN" ]; then
	/bin/mkdir bin
fi

/usr/bin/javac -d $DIRBIN -cp .:lib/* ProviderServiceTest.java
/usr/bin/java -cp .:$DIRBIN:lib/* ProviderServiceTest
