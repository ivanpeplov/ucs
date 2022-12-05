if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// Не проверяем операции с rej_status = SND
if( TXN.rej_status?.trim() == 'SND' ) {
    return
}

// check black merchant & HO

if( TXN.trans_type == '010' &&
     (SVC.REF.blackCrdMerchants.get( TXN.merchantid ) || SVC.REF.blackCrdHOs.get( TXN.ho_id ) ) ) {
    TXN.rej_status = 'CRD'
    return REJECTED
}

if( TXN.trans_type == '010' &&
    TXN.source_code?.trim() == 'MDC' &&
    TXN.amount > 1000 &&
    ['100001', '100002', '100003'].contains(TXN.slip_no) 
  )
{
    TXN.rej_status = 'CRD'
    return REJECTED
}

// check lenght auth_code
if( ![null,0,6].contains( TXN.auth_code?.trim()?.length()) )
{
    TXN.rej_status = 'LenACodeEr'
    return REJECTED
}
