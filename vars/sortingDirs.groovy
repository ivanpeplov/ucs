//test =[ 'DummyJob1', 'Stage10', 'Stage100', 'Stage101', 'Stage102', 'Stage103', 'Stage104', 'Stage20', 'Stage30', 'Stage40', 'Stage50', 'Stage60', 'Stage70', 'Stage80', 'Stage90']
//sortExample(test)
//println test
//def call (String tst)
@NonCPS
def sortExample(tst) {
tst.sort{ a,b ->
    def n1 = (a =~ /\d+/)[-1] as Integer
    def n2 = (b =~ /\d+/)[-1] as Integer

    def s1 = a.replaceAll(/\d+$/, '').trim()
    def s2 = b.replaceAll(/\d+$/, '').trim()

    if (s1 == s2){
        return n1 <=> n2
    }
    else{
        return s1 <=> s2
    }
}
}