def Nix (String units) { // for mm_nix, fis
    loadScript(place:'linux', name:'mmBuild.sh')
    units.split(',').each { u-> sh "./mmBuild.sh ${u} ${OS_ARCH}" }
}
def Win (String units) { // for mm_win
    loadScript(place:'win', name:'mmBuild.bat')
    arch.split(',').each { a -> units.split(',').each { u-> bat "mmBuild.bat ${u} ${a}" } }
}
def Android (String units) { // for mm_android
    if (units=='mmcore')
    {loadScript(place:'gradle', name:'addToPom.xml')}
    loadScript(place:'linux', name:'androidBuild.sh')
    sh "./androidBuild.sh"
}

