import org.apache.commons.io.FilenameUtils
def call (Map pth=[:]) { //PTH, XDB conversion
    pth.get('ss', '') // default value for map. change 'null' to ''
    if (pth.todo=='PTH') //filter .ktr/.kjb or .xml files
    {list = listFiles ("${pth.l1}/${pth.l2}/${pth.ss}").findAll { it=~ /(?i)\.(?:ktr|kjb)$/ } } //get all .ktr/.kjb
    else {list = listFiles ("${pth.l1}/${pth.l2}/${pth.ss}").findAll { it=~ /(?i)\.(?:xml)$/ } } //get all .xml
    for (i in list) {
        ext  = FilenameUtils.getExtension(i) //each filename
        name = FilenameUtils.removeExtension(i) //each extension
        if (pth.todo=='PTH') {sh "./pthChecker.sh ${pth.l2} ${name} ${ext} ${pth.ss}"}
        else {sh "./xdbChecker.sh ${pth.l2} ${name} ${ext} ${pth.ss}"}
    }
}