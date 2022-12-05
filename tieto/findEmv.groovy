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

//println "ext_data '${TXN.ext_data}'"

// Only for empty EMV data
if( TXN.ext_data != null && TXN.ext_data?.trim() != '' ) {
//    println "Won't find EMV trans_id: " + TXN.id
    return
}

//println "Find EMV trans_id: ${TXN.id} card_no: ${TXN.card_no_dec} "

// Восстановление EMV-данных в оффлайн-транзакциях по преавторизациям.

List preauthList = SVC.transFinder.findPreauth( TXN.id, TXN.card_no_dec?.trim(), TXN.auth_code, TXN.merchantid )

if( preauthList.size() == 1 && preauthList[0].ext_data?.trim() != '' ) {
    TXN.ext_data = preauthList[0].ext_data
    
    if( TXN.ext_data?.trim()?.length() > 14 )
    {
     ICC = TXN.ext_data.substring(14);
     if( ICC.startsWith( 'ICC1' ) ) TXN.entry_mode= '05'
     else
     if( ICC.startsWith( 'NFC1' ) ) TXN.entry_mode= '07'
    }
//    println "EMV found. preath id: " + preauthList.get( 0 ).trans_id + ", trans_id: " + TXN.id
}/* else {
    println "EMV not found. trans_id: ${TXN.id}, card_no: ${TXN.card_no_dec}, res: $preauthList "

}*/
