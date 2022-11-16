def call (String sample) {
sh """
pushd ${sample}
awk -i inplace '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc
echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
"""
}