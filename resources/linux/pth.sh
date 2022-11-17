#!/bin/sh
#usage: pth.sh ${lvl1} ${exe} ${stage} ${name} ${ext}
set +e
if [ "$3" == "" ]; then
pushd $1; java -jar BIN/xsltc.jar -i "$2/$4.$5" -o "$2/$4.xml" -l stdout.log -x BIN/pth2lst.xslt
else
pushd $1; java -jar BIN/xsltc.jar -i "$2/$3/"$4".$5" -o "$2/$3/"$4".xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
pushd BIN; echo "---------- "$4".xml ----------" >> CheckSql.log;
if [  "$3" == "" ]; then
  java -jar checkersql.jar "../$2/"$4".xml"
  popd +0; zip -q -u $2.zip ./$2/"$4".xml
else
  java -jar checkersql.jar "../$2/$3/"$4".xml"
  popd +0; zip -q -u $2.zip ./$2/$3/"$4".xml
fi
# true - for testing only. In prod - comment it