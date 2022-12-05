if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

if (TXN.merchantid?.trim() || TXN.term_id?.trim()) {
    
    if (TXN.term_id?.trim())    
     res = SVC.mms.findByMidTieto('T:'+TXN.term_id.trim(), TXN.trans_date)
    
    if (res.error && TXN.merchantid?.trim())
     res = SVC.mms.findByMidTieto(TXN.merchantid.trim(), TXN.trans_date)
    
    if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }
    else {
        TXN.merchantid = res.merch_id
        TXN.ho_id = res.ho_id
        TXN.id_fi = res.id_fi
        TXN.term_id = res.term_id

        ENV.merchInfo = res // запоминаем информацию по мерчанту

        TXN.bs_id = SVC.REF.HOtoBSbyDate.get( TXN.ho_id, TXN.proc_date)?.get("clerk_id")
    }
}
else {
    TXN.rej_status = "NO ID"
    return REJECTED
}
