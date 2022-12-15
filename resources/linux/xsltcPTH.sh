#!/bin/bash
#                      $1      $2     $3     $4      $5
#usage: xsltcPTH.sh ${lvl1} ${exe} ${name} ${ext} ${stage}
#check pos. parameters number "$#"
if [ "$#" -eq 4 ]; then
pushd $1; java -jar BIN/xsltc.jar -i "$2/$3.$4" -o "$2/$3.xml" -l stdout.log -x BIN/pth2lst.xslt
else
pushd $1; java -jar BIN/xsltc.jar -i "$2/$5/$3.$4" -o "$2/$5/$3.xml" -l stdout.log -x BIN/pth2lst.xslt
fi
cat stdout.log >> ktr_xml.log
# true - for testing only. In prod - comment it