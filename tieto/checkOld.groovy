if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// task ntp-143 Reject future date
if( TXN.trans_date > TXN.proc_date ) {
    TXN.rej_status = "AFTERTIME";
    return REJECTED;
}

// Не проверяем операции с rej_status = SND
if( TXN.rej_status?.trim() == 'SND' ) {
    return
}

// Не проверяем операции из FRB_OUT
if( TXN.source_code.trim() == 'FRB_OUT' ) {
    return
}

// Не проверяем реверсалы из DCS
if( TXN.source_code?.trim() == 'DCS' ) {
    return
}

// Skip Gold Crown
//if( TXN.source_code?.trim() == 'KRNA') {
//    return
//}

// Reject old transactions

if( TXN.trans_date < TXN.proc_date - 31 ) {
    TXN.rej_status = "OLD";
    return REJECTED;
}
