def call(String path, String arch) {
    dir (path) {
        switch (path) {
        case ('units') :
            sample=mm.split(', ') //'myizip_z', 'microx_t', 'cyassl-3.2.0', 'axcoder'
            for (int i=0; i < 2; i++) { //myizip_z', 'microx_t
                switch (arch) {
                    case ('x64') :
                    mmX64(sample[i])
                    break
                    default : //'atol' currently
                    mmX86(sample[i])
                }//switch (arch) finished
            }//loop for sample[i] finished
        break
        case ('units/microx_t/samples') :
            sample1=mmm.split(', ') //'microp', 'ucs_mm', 'ucs_ms', 'ucs_dt'
            for (int i=0; i < sample1.size(); i++) {
                switch (arch) {
                    case ('x64') :
                    mmX64(sample1[i])
                    default : // 'atol' currently
                    mmX86(sample1[i])
                } //switch (arch) finished
            }//loop for sample1[i] finished
            sh "cp ${PROJECTS}/lib/*.so ${PROJECTS}/bin/" //cp libucs_ms.so to TARGET
        break
        default:
            println "TBD"
        }//switch $path finished
    }//dir() finished
}//def call() finished
