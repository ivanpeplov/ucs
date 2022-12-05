if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// Не проверяем реверсалы из DCS
if( TXN.source_code?.trim() == 'DCS' && ["030", "040"].contains(TXN.trans_type)) {
    return
}

// Skip Gold Crown
if( TXN.source_code?.trim() == 'KRNA') {
    return
}

// Восстановление номера карты в «доавторизациях» по TERM_REF (CT_ID=’MISS’)

if( TXN.card_no_dec.startsWith( '999666'))
    TXN.firma = 'MISS'

if( TXN.firma == 'MISS') {
    List authList = SVC.auth.findByAuthCodeTermRefNo( TXN.auth_code, TXN.card_no_dec.substring( 6 ) )

    if( !authList || authList.size() != 1 ) {
        // Try to find by short term_ref_no
        authList = SVC.auth.findByAuthCodeTermRefNo( TXN.auth_code, TXN.card_no_dec.substring( 6+8 ) )

        if( !authList || authList.size() != 1 ) {
            TXN.rej_status = 'MISS'
            //return REJECTED
        }
         if( ['010'].contains( TXN.trans_type ) )
          TXN.auth_code=''

    }

    if( TXN.rej_status != 'MISS' )
    {
     TXN.card_no_dec = authList.get(0).card_no_dec
     TXN.firma = authList.get(0).ct_id
     TXN.entry_mode = authList.get(0).entry_mode_code?.substring( 0, 2 )
     TXN.exp_date = authList.get(0).exp_date.format('MMyy')

      if( ['010'].contains( TXN.trans_type ) )
         TXN.auth_code=''
    }
//    println "Found card_no for trans_id: ${TXN.id} card_no ${TXN.card_no_dec} "
}

if( TXN.firma == 'MISS')
{
    TXN.rej_status = ''
    List trList = SVC.transFinder.findPreauthLess( TXN.id, TXN.card_no_dec.substring( 6 ), null, TXN.auth_code, null )
    
    if( !trList || trList.size() != 1 )
     trList = SVC.transFinder.findPreauthLess( TXN.id, null, TXN.card_no_dec.substring( 6+8 ), TXN.auth_code, null )
     
    if( !trList || trList.size() != 1 )
     TXN.rej_status = 'MISS'
    else
    {
     if( TXN.mdc_trans_id == "06" )
      for( name in ['card_no_dec','exp_date','firma'] )
       TXN.setProperty( name, trList.get(0).get( name ) )
     else
     if( TXN.mdc_trans_id == "09" )
      for( name in ['card_no_dec','exp_date','firma', 'clishe_no', 'merchantid', 'term_id', 'authsource', 'entry_mode', 'id_method', 'ext_data', 'devicetype', 'pin_cpblty'] )
       TXN.setProperty( name, trList.get(0).get( name ) )
     else
      TXN.rej_status = 'MISS'
    }
    
    //println( "preauthless: "+TXN.firma )
}

if( TXN.rej_status == 'MISS' )
 return REJECTED
 