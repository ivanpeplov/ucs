def call(String path, String arch) {
    dir (path) {
        switch (path) {
            case ('units') : //build loop for "cyassl, myizip_z, microx_t"
                loadScript(place:'linux', name:'mmCpp.sh')
                sample=mm.split(',') 
                sample.each { x-> x=='cyassl-3.2.0' ? sh(script:"./mmCpp.sh ${x}") : sh(script:"./mmCpp.sh ${x} ${arch}") }
            break
            case ('units/microx_t/samples') : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt"
                loadScript(place:'linux', name:'mmCpp.sh')
                loadScript(place:'linux', name:'mmArt.sh')
                sample1=mmm.split(',')
                sample1.each { x-> sh(script:"./mmCpp.sh ${x} ${arch}") }
                sh "./mmArt.sh" // prepare for upload
            break
        }//switch $path
    }//dir()
}//end
