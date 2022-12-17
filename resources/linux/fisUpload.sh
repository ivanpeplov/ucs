#!/bin/bash
#                             $1          $2        $3          $4   
#usage: ./uploadFIS.sh ${JOB_BASE_NAME} ${SVN} ${NODE_NAME} ${VERSION}
#usage: ./uploadFIS.sh ${JOB_BASE_NAME} ${ARCH} ${NODE_NAME}
#check pos. parameters number "$#"
if [ "$#" -eq 3 ]; then
zip -r -q $1_$2.zip * -x fisUpload.sh
curl  -s -u jenkucs_sa:${nexus_pwd} --upload-file $1_$2.zip  ${NEXUS_URL}/${TOOR}/$3/               
else
zip -r -q $1_$2_$4.zip * -x fisUpload.sh
curl  -s -u jenkucs_sa:${nexus_pwd} --upload-file $1_$2_$4.zip  ${NEXUS_URL}/${TOOR}/$3/               
fi