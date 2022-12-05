if( TXN.source_code?.trim() != 'REV2MMS' )
 return
 
if( TXN.rej_status?.trim() == 'RDY' )
 return
 
TARGET = ['MDC_PROC','MDC_CALC','TIETOREV']
 
if( TXN.rej_status?.trim() == 'SND' )
 return PROCESSED
 
def Reject( txn, comment )
{
 txn.rej_status = 'ERRREV'
 txn.rej_comment = comment
 return REJECTED
}

if( !["030","040"].contains(TXN.trans_type) )
    return Reject( TXN, 'trans_type' )

TXN.curr_code='RUB'
TXN.vt_ct_id=TXN.firma
TXN.proc_class='DCS'
TXN.clerc_id = 'TVR'
TXN.entity_id = 'DCS' + TXN.proc_date.format('yyMMdd') + TXN.id.toString().padLeft( 11, '0' ).take(11)

// --comm clwh_event_id
//if( TXN.clwh_event_id == null ) 
// TXN.clwh_event_id = (TXN.odd_msg_id == null)?TXN.entity_id:TXN.odd_msg_id


TXN.acc_key = TXN.proc_class + TXN.trans_type + TXN.proc_date.format("ddMMyy") + TXN.vt_ct_id.trim().padRight(4,'0') + (TXN.acc_key?.trim()?.length()>=20?TXN.acc_key.substring(16,20):'0000')

    res = SVC.mms.findByMidTieto(TXN.merchantid?.trim(), TXN.trans_date)
    if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }
    else {
        TXN.id_fi = res.id_fi
    }

 fldup = [  'auth_code', 'amount','trans_date', 'exp_date','merchantid', 'clishe_no', 'br_id',
            'curr_code', 'ccy_exp', 'trans_time', 'trans_type', 'firma', 'ho_id', 'mdc_trans_type',
            'mdc_trans_id', 'ind_data', 'ext_data','slip_no', 'clerc_id','authsource', 'id_method',
            'entry_mode', 'capability', 'devicetype', 'pin_cpblty',
            'source_id'
          ] as Set
          
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), fldup, true )
 
for ( t in duplicates )
    return Reject( TXN, "DBL: " + t.proc_date + ", id: " + t.trans_id )

return PROCESSED