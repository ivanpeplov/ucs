def call(String path, String arch) {
    dir (path) {
        switch (path) {
            case ('units') : //build loop for "cyassl, myizip_z, microx_t"
                loadScript(place:'linux', name:'mmCpp.sh')
                sample=mm.split(',') 
                for (int i=0; i < sample.size(); i++) {
                sample[i]=='cyassl-3.2.0' ? sh(script:"./mmCpp.sh ${sample[i]}") : sh(script:"./mmCpp.sh ${sample[i]} ${arch}") }  
            break
            case ('units/microx_t/samples') : //build loop for "microp, ucs_mm, ucs_ms, ucs_dt"
                loadScript(place:'linux', name:'mmCpp.sh')
                loadScript(place:'linux', name:'mmArt.sh')
                sample1=mmm.split(',') 
                for (int i=0; i < sample1.size(); i++) { sh(script:"./mmCpp.sh ${sample1[i]} ${arch}") }
                sh "./mmArt.sh"
            break
        }//switch $path
    }//dir()
}//end
