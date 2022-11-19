def call(String path, String arch, String rel) {
    dir (path) {
        switch (path) {
            case ("${WORKSPACE}") : //build loop for main modules
                loadWinBat('mmX64.bat')
                println "cyassl, myizip_z, microx_t"
                sample=mm.split(', ') 
                for (int i=0; i < sample.size(); i++) {
                bat(script:"./mmX64.bat ${sample[i]} ${arch} ${rel}")
                }
            break
            case ("${WORKSPACE}/microx_t/samples") : //build loop for submodules under microx_t
                loadWinBat('mmX64.bat')
                println "microp, ucs_mm, ucs_ms, ucs_dt"
                sample1=mmm.split(', ') 
                for (int i=0; i < sample1.size(); i++) {
                bat(script:"./mmX64.bat ${sample1[i]} ${arch} ${rel}" )
                if (sample1[i]=='microp') {
                    loadWinBat('myizip_lib.bat')
                    bat(script:"./myizip_lib.bat ${sample1[i]} ${arch}" )
                }
                if (sample1[i]=='ucs_ms') {
                    loadWinBat('ucs_ms.bat')
                    bat(script:"./ucs_ms.bat ${sample1[i]} ${arch}" )
                }
                }
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//def call()
