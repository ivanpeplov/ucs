#!/bin/bash
zip -r -q $1.zip * -x evotorUpload.sh
curl  -s -u jenkucs_sa:${nexus_pwd} --upload-file $1.zip  ${NEXUS_URL}/MicroModule/Android/               