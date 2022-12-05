import org.springframework.dao.DuplicateKeyException

AgMdf = 75

if( !['MDC','DCS'].contains(TXN.source_code?.trim()) )
 return

ct = 'cles'

if( SVC.REF.whiteCLessHOs.get( TXN.ho_id ) )
 return

def rejReturn( sts )
{
 try
 { SVC.REF.blackCLessHoByDate.put( TXN.ho_id.trim(), TXN.proc_date, sts ) }
 catch( DuplicateKeyException dke ) {}
 return
}

            if(!
                ( TXN.trans_type == '000' && TXN.amount <= 1000 &&
                  ['91','07','  '].contains(TXN.entry_mode) &&
                 !['E','R','M'].contains(TXN.id_method) &&
                  [null,''].contains(TXN.auth_code?.trim()) &&
                  TXN.ext_data?.trim()?.length() > 14 && TXN.ext_data.substring(14).startsWith( 'NFC' )
                )
              ) return

if (TXN.rej_status?.trim() == 'RDY') {   //check by agregate result

     Sum = SVC.REF.AgAmountGet.get(TXN.trans_type, TXN.merchantid, TXN.proc_date, AgMdf, ct)?.values().iterator().next()
     if( Sum > 20000 )
      rejReturn( 'CL1' )
    
} else {    // agregate amount for cless by mid
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
                TXN.rej_status = "AGCLESPUTE"
                TXN.rej_comment = "IFXE: "+result
                return ERROR;
             }
        }
