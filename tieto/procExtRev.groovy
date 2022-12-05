
if( TXN.source_code?.trim() != 'EXTREV' )
 return
 
if( TXN.rej_status?.trim() == 'RDY' )
 return READY
 
TARGET = 'EXTREV'
 
def Reject( txn, comment )
{
 txn.rej_status = 'ERRREV'
 txn.rej_comment = comment
 return REJECTED
}

if( !["030","040"].contains(TXN.trans_type) )
    return Reject( TXN, 'trans_type' )
 
if(TXN.merchantid?.trim()?.length() < 1   )
    return Reject( TXN, 'merchantid' )

if( TXN.card_no_dec?.trim()?.length() < 1 )
    return Reject( TXN, 'card_no_dec' )

if( TXN.amount <= 0 )
    return Reject( TXN, 'amount' )
    
if( TXN.trans_type == '030' && TXN.auth_code?.trim()?.length() < 6 )
    return Reject( TXN, 'auth_code' )

if( TXN.trans_date == null )
    return Reject( TXN, 'trans_date' )

if( TXN.rej_status?.trim() == 'SND' )
 return PROCESSED

if( ['MIR'].contains(TXN.firma?.trim()) )
{
    (rd = Calendar.getInstance()).add(Calendar.DATE, -28);
    if( TXN.rep_date < new java.sql.Date( rd.getTimeInMillis() ) )
    {
         TXN.rej_status = 'RVOld30'
         TXN.rej_comment = 'ExtRevOld30'
         return REJECTED
    }
}
return PROCESSED

