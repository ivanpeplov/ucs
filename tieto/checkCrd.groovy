if( TXN.trans_type == '010' && SVC.REF.blackCrdHOsPP.get( TXN.ho_id ) )
    TXN.rej_codes = 'FC1FC4'
