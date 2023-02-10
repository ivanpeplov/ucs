#!/bin/bash
#chk_sql.groovy
#                         $1     $2     $3     $4   
#usage: pthChecker.sh   ${l2} ${name} ${ext} ${l3}
set +e
if [ "$#" -eq 3 ]; then
java -jar BIN/xsltc.jar -i "$1/$2.$3" -o "$1/$2.$3.xml" -l stdout.log -x BIN/pth2lst.xslt
else
java -jar BIN/xsltc.jar -i "$1/$4/$2.$3" -o "$1/$4/$2.$3.xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
pushd BIN; echo "---------- $2.$3.xml ----------" >> CheckSql.log;
if [  "$#" -eq 3 ]; then
  java -jar checkersql.jar "../$1/$2.$3.xml"
  popd +0; zip  -u $1.zip ./$1/$2.$3.xml
else
  java -jar checkersql.jar "../$1/$4/$2.$3.xml"
  popd +0; zip  -u $1.zip ./$1/$4/$2.$3.xml
fi
# true - for testing only. In prod - comment it