#!/bin/bash
# fm.groovy
zip  ${LABEL}.zip [f-F]mUX.* -x fmUpload.sh
curl -u jenkucs_sa:${nexus_pwd} --upload-file ${LABEL}.zip  ${NEXUS_URL_1}/FM/