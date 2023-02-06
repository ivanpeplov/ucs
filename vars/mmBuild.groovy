def Nix (String units, String arch) {
    loadScript(place:'linux', name:'mmBuild.sh')
    smpl=units.split(',')
    smpl.each { x-> sh "./mmBuild.sh ${x} ${arch}" }
}
def Win (String units, String arch) {
    smpl=units.split(',')
    id=arch.split(',')
    id.each { x-> smpl.each { y-> 
        bat "cd ${y} & msbuild ${y}.sln /t:build /p:configuration=Release /p:Platform=${x}"
        if (y=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat "mmLib.bat ${y} ${x}" } } }
}
def Fis (String modules, String release) {
    units = modules.split(',').toList()
    units.each {f -> sh "cd ${f} ; Make ${release}"}
}
