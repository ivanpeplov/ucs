def call() {
//use ASSERT_EQ (TAIL, '') for hsmMake() for example
//or assert arg1 == arg2 at any place. 
  def ASSERT_EQ (def arg1, def arg2) {
    if (arg1 != arg2)
      {
      println ('arg1 = ' + arg1 + " " + arg1.getClass())
      println ('arg2 = ' + arg2 + " " + arg2.getClass())
      }
    assert arg1 == arg2
    }
}