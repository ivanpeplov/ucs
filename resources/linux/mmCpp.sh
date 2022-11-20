#!/bin/sh
#usage: mX64.sh ${sample} | CCADDFLAGS addin for X64 micro_mod.groovy
pushd $1
if [ $2 = 'x64' ] ; then
awk '/^CCFLAGS /{$0=$0 " -DPROCMACH64"}1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make
else
awk '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make
fi