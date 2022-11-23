def call(String path) {
    dir (path) {
        id1=id.split(', ')
        switch (path) {
            case ("${WORKSPACE}") : //build for "cyassl, myizip_z, microx_t"
                loadWinBat('msBuild.bat')
                sample=mm.split(', ')
                for (int k=0; k < id1.size(); k++) {
                    for (int i=0; i < sample.size(); i++) {
                    bat (script:"msBuild.bat ${sample[i]} ${id1[k]}") }
                }
            break
            case ("microx_t/samples") : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt"
                loadWinBat('msBuild.bat')
                sample1=mmm.split(', ')
                for (int k=0; k < id1.size(); k++) {
                    for (int i=0; i < sample1.size(); i++) {
                        bat(script:"msBuild.bat ${sample1[i]} ${id1[k]}")  
                        if ("${sample1[i]}"=='ucs_ms') {
                        loadWinBat('lib_ucs_ms.bat') //ucs_ms.lib stub for ucs_ms.dll copy to ./microx_t/test/bin
                        bat(script:"lib_ucs_ms.bat ${sample1[i]} ${id1[k]}" )
                        }
                    }
                }
                //build for setup_p.msi
                bat (script:"cd setup_p & devenv setup_p.sln /build Release")
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//end
