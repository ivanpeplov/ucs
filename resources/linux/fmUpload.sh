#!/bin/bash
# fm build
zip  ${ARCH}.zip [f-F]mUX.* -x ppcfmUpload.sh
curl -u jenkucs_sa:${nexus_pwd} --upload-file ${ARCH}.zip  ${NEXUS_URL_1}/FM/