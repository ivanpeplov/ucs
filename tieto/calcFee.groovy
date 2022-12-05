/**
 * Created by vadik on 16.10.2014.
 */

if( ['CBK', 'FEE', 'REV2MMS'].contains(TXN.source_code?.trim()) ) {
    return
}

if( ["AMEX", "DCL"].contains(TXN.firma.trim())) {
    if( !'AMEX'.equals(ENV?.subct) )
    return
}

if( SVC.REF.skipScript.get( this.class.getName().toUpperCase().replace('.','_').split("_")[0], "HOINLIST", "TOZERO", "FEE_AMOUNT" )?.cond_value?.split(",")?.contains( TXN.ho_id.trim() ) )
{
    TXN.fee_amount = 0
    return
}

def abdec( ct )
{
 if( ct != null )
 switch( ct )
 {
  case 'ABVI': ct = 'VISA'; break;
  case 'ABMC': ct = 'MAST'; break;
 }
 return ct;
}

if( ['REV2MMS'].contains(TXN.source_code?.trim()) )
 proc_date = TXN.send_date
else
 proc_date = TXN.proc_date

if (TXN.merchantid?.trim()) {


    ct = TXN.vt_ct_id
    if( ct == "EURO") {
        ct = "MAST"
    }

    res = SVC.mms.findMerchCommTietoToday(TXN.merchantid.trim(), abdec( ct ), proc_date)

    // find once more with real card type
    if( res.error ) {
        ct = TXN.firma
        if (ct == "EURO") {
            ct = "MAST"
        }
        res = SVC.mms.findMerchCommTietoToday(TXN.merchantid.trim(), abdec( ct ), proc_date)
        TXN.vt_ct_id = TXN.firma?.trim()

    }

//    println "Run findMErchComm: ${TXN.merchantid}, $ct, ${TXN.proc_date}, Res: ${res}"

    if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = "Fee error: " + res.error
        return REJECTED
    } else {
        ENV.feeInfo = res // запоминаем информацию по комиссии

        TXN.owner_id = (['33056','33058','33061'].contains(TXN.ho_id)||ENV.feeInfo.grp_id==null)?'':ENV.feeInfo.grp_id;

            TXN.fee_amount = TXN.amount * res.comm
        

//        println "id: ${TXN.id}, Amount: ${TXN.amount}, FEE: ${res.comm}, Famount: ${TXN.fee_amount}"
    }
}