if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

//  rej_status = SND
if( TXN.rej_status?.trim() == 'SND' ) {
    return
}
// Add special for HO 33047 07.11.2017
if( ['33047'].contains( TXN.ho_id ) &&
    !( ['37459','37439'].contains( TXN.card_no_dec?.substring(0,5) ) || ['375120'].contains( TXN.card_no_dec?.substring(0,6) ) )
  )
{
    TXN.rej_status = 'FNOTBTA'
    return REJECTED
}


// check merchant & HO
if( (SVC.REF.whiteBtaMerchants.get( TXN.merchantid ) || SVC.REF.whiteBtaHOs.get( TXN.ho_id )) &&
    !TXN.card_no_dec?.startsWith( '375120' )
  )
{
    TXN.rej_status = 'FNOTBTA'
    return REJECTED
}

// Aproove only one card for processing ( rule amex  11.12.2018 ) 
if(  ['3045935001'].contains( TXN.merchantid?.trim() ) &&
    !['375120018941008'].contains( TXN.card_no_dec?.trim() )
  )
{
    TXN.rej_status = 'FNOTBTA1'
    return REJECTED    
}