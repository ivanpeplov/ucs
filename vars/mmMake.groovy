def call(String path, String arch) {
    dir (path) {
        switch (path) {
            case ('units/cyassl-3.2.0') :
            loadLinuxScript('cyasslMake.sh')
            sh "./cyasslMake.sh"
            //cyasslMake()
            break
            case ('units') :
                loadLinuxScript('mmX64.sh')
                loadLinuxScript('mmX86.sh')
                println "myizip_z, microx_t"
                sample=mm.split(', ') //'myizip_z', 'microx_t', 'cyassl-3.2.0', 'axcoder'
                for (int i=0; i < 2; i++) //myizip_z', 'microx_t
                { arch=='x64' ? sh(script:"./mmX64.sh ${sample[i]}") : sh(script:"./mmX86.sh ${sample[i]}") }
            break
            case ('units/microx_t/samples') :
                loadLinuxScript('mmX64.sh')
                loadLinuxScript('mmX86.sh')
                println "microp, ucs_mm, ucs_ms, ucs_dt"
                sample1=mmm.split(', ') //'microp', 'ucs_mm', 'ucs_ms', 'ucs_dt'
                for (int i=0; i < sample1.size(); i++)
                { arch=='x64' ? sh(script:"./mmX64.sh ${sample1[i]}") : sh(script:"./mmX86.sh ${sample1[i]}") }
                sh "cp ${PROJECTS}/lib/*.so ${PROJECTS}/bin/" //cp libucs_ms.so to TARGET
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//def call()
