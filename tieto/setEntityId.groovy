if( TXN.rej_status?.trim() == 'RDY' ) return

def PadAndCut( obj, size, pad, right )
{
 String T = obj.toString().trim();
 
 if( right )
  T = T.padRight( size, pad )
 else
  T = T.padLeft( size, pad )
  
 if( T.length() > size )
 {
  if( right )    
   T = T.substring( 0, size );
  else
   T = T.substring( T.length()-size, T.length() );
 }
 
 return T;
}

TXN.entity_id = PadAndCut( (ENV.EntityPrefix == null?'UUU': ENV.EntityPrefix )/*TXN.source_code*/, 3, ' ', true ) +
                TXN.proc_date.format('yyMMdd') +
                PadAndCut( TXN.id, 11, '0', false );
