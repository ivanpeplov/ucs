#!/bin/sh
#usage: pthUpload.sh ${lvl1} ${exe}
pushd $1; zip -q -u  $2.zip ktr_xml.log ; zip -q -u -j $2.zip ./BIN/CheckSql.log;
curl -s -u admin:${nexus_pwd} --upload-file $2.zip ${NEXUS_URL}/${SVN_PATH}/$1/; 
rm $2.zip; rm *.log; rm ./BIN/*.log
    