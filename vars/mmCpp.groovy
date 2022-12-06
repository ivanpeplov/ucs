def call (String yml, String arch) {
loadScript(place:'linux', name:'mmCpp.sh')
smpl=yml.split(',')
smpl.each { x-> sh(script:"./mmCpp.sh ${x} ${arch}") }
}