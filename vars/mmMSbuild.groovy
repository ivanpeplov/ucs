def call(String path) {
    dir (path) {
        id=arch.split(',') // Win32 or x64
        switch (path) {
            case ("${WORKSPACE}") : //build loop for "cyassl, myizip_z, microx_t"
                loadWinBat('mmBuild.bat')
                sample=mm.split(',')
                for (int k=0; k < id.size(); k++) {
                    for (int i=0; i < sample.size(); i++) {
                        bat (script:"mmBuild.bat ${sample[i]} ${id[k]}")
                    }
                }
            break
            case ("microx_t/samples") : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt, ucs_mu"
                loadWinBat('mmBuild.bat')
                sample1=mmm.split(',')
                for (int k=0; k < id.size(); k++) {
                    for (int i=0; i < sample1.size(); i++) {
                        bat(script:"mmBuild.bat ${sample1[i]} ${id[k]}")  
                        if ("${sample1[i]}"=='ucs_ms') {
                            loadWinBat('lib_ucs_ms.bat') //ucs_ms.lib stub for ucs_ms.dll copy to ./microx_t/sample/test/bin
                            bat(script:"lib_ucs_ms.bat ${sample1[i]} ${id[k]}" ) 
                        }
                    }
                }
                loadWinBat("mmArt.bat")
                bat (script:"mmArt.bat") //build for setup_p.zip from setup_p.msi
            break
        }//switch $path
    }//dir()
}//end
