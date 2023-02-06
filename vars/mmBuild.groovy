def Nix (String units, String arch) {
    loadScript(place:'linux', name:'mmBuild.sh')
    modules=units.split(',')
    modules.each { f-> sh "./mmBuild.sh ${f} ${arch}" }
}
def Win (String units, String arch) {
    modules=units.split(',')
    id=arch.split(',')
    id.each { x-> modules.each { f-> 
        bat "cd ${f} & msbuild ${f}.sln /t:build /p:configuration=Release /p:Platform=${x}"
        if (y=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat "mmLib.bat ${y} ${x}" } } }
}
def Fis (String units, String release) {
    modules = units.split(',')
    modules.each {f -> sh "cd ${f} ; Make ${release}"}
}
