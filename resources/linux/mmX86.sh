#!/bin/sh
#usage: mX86.sh ${sample}
pushd $1
awk '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make