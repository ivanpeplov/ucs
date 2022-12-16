#!/bin/bash
#                       $1      $2      $3     $4      $5
#usage: checkerSQL.sh ${lvl1} ${exe} ${name} ${ext} ${stage}
set +e
pushd ./$1/BIN; echo "---------- $3.xml ----------" >> CheckSql.log;
if [  "$#" -eq 4 ]; then
  java -jar checkersql.jar "../$2/$3.xml"
  cd ..; zip  -u $2.zip ./$2/$3.xml
else
  java -jar checkersql.jar "../$2/$5/$3.xml"
  cd ..; zip  -u $2.zip ./$2/$5/$3.xml
fi
# true - for testing only. In prod - comment it