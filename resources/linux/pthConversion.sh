#!/bin/bash
#usage: pthConversion.sh ${lvl1} ${exe} ${name} ${ext} ${stage}
set +e
#check pos. parameters number "$#"
if [ "$#" -eq 4 ]; then
pushd $1; java -jar BIN/xsltc.jar -i "$2/$3.$4" -o "$2/$3.xml" -l stdout.log -x BIN/pth2lst.xslt
else
pushd $1; java -jar BIN/xsltc.jar -i "$2/$5/$3.$4" -o "$2/$5/$3.xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
pushd BIN; echo "---------- $3.xml ----------" >> CheckSql.log;
if [  "$#" -eq 4 ]; then
  java -jar checkersql.jar "../$2/$3.xml"
  popd +0; zip  -u $2.zip ./$2/$3.xml
else
  java -jar checkersql.jar "../$2/$5/$3.xml"
  popd +0; zip  -u $2.zip ./$2/$5/$3.xml
fi
# true - for testing only. In prod - comment it