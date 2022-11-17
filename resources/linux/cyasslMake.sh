#!/bin/sh
#usage: cyasslMake.sh | library build for micro_mod.groovt
chmod 750 configure
./configure --enable-opensslextra --enable-aesgcm --enable-sha512 --enable-ripemd --enable-ecc --enable-static
make 2>errs
cp ./src/.libs/libcyassl.a ${PROJECTS}/lib