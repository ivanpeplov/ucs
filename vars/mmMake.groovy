def call(String path, String arch) {
    dir (path) {
        switch (path) {
            case ('units') : //build loop for "cyassl, myizip_z, microx_t"
                loadLinuxScript('cyasslMake.sh')
                loadLinuxScript('mmCpp.sh')
                sample=mm.split(', ') 
                for (int i=0; i < sample.size(); i++) {
                if (sample[i]=='cyassl') {sh "./cyasslMake.sh ${sample[i]}"} //build for libcyassl.a
                sh(script:"./mmCpp.sh ${sample[i]} ${arch}") }
            break
            case ('units/microx_t/samples') : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt"
                loadLinuxScript('mmCpp.sh')
                sample1=mmm.split(', ') 
                for (int i=0; i < sample1.size(); i++) {
                sh(script:"./mmCpp.sh ${sample1[i]} ${arch}") }
                sh "cp ${PROJECTS}/lib/*.so ${PROJECTS}/bin/" //cp libucs_ms.so to TARGET
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//def call()
