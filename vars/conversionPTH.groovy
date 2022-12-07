import org.apache.commons.io.FilenameUtils
def call (Map pth=[:]) { //pth conversion - .ktr/.kjb Pentaho files
    pth.get('ss', '') // default value for map. change 'null' to ''
    list = listFiles ("${pth.r}/${pth.l1}/${pth.l2}/${pth.ss}").findAll { it=~ /(?i)\.(?:ktr|kjb)$/ } //get all .ktr/.kjb
    for (i in list) {
    ext  = FilenameUtils.getExtension(i) //each .ktr/.kjb filename
    name = FilenameUtils.removeExtension(i) //each .ktr/.kjb extension
    sh "./conversionPTH.sh ${pth.l1} ${pth.l2} ${name} ${ext} ${pth.ss}" } //incl. substage (5 parameters)
}