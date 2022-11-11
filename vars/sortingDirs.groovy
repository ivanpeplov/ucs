def call(String test) {
//sorting for BonusETL_top.PTH
///DummyJob' should contain 1 at tail - Dummyjob1
//def test =[ 'DummyJob1', 'Stage10', 'Stage100', 'Stage101', 'Stage102', 'Stage103', 'Stage104', 'Stage20', 'Stage30', 'Stage40', 'Stage50', 'Stage60', 'Stage70', 'Stage80', 'Stage90']
test.sort{ a,b ->
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
return test
//Result: [DummyJob1, Stage10, Stage20, Stage30, Stage40, Stage50, Stage60, Stage70, Stage80, Stage90, Stage100, Stage101, Stage102, Stage103, Stage104]
}
