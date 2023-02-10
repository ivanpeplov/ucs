def Nix (String units) { // for mm_nix.groovy, fis.groovy
    loadScript(place:'linux', name:'mmBuild.sh')
    modules=units.split(',')
    modules.each { m-> sh "./mmBuild.sh ${m} ${OS_ARCH}" }
}
def Win (String units) { // for mm_win
    loadScript(place:'win', name:'mmBuild.bat')
    modules=units.split(',')
    id=arch.split(',')
    id.each { a -> modules.each { m-> bat "mmBuild.bat ${m} ${a}" } }
}
