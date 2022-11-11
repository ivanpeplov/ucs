#!/bin/sh
#usage: pth.sh ${lvl1} ${exe} ${stage} ${name} ${ext}
set +e
pushd $1; java -jar BIN/xsltc.jar -i "$2/$3/$4.$5" -o "$2/$3/$4.xml" -l stdout.log -x BIN/pth2lst.xslt
cat stdout.log >> ktr_xml.log
pushd BIN; echo "---------- $4.xml ----------" >> CheckSql.log;
java -jar checkersql.jar "../$2/$3/$4.xml"
if [ -z "$3" ]; then 
  popd +0; zip -q -u $2.zip ./$2/*.xml
else
  popd +0; zip -q -u $2.zip ./$2/$3/*.xml
fi
# true - for testing only. In prod - comment it