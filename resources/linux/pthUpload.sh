#!/bin/bash
#chk_sql.groovy
#usage: pthUpload.sh ${itLvl2}
exitCode=$(grep -c 'End check with code:' ./BIN/CheckSql.log)
exitCode1=$(grep -c 'End check with code: 0' ./BIN/CheckSql.log)
if [ $exitCode -eq $exitCode1 ] ; then
echo "All exit codes = 0, $exitCode1 checks in CheckSql.log" > $1_has_no_err ;
fi
if [ -f "$1_has_no_err" ]; then
zip -u  $1.zip $1_has_no_err
fi
if [ -f "ktr_xml.log" ]; then
zip -u  $1.zip ktr_xml.log
fi
zip -u -j $1.zip ./BIN/CheckSql.log
curl -s -u jenkucs_sa:${nexus_pwd} --upload-file $1.zip ${NEXUS_URL}/${SVN_PATH}/
rm $1.zip; rm ./BIN/*.log;
if [ -f "ktr_xml.log" ]; then
rm ktr_xml.log
fi
