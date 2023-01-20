def call (String yml, String arch) {
    id=arch.split(',')
    smpl=yml.split(',')
    id.each { x-> smpl.each { y-> bat (script:"mmBuild.bat ${y} ${x}" ) 
    if (y=='ucs_ms') {
        loadScript(place:'win', name:'mmMicrosCopy.bat') 
        bat(script:"mmMicrosCopy.bat ${y} ${x}" ) } } }
}