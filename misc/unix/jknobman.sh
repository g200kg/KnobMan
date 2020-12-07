#!/bin/sh
set -e
bindir=`dirname "$0"`
sharedir="$bindir"/../share
exec java -jar "$sharedir"/JKnobMan/JKnobMan.jar "$@"
