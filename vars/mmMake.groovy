def call(String path, String arch) {
    dir (path) {
        switch (path) {
            case ('units/cyassl-3.2.0') :
            loadLinuxScript('cyasslMake.sh')
            sh "./cyasslMake.sh" //build for libcyassl.a
            break
            case ('units') : //loop for main modules
                loadLinuxScript('mmX64.sh') //CCADDFLAGS for X64
                loadLinuxScript('mmX86.sh') //CCADDFLAGS for X86
                println "myizip_z, microx_t"
                sample=mm.split(', ') //'myizip_z', 'microx_t'
                for (int i=0; i < sample.size(); i++)
                //elvis groovy operator
                { arch=='x64' ? sh(script:"./mmX64.sh ${sample[i]}") : sh(script:"./mmX86.sh ${sample[i]}") }
            break
            case ('units/microx_t/samples') : //loop for submodules under microx_t
                loadLinuxScript('mmX64.sh')
                loadLinuxScript('mmX86.sh')
                println "microp, ucs_mm, ucs_ms, ucs_dt"
                sample1=mmm.split(', ') //'microp', 'ucs_mm', 'ucs_ms', 'ucs_dt'
                for (int i=0; i < sample1.size(); i++)
                //elvis groovy operator
                { arch=='x64' ? sh(script:"./mmX64.sh ${sample1[i]}") : sh(script:"./mmX86.sh ${sample1[i]}") }
                sh "cp ${PROJECTS}/lib/*.so ${PROJECTS}/bin/" //cp libucs_ms.so to TARGET
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//def call()
