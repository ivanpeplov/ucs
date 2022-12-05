if( TXN.rej_status?.trim() == 'RDY' ) return
if( TXN.rej_status?.trim() == 'SND' ) return

if( SVC.REF.blackExtData.get( TXN.ho_id ) )
  TXN.ext_data = null
