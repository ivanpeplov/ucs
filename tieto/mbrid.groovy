if( TXN.rej_status?.trim() == 'RDY' ) return

merchInfo = ENV?.merchInfo

if( merchInfo == null &&
    TXN.merchantid?.trim()
  )
{
    res = SVC.mms.findByMidTieto(TXN.merchantid.trim(), TXN.trans_date)
    if (res.error) {
        TXN.rej_status = "MMSERR"
        TXN.rej_comment = res.error
        return REJECTED
    }

    merchInfo = res         
}    
        
if( 'AMEX'.equals( TXN.firma ) && merchInfo != null )
{
    if( [ '100003','100004' ].contains( merchInfo.proc_id ) )
     ENV.subct = 'AEB'
    else
    if( [ '100001','100002' ].contains( merchInfo.proc_id ) )
     ENV.subct = 'AMEX'
}
        
if( 'AMEX'.equals( TXN.firma ) && merchInfo != null )
{
 TXN.mbr_id=''
 def Mbrs = [
             [ ['100001'], [ '900004', '900005', '900006' ], 'AMAERB' ],
             [ ['100002'], [ '900007' ], 'AMAEAB' ],
             [ ['100003'], [ '900004', '900005', '900006' ], 'AMAEAE' ],
             [ ['100004'], [ '900007' ], 'AMAEAE' ],
             [ ['100005'], [ '900004', '900005', '900006' ], 'AMRSRS' ],
             [ ['100006'], [ '900007' ], 'AMRSAB' ]
            ]
 for( Mbr in Mbrs )
  if( Mbr[0].contains( merchInfo.proc_id ) && Mbr[1].contains( merchInfo.id_fi ) )
  {
   TXN.mbr_id=Mbr[2]
   break;
  }
}

if( 'AMEX'.equals( TXN.firma ) && TXN.clishe_no?.startsWith("855") )
{
 if(!('100003'.equals( merchInfo.proc_id ) && '900006'.equals( merchInfo.id_fi ) ||
      '100004'.equals( merchInfo.proc_id ) && '900007'.equals( merchInfo.id_fi )
     )
   )
   {
    TXN.rej_status = "NOTAMEXPROC"
    TXN.rej_comment = merchInfo.proc_id +"|"+ merchInfo.id_fi+"|"
    return REJECTED 
   }
}
