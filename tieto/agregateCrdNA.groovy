import org.springframework.dao.DuplicateKeyException

AgMdf = 75

if( !['MDC','DCS'].contains(TXN.source_code?.trim()) )
 return

if ((TXN.clishe_no?.startsWith("855")) && (TXN.firma == 'AMEX'))
    return

if (TXN.firma == 'AMEX' || TXN.trans_type != '010')
 return
 
ct = 'namx'

if( SVC.REF.whiteCrdMerchants.get( TXN.merchantid ) || SVC.REF.whiteCrdHOs.get( TXN.ho_id ) )
 return

def rejReturn( sts, msg )
{
 try
 { SVC.REF.blackCrdMerchByDate.put( TXN.merchantid.trim(), TXN.proc_date, 'FC1' ) }
 catch( DuplicateKeyException dke ) {}
 //TXN.rej_status = sts
 //TXN.rej_comment = msg
 return //REJECTED
}
    
if (TXN.rej_status?.trim() == 'RDY') {   //check by agregate result

     Sum = SVC.REF.AgAmountGet.get(TXN.trans_type, TXN.merchantid, TXN.proc_date, AgMdf, ct)?.values().iterator().next()
     if( Sum > 29000 )
      rejReturn( 'AGCRDBIG', 'Agregate credit amount FC1 too big' )
    
} else {    // agregate amount for credit by mid
   
            R = 3
            REP:
            result = SVC.REF.AgAmountSet.get(TXN.id, TXN.trans_type, TXN.merchantid, TXN.proc_date, TXN.amount, AgMdf, ct)?.values().iterator().next()
            if( result != 0 && result != 1 )
             {
                R--
                if( R > 0 )
                {
                 sleep((long)(300+400*Math.random()))
                 continue REP
                }
                TXN.rej_status = "AGNAMXPUTE"
                TXN.rej_comment = "IFXE: "+result
                return ERROR;
             }
            if( TXN.amount > 29000 )
             rejReturn( 'CRD', 'Credit amount FC1 too big' )
        }
