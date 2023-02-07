// for mm_nix, mm_win, fis
def Nix (String units) {
    loadScript(place:'linux', name:'mmBuild.sh')
    modules=units.split(',')
    modules.each { m-> sh "./mmBuild.sh ${m} ${OS_ARCH}" }
}
def Win (String units) {
    modules=units.split(',')
    id=arch.split(',')
    id.each { x-> modules.each { m-> 
        bat "cd ${m} & msbuild ${m}.sln /t:build /p:configuration=Release /p:Platform=${x}"
        if (m=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat "mmLib.bat ${m} ${x}" } } }
}
