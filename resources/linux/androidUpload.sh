#!/bin/bash
# mm_android.groovy
zip -r -q $1.zip * -x androidUpload.sh
curl  -s -u jenkucs_sa:${nexus_pwd} --upload-file $1.zip  ${NEXUS_URL}/MicroModule/Android/               
