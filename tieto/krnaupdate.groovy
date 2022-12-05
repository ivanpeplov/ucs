if( TXN.rej_status?.trim() == 'RDY' ) return

if( TXN.source_code?.trim() != 'KRNA') {
    return
}

TXN.capability = '5'
TXN.authsource = '1'
TXN.entry_mode = '05'
TXN.pin_cpblty = '1'
TXN.devicetype = '7'
TXN.br_id = TXN.merchantid.substring(4,7)
