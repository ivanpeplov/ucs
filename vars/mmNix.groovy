def call (String yml, String arch) {
smpl=yml.split(',')
smpl.each { x-> sh(script:"./mmBuild.sh ${x} ${arch}") }
}