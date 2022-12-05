if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

//

if (TXN.auth_code?.trim()) {
    authId = SVC.auth.find(TXN.card_no_dec, TXN.auth_code)
    if (authId != null) {
        TXN.auth_entity_id = authId
    }
}
