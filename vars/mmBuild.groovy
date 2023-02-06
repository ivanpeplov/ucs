def Nix (String yml, String arch) {
    loadScript(place:'linux', name:'mmBuild.sh')
    smpl=yml.split(',')
    smpl.each { x-> sh(script:"./mmBuild.sh ${x} ${arch}") }
}
def Win (String yml, String arch) {
    smpl=yml.split(',')
    id=arch.split(',')
    id.each { x-> smpl.each { y-> 
        bat (script: "cd ${y} & msbuild ${y}.sln /t:build /p:configuration=Release /p:Platform=${x}") 
        if (y=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat(script:"mmLib.bat ${y} ${x}" ) } } }
}
def Fis (String modules, String release) {
    units = modules.split(',').toList()
    units.each {f -> sh "cd ${f} ; Make ${release}"}
}
