if( TXN.rej_status?.trim() == 'RDY' ) return
if( TXN.rej_status?.trim() == 'SND' ) return

if( [ "JCB" ].contains( TXN.firma?.trim() ) )
 TXN.ext_data = null
