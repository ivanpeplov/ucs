import org.springframework.dao.DuplicateKeyException

if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['010'].contains(TXN.trans_type) )
{

  if(  !(((SVC.REF.whiteRAuthMerchants.get( TXN.merchantid ) || SVC.REF.whiteRAuthHo.get( TXN.ho_id ) ) 
        || !SVC.REF.whiteRAuthClerc.get( TXN.clerc_id ))) 
     )
    try
    { SVC.REF.blackACodeByDate.put( TXN.id, TXN.proc_date ) }
    catch( DuplicateKeyException dke ) {}

}
