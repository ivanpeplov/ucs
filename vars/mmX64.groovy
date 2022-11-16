def call (String sample) {
sh """
pushd ${sample}
awk -i inplace '/^CCFLAGS /{\$0=\$0 " -DPROCMACH64"}1' filedefs.inc
echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
"""
}