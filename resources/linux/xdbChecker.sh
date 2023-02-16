#!/bin/bash
#pthConversion.groovy
#                       $1     $2        $3       $4   
#usage: xdbChecker.sh ${l2} ${name} ${extension} ${l3}
set +e
cd BIN; echo "---------- $2.xml ----------" >> CheckSql.log;
if [  "$#" -eq 3 ]; then
  java -jar checkersql.jar "../$1/$2.xml"
  cd ..; zip  -u $1.zip ./$1/$2.xml
else
  java -jar checkersql.jar "../$1/$4/$2.xml"
  cd ..; zip  -u $1.zip ./$1/$4/$2.xml
fi
# true - for testing only. In prod - comment it