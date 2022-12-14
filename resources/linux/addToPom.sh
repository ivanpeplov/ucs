#!/bin/bash
#delete last line
sed -i '$d' pom.xml
#attached after last line
cat addToPom.xml >> pom.xml