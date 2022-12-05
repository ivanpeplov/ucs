if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

AgMdf = 75

if( TXN.rej_status?.trim() == 'RDY' )
{   //check by agregate result

    if( TXN.trans_type == '010' )
    {
     result = SVC.REF.AgAmountGet.get( TXN.trans_type, TXN.merchantid, TXN.proc_date, AgMdf )?.values().iterator().next()
     if( result == null )
     {
      /*
        TXN.rej_status = "AGAMNTABST"
        TXN.rej_comment = "Agregate amount absent"
        return ERROR;
      */
     }
     else
     if( result > 100000000 ) /* credit value limit in rubles */
     {
        TXN.rej_status = "AGCRDBIG"
        TXN.rej_comment = "Agregate credit amount too big"
        return REJECTED;
     }
    }
}
else
if( TXN.rej_status?.trim() == 'SND' )
{ }
else
{
    // agregate amount for credit by mid
    if( TXN.trans_type == '010' )
    {
     result = SVC.REF.AgAmountSet.get( TXN.id, TXN.trans_type, TXN.merchantid, TXN.proc_date, TXN.amount, AgMdf )?.values().iterator().next()
     if( result != 1 )
      println( TXN.id + ' agregate result: ' + result + ' - ignored' )
    }
}