#!/bin/sh
#usage: mX64.sh ${sample}
pushd $1
awk '/^CCFLAGS /{$0=$0 " -DPROCMACH64"}1' filedefs.inc > tmp && mv tmp filedefs.inc
echo release 2>errs | xargs -n 1 "${PROJECTS}"/tools/Make