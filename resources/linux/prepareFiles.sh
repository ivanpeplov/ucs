#!/bin/sh
#prepare folders for fis/fis_util/micro_mod.groovy
mkdir {lib,bin}
cp -r ${WORKSPACE}/git/config/${JOB_BASE_NAME}/tools ${WORKSPACE}/
chmod a+x ${WORKSPACE}/tools/Make
if [ "${JOB_BASE_NAME}" = "fis" ]; then
cd ./bin
mkdir fis.bin
fi
if [ "${JOB_BASE_NAME}" = "mm_nix" ]; then
mkdir units
cp -R ${WORKSPACE}/MicroModule/{axcoder,cyassl-3.2.0,microx_t,myizip_z}/ ${PROJECTS}/units
cp ${PROJECTS}/units/axcoder/axorlib.* ${PROJECTS}/units/microx_t/samples/ucs_mm/sources/
cp ${PROJECTS}/units/microx_t/sources/SLogger.cpp ${PROJECTS}/units/microx_t/samples/ucs_mm/sources/
cp ${PROJECTS}/units/microx_t/sources/SLogger.cpp ${PROJECTS}/units/microx_t/samples/ucs_ms/sources/
mv ${PROJECTS}/units/cyassl-3.2.0 ${PROJECTS}/units/cyassl
fi
