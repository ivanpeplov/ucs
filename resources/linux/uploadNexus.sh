#!/bin/sh
#usage: uploadNexus.sh ${lvl1} ${lvl2} ${lvl3}
pushd $1/$2/$3
if [ -z "$(ls -A )" ]; then echo "Empty"
else
zip -r -q $3.zip *
curl -s -u admin:${nexus_pwd} --upload-file $3.zip ${NEXUS_URL}/${SVN_PATH}/$1/$2/
fi