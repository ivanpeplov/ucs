def call(String path, String arch) {
    dir (path) {
        switch (path) {
            //"cyassl, myizip_z, microx_t"
            case ('units') :
                loadScript(place:'linux', name:'mmCpp.sh')
                mmCpp(mm, arch)
            break
            //"microp, ucs_mm, ucs_ms, ucs_dt"
            case ('units/microx_t/samples') :
                loadScript(place:'linux', name:'mmCpp.sh')
                mmCpp(mmm, arch)
                loadScript(place:'linux', name:'mmArt.sh')
                sh "./mmArt.sh" // prepare for upload
            break
        }//switch $path
    }//dir()
}//end
