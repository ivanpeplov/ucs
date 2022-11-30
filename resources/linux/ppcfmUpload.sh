#!/bin/bash
zip  ${ARCH}_${BUILD_NUMBER}.zip [f-F]mUX.* -x ppcfmUpload.sh
curl -u jenkucs_sa:${nexus_pwd} --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/FM/