def call(String path) {
    dir (path) {
        id=arch.split(',') // Win32 or x64
        switch (path) {
            case ("${WORKSPACE}") : //build loop for "cyassl, myizip_z, microx_t"
                loadScript(place:'win', name:'mmBuild.bat')
                sample=mm.split(',')
                id.each {x->
                sample.each { y-> bat (script:"mmBuild.bat ${y} ${x}") } }
            break
            case ("microx_t/samples") : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt, ucs_mu"
                loadScript(place:'win', name:'mmBuild.bat')
                sample1=mmm.split(',')
                id.each { x->
                sample1.each { y-> bat (script:"mmBuild.bat ${y} ${x}")
                    if ("${y}"=='ucs_ms') {
                    loadScript(place:'win', name:'lib_ucs_ms.bat') //ucs_ms.lib stub for ucs_ms.dll copy to ./microx_t/sample/test/bin
                    bat(script:"lib_ucs_ms.bat ${y} ${x}" ) } } } //.each .each if end
                loadScript(place:'win', name:'mmArt.bat')
                bat (script:"mmArt.bat") //build for setup_p.zip from setup_p.msi
            break
        }//switch $path
    }//dir()
}//end
