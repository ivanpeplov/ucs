if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

/**
 * Created by vendin on 28.10.2016.
 * Test: Amount Limit = 50000.00 $, (SOC \ SOD) > 0.1
 */
AgMdf = 75

def SOC = 0
def SOD = 0

def Sum = 50000
def course = 65

boolean is_AMEX = false
ct_id = null


if ((TXN.clishe_no?.startsWith("855")) && (TXN.firma == 'AMEX')) {
    is_AMEX = true
    ct_id = 'AMEX'

} else {
    return
}

if( (SVC.REF.whiteAeroMerchants.get( TXN.merchantid ) || SVC.REF.whiteAeroHOs.get( TXN.ho_id )) )
 return


if ( is_AMEX && 
     TXN.clishe_no?.startsWith("8550090112") &&
     TXN.proc_date == Date.parse("yyyy-MM-dd","2017-02-15")
   )
    return


if (TXN.rej_status?.trim() == 'RDY') {   //check by agregate result
    if( TXN.trans_type != '010' )
     return;
    if (is_AMEX) {
        //credit
        SOC = SVC.REF.AgAmountGet.get('010', TXN.merchantid, TXN.proc_date, AgMdf, ct_id)?.values().iterator().next()
        //get Debit for AMEX
        SOD = SVC.REF.AgAmountGet.get('000', TXN.merchantid, TXN.proc_date, AgMdf, ct_id)?.values().iterator().next()
        if ((SOD != null) & (SOC != null) & (SOD != 0)) {
            if (((SOC / SOD) > 0.1) & (SOC > Sum * course)) {
                TXN.rej_status = "DISCFORAMEX"
                TXN.rej_comment = "Discarding transaction for AMEX"
                return REJECTED;
            } else {
                if ((SOD == 0) & (TXN.trans_type == '010')) {
                    TXN.rej_status = "DISCFORAMEX"
                    TXN.rej_comment = "Discarding transaction because SOD equals 0 for credit transaction"
                    return REJECTED;
                }
            }
        }
    }
} else if (TXN.rej_status?.trim() == 'SND') {
} else {
    // agregate amount for credit by mid
    if (is_AMEX) {
        if ((TXN.trans_type == '010') || (TXN.trans_type == '000')) {
            R = 3
            REP:
            result = SVC.REF.AgAmountSet.get(TXN.id, TXN.trans_type, TXN.merchantid, TXN.proc_date, TXN.amount, AgMdf, ct_id)?.values().iterator().next()
            if( result != 0 && result != 1 )
             {
                R--
                if( R > 0 )
                {
                sleep((long)(3000+4000*Math.random())) 
                 continue REP
                }
                TXN.rej_status = "AGAMEXPUTE"
                TXN.rej_comment = "IFXE: "+result
                return ERROR;
             }
        }
    }
}