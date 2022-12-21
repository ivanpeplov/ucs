#!/bin/bash
#usage: pthUpload.sh ${exe}
#check "exit codes 0" in CheckSql.log | alarm file ${exe}_has_no_err
var=$(grep -c 'End check with code:' ./BIN/CheckSql.log)
var1=$(grep -c 'End check with code: 0' ./BIN/CheckSql.log)
#count=%((var - var1))
if [ $var -eq $var1 ] ; then
echo "All exit codes = 0, $var checks in CheckSql.log" > $1_has_no_err ;
fi
#check file $2_has_no_err exists
if [ -f "$1_has_no_err" ]; then
zip -u  $1.zip ktr_xml.log $1_has_no_err
else
zip -u  $1.zip ktr_xml.log
fi
zip -u -j $1.zip ./BIN/CheckSql.log
curl -s -u jenkucs_sa:${nexus_pwd} --upload-file $1.zip ${NEXUS_URL}/${SVN_PATH}/
rm $1.zip; rm *.log; rm ./BIN/*.log;