if( TXN.rej_status?.trim() == 'RDY' ) return

if( 'CBK' != TXN.proc_class )
    return

if( 'AMEX' == TXN.firma.trim() )
{
 res = SVC.mms.findByMidTieto(TXN.merchantid.trim(), TXN.trans_date)
 TXN.clishe_no = res.clishe
}
