if( TXN.rej_status?.trim() == 'RDY' ) return

if( TXN.rej_status?.trim() == 'SND' ) {
    return
}

// Remove test cards

if( SVC.REF.testCards.get( TXN.card_no_dec ) ) {
    TXN.rej_status = 'TST_CRD'
    return DELETED;
}

// --- Remove test RANGE 589379 ---
// --- Remove test RANGE 67707698 ---
if( TXN.card_no_dec.startsWith('589379') || TXN.card_no_dec.startsWith('67707698') ) {
    TXN.rej_status = 'TST_RNG'
    return DELETED;
}
// --- Remove test HOID 09999 ---
if( TXN.ho_id == '09999' || TXN.ho_id == '09997') {
    TXN.rej_status = 'TST_HO'
    return DELETED;
}

// --- Remove test MID  ---
if( TXN.merchantid == '3209113001')
{ TXN.rej_status = 'TST_MER'
   return DELETED;
}
