def Reject( txn, comment )
{
 txn.rej_status = 'ERRMCBKW'
 txn.rej_comment = comment
 return REJECTED
}

if( TXN.source_code?.trim() != 'CBKW2MMS' )
 return
 
if( TXN.rej_status?.trim() == 'RDY' )
 return READY
 
TARGET = 'MLS'
 
if( TXN.rej_status?.trim() == 'SND' )
 return PROCESSED

for( String field: ['amount','ho_id','card_no_dec','merchantid','trans_date', 'trans_type'] )
{
  val = TXN.getFields().get(field)
  if( [null,''].contains(val?.toString()?.trim()) )
   return Reject( TXN, 'Empty field: '+field )
}

res = SVC.mms.findByMidTieto(TXN.merchantid.trim(), TXN.trans_date)
if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }
    
if( TXN.amount <= 0 )
 return Reject( TXN, 'Wrong amount' )
    
if( TXN.ho_id != res.ho_id )
 return Reject( TXN, 'Different HO: '+TXN.ho_id +'/'+ res.ho_id )
    
TXN.merchantid = res.merch_id
TXN.clishe_no = res.clishe
TXN.ho_id = res.ho_id
TXN.id_fi = res.id_fi
TXN.curr_code = res.curr_code_3c
TXN.odd_ccy_rec = res.curr_code_3c

TXN.entity_id = 'MLS' + TXN.proc_date.format('yyMMdd') + TXN.id.toString().padLeft( 11, '0' ).take(11)

cardno = TXN.card_no_dec.trim().replace('*','0')
range = SVC.baseArdef.findRange( cardno )
if( !range )
 range = SVC.ardef.findRange( cardno )

if( range ) 
{
 TXN.firma = ( range.paymentsystem?.trim() == "EURO"?"MAST": range.paymentsystem?.trim())
 TXN.subdomain = range.subdomain?.trim()
}
else
if( ['',null].contains( TXN.firma?.trim() ) )
  return Reject( TXN, 'Unknown card range' )

TXN.vt_ct_id = TXN.firma

TXN.fee_amount = 0
TXN.proc_class = 'MLS'
TXN.odd_amnt_rec = TXN.amount
TXN.clerc_id= 'TPR'
TXN.status = 'CBK'

if( '010'.equals( TXN.trans_type ) )
 TT = 'CMB'
else
if( '000'.equals( TXN.trans_type ) )
 TT = 'BMB'
else
 return Reject( TXN, 'Wrong trans_type' )

TXN.acc_key = TXN.proc_class + TT + TXN.proc_date.format("ddMMyy") + TXN.vt_ct_id.trim().padRight(4,'0') + (TXN.acc_key?.trim()?.length()==20?TXN.acc_key.substring(16,20):'0000')
 
TXN.batch_id = TT + TXN.proc_date.format("DDD") + TXN.batch_id.drop(6)
 
if( TXN.rej_status?.trim() != 'SND' )
{
 //println( TXN.getFields() )
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), ['source_id','amount', 'ho_id', 'trans_date', 'trans_type', 'merchantid', 'firma'] as Set, true );

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
