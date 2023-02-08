def Nix (String units) { // for mm_nix.groovy, fis.groovy
    loadScript(place:'linux', name:'mmBuild.sh')
    modules=units.split(',')
    modules.each { m-> sh "./mmBuild.sh ${m} ${OS_ARCH}" }
}
def Win (String units) { // for mm_win
    modules=units.split(',')
    id=arch.split(',')
    id.each { a -> modules.each { m-> 
        bat "cd ${m} & msbuild ${m}.sln /t:build /p:configuration=Release /p:Platform=${a}"
        if (m=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat "mmLib.bat ${m} ${a}" } } }
}
