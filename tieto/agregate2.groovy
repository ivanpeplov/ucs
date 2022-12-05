import org.springframework.dao.DuplicateKeyException

if( ['CBK', 'FEE', 'TMS2MMS'].contains(TXN.source_code?.trim()) ) {
    return
}

if( TXN.proc_date?.compareTo( (new GregorianCalendar(2018,4,21)).getTime() ) < 0 )
 return
 
if( SVC.REF.whiteCrdMerchants.get( TXN.merchantid ) || SVC.REF.whiteCrdHOs.get( TXN.ho_id ) )
 return

if ((TXN.clishe_no?.startsWith("855")) && (TXN.firma == 'AMEX'))
    return

Limit = 29000
AgMdf = 75
Ratio = 0.5

if( TXN.rej_status?.trim() == 'RDY' )
{   //check by agregate result

    if( TXN.trans_type == '010' )
    {
     resCr = SVC.REF.AgAmountGet.get( TXN.trans_type, TXN.merchantid, TXN.proc_date, AgMdf )?.values().iterator().next()
     resDb = SVC.REF.AgAmountGet.get( '000', TXN.merchantid, TXN.proc_date, AgMdf )?.values().iterator().next()
     if( resDb == null || resDb == 0 )
      resDb = 0.0001
     if( resCr == null )
     {
        TXN.rej_status = "AGAMNTABST"
        TXN.rej_comment = "Agregate amount absent"
        return ERROR;
     }
     else
     if( resCr > Limit && resCr.div(resDb) > Ratio ) /* credit value limit in rubles */
     {
        try
        { SVC.REF.blackCrdMerchByDate.put( TXN.merchantid.trim(), TXN.proc_date, 'FC4' ) }
        catch( DuplicateKeyException dke ) {}
            
        //TXN.rej_status = "AGCRDBIG"
        //TXN.rej_comment = "Agregate credit amount too big"
        return //REJECTED;
     }
    }
}
else
{
    // agregate amount for credit and debet by mid
    if( ['010','000'].contains( TXN.trans_type ) )
    {
     R = 3
     REP:
     result = SVC.REF.AgAmountSet.get( TXN.id, TXN.trans_type, TXN.merchantid, TXN.proc_date, TXN.amount, AgMdf )?.values().iterator().next()
     //if( result != 1 )
     // println( TXN.id + ' agregate result: ' + result + ' - ignored' )
     if( result != 0 && result != 1 )
     {
        R--
        if( R > 0 )
        {
         sleep((long)(3000+4000*Math.random()))
         continue REP
        }
        TXN.rej_status = "AGAMNTPUTE"
        TXN.rej_comment = "IFXE: "+result
        return ERROR;
     }
    }
    
    //credit value limit in rubles
    if( TXN.trans_type == '010' && TXN.amount > Limit )
    {
        try
        { SVC.REF.blackCrdMerchByDate.put( TXN.merchantid.trim(), TXN.proc_date, 'FC4' ) }
        catch( DuplicateKeyException dke ) {}
        
        //TXN.rej_status = "CRD"
        //TXN.rej_comment = "Credit amount too big"
        return //REJECTED;
    }        
}