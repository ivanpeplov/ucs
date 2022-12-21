#!/bin/bash
#                          $1      $2     $3      $4   
#usage: ConversionPTH.sh ${exe} ${name} ${ext} ${stage}
set +e
if [ "$#" -eq 3 ]; then
java -jar BIN/xsltc.jar -i "$1/$2.$3" -o "$1/$2.xml" -l stdout.log -x BIN/pth2lst.xslt
else
java -jar BIN/xsltc.jar -i "$1/$4/$2.$3" -o "$1/$4/$2.xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
pushd BIN; echo "---------- $2.xml ----------" >> CheckSql.log;
if [  "$#" -eq 3 ]; then
  java -jar checkersql.jar "../$1/$2.xml"
  popd +0; zip  -u $1.zip ./$1/$2.xml
else
  java -jar checkersql.jar "../$1/$4/$2.xml"
  popd +0; zip  -u $1.zip ./$1/$4/$2.xml
fi
# true - for testing only. In prod - comment it