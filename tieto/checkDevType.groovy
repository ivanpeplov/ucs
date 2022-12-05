if( !['DCS', 'MDC', 'CBK'].contains(TXN.proc_class) )
    return

if( ['5'].contains(TXN.devicetype) && !['5','6','7'].contains(TXN.eci) )
 TXN.devicetype = '5'

if( ['1','2','3','7','M'].contains(TXN.devicetype) && !['2','5','8','0'].contains(TXN.capability) )
   TXN.rej_codes += 'R23'
   
if( 'EEP'.equals( TXN.clerc_id ))
 TXN.devicetype = '7'

if( !(TXN.term_id?.trim()?.length() > 0) )
 {
  TXN.devicetype = ' '
  TXN.capability = '1'
  TXN.pin_cpblty = '2'
 }

if( TXN.devicetype == null )
 TXN.devicetype = ' '
