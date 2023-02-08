#!/bin/bash
#fis.groovy, mm_nix.groovy : prepare folders
mkdir {lib,bin}
cp -r ./git/config/${JOB_BASE_NAME}/tools .
chmod a+x ./tools/Make
if [ "${LABEL}" = "fis" ]; then
cd ./bin
mkdir fis.bin
fi
if [ "${JOB_BASE_NAME}" = "mm_nix" ]; then
mkdir units
cp -r ./MicroModule/{axcoder,cyassl-3.2.0,microx_t,myizip_z}/ ./units
cp ./units/axcoder/axorlib.* ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_ms/sources/
fi
