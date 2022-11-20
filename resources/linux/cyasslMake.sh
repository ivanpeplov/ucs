#!/bin/sh
#usage: cyasslMake.sh $1| library build for micro_mod.groovt
pushd $1
chmod 750 configure
./configure --enable-opensslextra --enable-aesgcm --enable-sha512 --enable-ripemd --enable-ecc --enable-static
make 2>errs
cp ./src/.libs/libcyassl.a ${PROJECTS}/lib