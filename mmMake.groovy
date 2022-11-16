def call(String path, String arch) {
    dir (path) {
        switch (path) {
        case ('units') :
            sample=['myizip_z', 'microx_t']
            for (int i=0; i < sample.size(); i++) {
                switch (arch) {
                    case ('x64') :
                    sh """
                    pushd ${sample[i]}
                    awk -i inplace '/^CCFLAGS /{\$0=\$0 " -DPROCMACH64"}1' filedefs.inc
                    echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
                    """
                    break
                    default :
                    sh """
                    pushd ${sample[i]}
                    awk -i inplace '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc
                    echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
                    """
                }//switch (arch) finished
            }//loop for sample[i] finished
        break
        case ('units/microx_t/samples') :
            sample1=['microp', 'ucs_mm', 'ucs_ms', 'ucs_dt']
            for (int i=0; i < sample1.size(); i++) {
                switch (arch) {
                    case ('x64') :
                    sh """
                    pushd ${sample1[i]}
                    awk -i inplace '/^CCFLAGS /{\$0=\$0 " -DPROCMACH64"}1' filedefs.inc
                    echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
                    """
                    default :
                    sh """
                    pushd ${sample[i]}
                    awk -i inplace '/ATOL Frontol/{ rl = NR + 1 } NR == rl { gsub( /#/,"") } 1' filedefs.inc
                    echo release 2>errs | xargs -n 1 ${PROJECTS}/tools/Make
                    """
                } //switch (arch) finished
            }//loop for sample1[i] finished
            sh "cp ${PROJECTS}/lib/*.so ${PROJECTS}/bin/"
        break
        default:
            println "TBD"
        }//switch $path finished
    }//dir() finished
}//def call() finished
