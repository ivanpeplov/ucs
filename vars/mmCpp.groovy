def call (String yml, String arch) {
smpl=yml.split(',')
smpl.each { x-> sh(script:"./mmCpp.sh ${x} ${arch}") }
}