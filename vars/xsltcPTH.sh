#!/bin/bash
#                      $1      $2     $3     $4   
#usage: xsltcPTH.sh ${exe} ${name} ${ext} ${stage}
if [ "$#" -eq 3 ]; then
java -jar BIN/xsltc.jar -i "$1/$2.$3" -o "$1/$2.xml" -l stdout.log -x BIN/pth2lst.xslt
else
java -jar BIN/xsltc.jar -i "$1/$4/$2.$3" -o "$1/$4/$2.xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
# true - for testing only. In prod - comment it