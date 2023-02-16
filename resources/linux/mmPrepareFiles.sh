#!/bin/bash
#fis.groovy, mm_nix.groovy : prepare folders
mkdir {lib,bin}
cp -r ./git/config/${JOB_BASE_NAME}/tools .
chmod a+x ./tools/Make
if [ "${LABEL}" = "fis" ]; then
mkdir ./bin/fis.bin
fi
if [ "${JOB_BASE_NAME}" = "mm_nix" ]; then
mkdir units
cp -r ./MicroModule/* ./units
cp ./units/axcoder/axorlib.* ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_mm/sources/
cp ./units/microx_t/sources/SLogger.cpp ./units/microx_t/samples/ucs_ms/sources/
fi
