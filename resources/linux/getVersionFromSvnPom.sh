#!/bin/sh
cat pom.xml | grep -m 1 \<version\> | sed -e 's/<version>\(.*\)<\/version>/\1/'