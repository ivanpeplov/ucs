#!/bin/bash
#usage: scriptsUpload2Nexus.sh ${lvl1} ${lvl2} ${lvl3}
pushd $1/$2/$3
if [ -z "$(ls -A )" ]; then echo "Empty"
else
zip -r $3.zip *
curl -u jenkucs_sa:${nexus_pwd} --upload-file $3.zip ${NEXUS_URL}/${SVN_PATH}/$1/$2/
fi