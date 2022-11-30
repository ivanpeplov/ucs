#!/bin/bash
#usage: "./mmCpp.sh ${sample[i]} | ./mmCpp.sh ${sample[i]} ${arch}") | CCADDFLAGS addin for X64 micro_mod.groovy
#pushd $1
if [ "$#" -eq 1 ]; then
pushd $1
chmod 750 configure
./configure --enable-opensslextra --enable-aesgcm --enable-sha512 --enable-ripemd --enable-ecc --enable-static
autoreconf
make 2>errs
cp ./src/.libs/libcyassl.a ${PROJECTS}/lib
else
pushd $1
if [ $2 = 'x64' ] ; then
awk '/^CCFLAGS /{$0=$0 " -DPROCMACH64"}1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make
else
awk '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make
fi
fi
