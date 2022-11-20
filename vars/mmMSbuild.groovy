def call(String path, String arch, String rel ) {
    dir (path) {
        switch (path) {
            case ("${WORKSPACE}/cyassl-3.2.0") :
            bat (script:"msbuild cyassl.sln /t:build /p:configuration=Release${rel} /p:Platform=${arch}")
            break
            case ("${WORKSPACE}") : //build for "cyassl, myizip_z, microx_t"
                loadWinBat('msBuild.bat')
                sample=mm.split(', ') 
                for (int i=0; i < sample.size(); i++) {
                bat (script:"msBuild.bat ${sample[i]} ${arch} ${rel}") }
            break
            case ("${WORKSPACE}/microx_t/samples") : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt"
                loadWinBat('msBuild.bat')
                loadWinBat('msBuild_ucs_mm.bat')
                sample1=mmm.split(', ') 
                for (int i=0; i < sample1.size(); i++) {
                    sample1[i]=='ucs_mm' ? bat(script:"msBuild_ucs_mm.bat ${sample1[i]} ${arch} ${rel}") : bat(script:"msBuild.bat ${sample1[i]} ${arch} ${rel}")
                    switch (sample1[i]) {
                        case ('microp') :
                        loadWinBat('myizip_lib.bat') //myizip_z.lib service copy for build to ./microx_t/samples/microp
                        bat(script:"myizip_lib.bat ${sample1[i]} ${arch}" )
                        break
                        case ('ucs_ms') :
                        loadWinBat('ucs_ms.bat') //ucs_ms.lib stub for ucs_ms.dll service copy to ./microx_t/test/bin
                        bat(script:"ucs_ms.bat ${sample1[i]} ${arch}" )
                        break
                    }
                }
            break
            default:
                println "TBD"
        }//switch $path
    }//dir()
}//def call()
