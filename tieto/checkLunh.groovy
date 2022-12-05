if( TXN.rej_status?.trim() == 'RDY' ) return
if( TXN.rej_status?.trim() == 'SND' ) return


if( !['VISA','EURO'].contains( TXN.firma?.trim() ) )
    return

Card = TXN.card_no_dec?.trim()

if( Card )
{
 int Sum =0, LCh = 0, I = 0, C = 0;
 Card.reverse().each()
 {
   C = it.codePointAt(0)  - '0'.codePointAt(0)
   if( I == 0 )
    LCh = C
   else
   { C = (I%2+1)*C
     if( C > 9 )
       C -= 9

     Sum += C
   }
   I++
 }
 
 Sum %= 10
 Sum = (Sum == 0?0:10-Sum)
    
 if( Sum != LCh )
 {
  TXN.rej_status = "LUH_ERR"
  TXN.rej_comment = "Last digit "+LCh+" <> Lunh digit "+Sum
  return REJECTED     
 }
}
