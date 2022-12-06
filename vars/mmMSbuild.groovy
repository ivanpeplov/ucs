def call(String path) {
    dir (path) {
        switch (path) {
            case ("${WORKSPACE}") : //"cyassl, myizip_z, microx_t"
                loadScript(place:'win', name:'mmBuild.bat')
                mmBuild(arch, mm)
            break
            case ("microx_t/samples") : //"microp, ucs_mm, ucs_ms, ucs_dt, ucs_mu"
                loadScript(place:'win', name:'mmBuild.bat')
                mmBuild(arch, mmm)
                loadScript(place:'win', name:'mmArt.bat')
                bat (script:"mmArt.bat") //build for setup_p.zip from setup_p.msi
            break
        }//switch $path
    }//dir()
}//end
