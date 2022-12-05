def Reject( txn, comment )
{
 txn.rej_status = 'ERRMCBK'
 txn.rej_comment = comment
 return REJECTED
}

if( TXN.source_code?.trim() != 'CBK2MMS' )
 return
 
if( TXN.rej_status?.trim() == 'RDY' )
 return READY
 
TARGET = 'MLS'
 
if( TXN.amount < 0 )
 TXN.amount = -TXN.amount

if( TXN.rej_status?.trim() == 'SND' )
 return PROCESSED

for( String field: ['amount','batch_id','card_no_dec','curr_code','term_id','trans_type'] )
{
  val = TXN.getFields().get(field)
  if( [null,''].contains(val?.toString()?.trim()) )
   Reject( TXN, 'Empty field: '+field )
}

res = SVC.mms.findByMidTieto('T:'+TXN.term_id.trim(), TXN.trans_date)
if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }
    
if( TXN.ho_id != res.ho_id )
 return Reject( TXN, 'Different HO: '+TXN.ho_id +'/'+ res.ho_id )

TXN.merchantid = res.merch_id
TXN.clishe_no = res.clishe
TXN.ho_id = res.ho_id
TXN.id_fi = res.id_fi
TXN.term_id = res.term_id

TXN.entity_id = 'MLS' + TXN.proc_date.format('yyMMdd') + TXN.id.toString().padLeft( 11, '0' ).take(11)

cardno = TXN.card_no_dec.trim().replace('*','0')
range = SVC.baseArdef.findRange( cardno )
if( !range )
 range = SVC.ardef.findRange( cardno )

if( !range ) 
 Reject( TXN, 'Unknown card range' )

TXN.firma = ( range.paymentsystem?.trim() == "EURO"?"MAST": range.paymentsystem?.trim())
TXN.vt_ct_id = TXN.firma
TXN.subdomain = range.subdomain?.trim()

TXN.fee_amount = 0
TXN.proc_class = 'MLS'


if( '010'.equals( TXN.trans_type ) )
 TT = '0C0'
else
if( '000'.equals( TXN.trans_type ) )
 TT = '0D0'
else
 TT = TXN.trans_type

TXN.acc_key = TXN.proc_class + TT + TXN.proc_date.format("ddMMyy") + TXN.vt_ct_id.trim().padRight(4,'0') + (TXN.acc_key?.trim()?.length()>=20?TXN.acc_key.substring(16,20):'0000')
 
if( '*********'.equals(TXN.batch_id) ) TXN.status = 'LRJ'
 else TXN.status = 'CBK'

if( TXN.rej_status?.trim() != 'SND' )
{
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), ['source_id','amount', 'arn', 'auth_code', 'merchantid', 'trans_date', 'trans_type', 'odd_entry_ref', 'a_audit_no', 'c_retr_ref', 'slip_no', 'a_retr_ref'] as Set, true );

 if( duplicates.size() != 0 )
 {
  TXN.rej_status = "DBL";
  TXN.rej_comment = "" + duplicates[0].proc_date + ", id: " + duplicates[0].trans_id
  return REJECTED
 }
}

res = SVC.mms.findMerchCommTietoToday(TXN.merchantid.trim(), TXN.firma, null)
TXN.owner_id = (['33056','33058','33061'].contains(TXN.ho_id)||res.grp_id==null)?'':res.grp_id

if( ['33056','33058','33061'].contains(TXN.ho_id) &&
    !TXN.card_no_dec.trim().matches( '\\d{13,19}')
  )
{
        TXN.rej_status = "FBMASKPAN"
        TXN.rej_comment = 'card_no is masked'
        return REJECTED
}
return PROCESSED