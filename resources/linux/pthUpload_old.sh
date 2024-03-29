#!/bin/bash
#                      $1      $2   
#usage: pthUpload.sh ${lvl1} ${exe}
pushd $1
#check "exit codes 0" in CheckSql.log | alarm file ${exe}_has_no_err
var=$(grep -c 'End check with code:' ./BIN/CheckSql.log)
var1=$(grep -c 'End check with code: 0' ./BIN/CheckSql.log)
if [ $var -eq $var1 ] ; then
echo "All exit codes = 0, $var checks in CheckSql.log" > $2_has_no_err ;
fi
if [ -f "$2_has_no_err" ]; then
zip -u  $2.zip ktr_xml.log $2_has_no_err
else
zip -u  $2.zip ktr_xml.log
fi
zip -u -j $2.zip ./BIN/CheckSql.log
curl -s -u jenkucs_sa:${nexus_pwd} --upload-file $2.zip ${NEXUS_URL}/${SVN_PATH}/$1/
rm $2.zip; rm *.log; rm ./BIN/*.log;