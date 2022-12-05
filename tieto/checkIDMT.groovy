if( !['DCS', 'MDC', 'CBK'].contains(TXN.proc_class) )
    return
    
if(!(
    !['E','T','M','D','C'].contains(TXN.id_method) ||
     ['C'].contains(TXN.id_method) && ['1','5','6','7',' ', null].contains(TXN.eci) ||
     ['D'].contains(TXN.id_method) && ['7',' ', null].contains(TXN.eci) ||
     ['E'].contains(TXN.id_method) && ['5','6','7'].contains(TXN.eci) ||
     ['T'].contains(TXN.id_method) && ['5','6','7','9'].contains(TXN.eci) ||
     ['M'].contains(TXN.id_method) && ['1'].contains(TXN.eci)
    )
  )
   TXN.rej_codes += 'R21'

if(!(
    !['T'].contains(TXN.id_method) ||
     ['T'].contains(TXN.id_method) && ['4814'].contains(ENV.merchInfo?.mcc)
    )
  )
   TXN.rej_codes += 'R11'

if( 'T'.equals( TXN.id_method ) && 'EURO'.equals( TXN.ct_id ) )
{
 INDD = TXN.ind_data
 if( INDD == null ||
    !INDD.matches( '99MTUP\\d{10}.{37} {6}.*' )
   )
  TXN.rej_codes += 'R22'
}

if( 'T'.equals( TXN.id_method ) && 'VI'.equals( TXN.subdomain ) )
{
 if( ['9'].contains(TXN.eci) )    
 {
  TXN.id_method = 'R'  
  TXN.eci = ' '
 }
 else
 if( ['5','6','7'].contains(TXN.eci) )    
  TXN.id_method = 'E'  
}



if( 'R'.equals( TXN.id_method ) && (TXN.eci == null || TXN.eci.trim().isEmpty() ) )
{
 TXN.devicetype = ' '
 TXN.capability = '1'
 TXN.pin_cpblty = '2'
 TXN.term_id = ''
}

if( TXN.id_method == null )
TXN.id_method = ' '



