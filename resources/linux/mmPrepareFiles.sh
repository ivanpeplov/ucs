#!/bin/bash
#prepare folders for fis.groovy, mm_nix.groovy
mkdir {lib,bin}
cp -r ./git/config/${JOB_BASE_NAME}/tools .
chmod a+x ./tools/Make
if [ "${JOB_BASE_NAME}" = "fis" ]; then
cd ./bin
mkdir fis.bin
fi
if [ "${JOB_BASE_NAME}" = "mm_nix" ]; then
mkdir units
cp -R ./MicroModule/{axcoder,cyassl-3.2.0,microx_t,myizip_z}/ ./units
cp ./units/axcoder/axorlib.* ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_ms/sources/
fi
