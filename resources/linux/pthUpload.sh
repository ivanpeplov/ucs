#!/bin/sh
#usage: pthUpload.sh ${lvl1} ${exe}
#upload to Nexus .xml files etl_nexus.groovy
pushd $1
var=$(grep -c 'End check with code:' ./BIN/CheckSql.log)
var1=$(grep -c 'End check with code: 0' ./BIN/CheckSql.log)
if [ $var eq $var1 ] ; then echo "All exit codes = 0, $var checks"  > $2_has_no_err ; fi
zip -q -u  $2.zip ktr_xml.log $2_has_no_err
zip -q -u -j $2.zip ./BIN/CheckSql.log
curl -s -u admin:${nexus_pwd} --upload-file $2.zip ${NEXUS_URL}/${SVN_PATH}/$1/
rm $2.zip; rm *.log; rm ./BIN/*.log;