def Nix (String units) { // for mm_nix, fis
    loadScript(place:'linux', name:'mmBuild.sh')
    units.split(',').each { m-> sh "./mmBuild.sh ${m} ${OS_ARCH}" }
}
def Win (String units) { // for mm_win
    loadScript(place:'win', name:'mmBuild.bat')
    modules=units.split(',')
    arch.split(',').each { a -> modules.each { m-> bat "mmBuild.bat ${m} ${a}" } }
}
def Android (String units) { // for mm_android
    if (units=='mmcore')
    {loadScript(place:'gradle', name:'addToPom.xml')}
    loadScript(place:'linux', name:'androidBuild.sh')
    sh "./androidBuild.sh"
}

