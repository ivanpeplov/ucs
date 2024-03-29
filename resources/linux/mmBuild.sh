#!/bin/bash
# mm_nix.groovy; fis.groovy
if [ $1 = 'cyassl-3.2.0' ] ; then
cd $1
chmod 750 configure
./configure --enable-opensslextra --enable-aesgcm --enable-sha512 --enable-ripemd --enable-ecc --enable-static
autoreconf
make 2>errs
cp ./src/.libs/libcyassl.a ${PROJECTS}/lib
else
cd $1
if [ $2 = '64' ] ; then
case "${JOB_BASE_NAME}" in
fis) 
Make ${RELEASE} 2>errs ;;
*)
awk '/^CCFLAGS /{$0=$0 " -DPROCMACH64"}1' filedefs.inc > tmp && mv tmp filedefs.inc
Make release 2>errs ;;
esac
else
awk '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc > tmp && mv tmp filedefs.inc
Make release 2>errs
fi
fi
