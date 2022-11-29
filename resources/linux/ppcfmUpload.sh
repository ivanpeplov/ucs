#!/bin/bash
zip -q ${ARCH}_${BUILD_NUMBER}.zip [f-F]mUX.*
curl -s -u jenkucs_sa:${nexus_pwd} --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/FM/