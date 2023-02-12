//called from pthScript.groovy
import org.apache.commons.io.FilenameUtils
def call (Map pth=[:]) { //PTH, XDB conversion
    pth.get('l3', '') // default value for map. change 'null' to ''
    if (pth.todo=='PTH') //filter .ktr/.kjb or .xml files
    { list = listFiles.Nix ("${pth.l1}/${pth.l2}/${pth.l3}")
    .findAll { it=~ /(?i)\.(?:ktr|kjb)$/ } } //get all .ktr/.kjb
    else 
    { list = listFiles.Nix ("${pth.l1}/${pth.l2}/${pth.l3}")
    .findAll { it=~ /(?i)\.(?:xml)$/ } } //get all .xml
    list.each { l->
        name = FilenameUtils.removeExtension(l) //each filename
        extension  = FilenameUtils.getExtension(l) //each extension
        if (pth.todo=='PTH') 
        { sh "./pthChecker.sh ${pth.l2} ${name} ${extension} ${pth.l3}" }
        else
        { sh "./xdbChecker.sh ${pth.l2} ${name} ${extension} ${pth.l3}" }
    }
}