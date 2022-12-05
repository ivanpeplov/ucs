def Reject( txn, comment )
{
 txn.rej_status = 'ERRTDCS'
 txn.rej_comment = comment
 TARGET = []
 return REJECTED
}

if( TXN.source_code?.trim() != 'TMS2MMS' )
 return
 
if( TXN.rej_status?.trim() == 'RDY' )
 return
 
TARGET = ['DCS', 'MDC_CALC', 'TIETO']
 
if( TXN.rej_status?.trim() == 'SND' )
 return PROCESSED

for( String field : ['rep_date','trans_date','card_no_dec','merchantid'] )
{
  val = TXN.getFields().get(field)
  if( [null,''].contains(val?.toString()?.trim()) )
   return Reject( TXN, 'Empty field: '+field )
}

if( !['010','000'].contains( TXN.trans_type ) )
 return Reject( TXN, 'Wrong trans_type' )

res = SVC.mms.findByMidTieto(TXN.merchantid.trim(), TXN.trans_date)
if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }
    
TXN.term_id = res.term_id
TXN.clishe_no = res.clishe
TXN.ho_id = res.ho_id
TXN.id_fi = res.id_fi
TXN.curr_code = res.curr_code_3c
TXN.odd_ccy_rec = res.curr_code_3c

cardno = TXN.card_no_dec.trim()//.replace('*','0')
range = SVC.baseArdef.findRange( cardno )
if( !range )
 range = SVC.ardef.findRange( cardno )

if( !range ) 
 return Reject( TXN, 'Unknown card range' )

TXN.firma = range.paymentsystem?.trim()
TXN.vt_ct_id = TXN.firma
TXN.subdomain = range.subdomain?.trim()


TXN.clerc_id = 'TMS'
TXN.proc_class = 'DCS'
TXN.capability = ' '
TXN.entry_mode = '01'
TXN.id_method = '@'
TXN.devicetype = ' '
TXN.pin_cpblty = ' '
TXN.entity_id = 'TMS' + TXN.proc_date.format('yyMMdd') + TXN.id.toString().padLeft( 11, '0' ).take(11)
TXN.bs_id = SVC.REF.HOtoBSbyDate.get( TXN.ho_id, TXN.proc_date)?.get("clerk_id")


if( TXN.amount == 0 )
 return Reject( TXN, 'Wrong amount' )
 
//println( TXN.getFields() )
duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), ['source_id','amount', 'auth_code', 'trans_date', 'trans_type', 'term_id', 'odd_entry_ref'] as Set, true );

 for ( t in duplicates )
 {
     if( t.in_file_name == TXN.in_file_name )
      continue;
      
     TXN.rej_status = "DBL";
     TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
     return REJECTED
 }
 
return PROCESSED

