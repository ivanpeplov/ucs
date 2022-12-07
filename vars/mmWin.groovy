def call (String yml, String arch) {
    id=arch.split(',')
    smpl=yml.split(',')
    id.each { x-> smpl.each { y-> bat (script:"mmBuild.bat ${y} ${x}" ) 
    if (y=='ucs_ms') {
        loadScript(place:'win', name:'lib_ucs_ms.bat') 
        bat(script:"lib_ucs_ms.bat ${y} ${x}" ) } } }
}