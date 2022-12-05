if( TXN.rej_status?.trim() == 'RDY' ) return

// Не проверяем операции с rej_status = SND
if( TXN.rej_status?.trim() == 'SND' ) {
    return
}

// Не проверяем на дубли операции из FRB_OUT
if( TXN.source_code.trim() == 'FRB_OUT' ) {
    return
}

// Не проверяем на дубли для определенных ХО
if( TXN.trans_type == '010' && ['05615','19182'].contains( TXN.ho_id ) ) {
    return
}

// Comment 31.08.2016 Не проверяем на дубли не кредитовые операции LOTY, кроме пришедших от внешних источников
//if( TXN.firma == 'LOTY' && TXN.trans_type != '010' && TXN.source_code.trim() != 'EXTMDC' ) {
//    return
//}

// Coment 12/09/2017 ntk-283 Не проверяем реверсалы из DCS
// if( TXN.source_code?.trim() == 'DCS' && ["030", "040"].contains(TXN.trans_type)) {
//    return
//}




// Skip Gold Crown
if( TXN.source_code?.trim() == 'KRNA') {
    return
}

boolean void_flag =
           ( TXN.source_code.trim() == 'VOID'
        || ( TXN.mdc_trans_type != null && !['D','C'].contains( TXN.mdc_trans_type ) )
        || ( TXN.mdc_trans_type == 'D' && TXN.mdc_trans_id == '00' ) )

// Не проверяем на дубли не кредитовые операции VOID
if( void_flag && TXN.trans_type != '010' && TXN.firma != 'LOTY' ) {
    return
}

if( TXN.source_code?.trim() == 'DCS' && ["030", "040"].contains(TXN.trans_type) )
{
 fldup = [  'auth_code', 'amount','trans_date', 'exp_date','merchantid', 'clishe_no', 'br_id',
            'curr_code', 'ccy_exp', 'trans_time', 'trans_type', 'firma', 'ho_id', 'mdc_trans_type',
            'mdc_trans_id', 'ind_data', 'ext_data','slip_no', 'clerc_id','authsource', 'id_method',
            'entry_mode', 'capability', 'devicetype', 'pin_cpblty'
          ] as Set
          
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), fldup, false );
}
else

if( ['FEE'].contains( TXN.proc_class ) )
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), ['proc_date','proc_class','amount','curr_code','trans_type','trans_date','membertext'] as Set, false );
else if(TXN.in_file_name.startsWith('CARP') && (TXN.trans_type == '010'))
  duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.getFields(), ['auth_code','trans_type','amount','curr_code','slip_no'] as Set, false );
else
 duplicates = SVC.transFinder.findDuplicates( TXN.id, TXN.card_no_dec, TXN.auth_code, TXN.trans_type, TXN.amount, TXN.curr_code );


// No duplicates found
if( duplicates.size() == 0 ) {
    return;
}



// Go through found duplicates
// TXN.card_no_dec, TXN.auth_code, TXN.trans_type, TXN.amount, TXN.curr_code are the same already
for ( t in duplicates ) {

    if( ['CBK','FEE'].contains( TXN.proc_class ) &&
       ( t.proc_date != TXN.proc_date ||
         t.proc_class != TXN.proc_class
       )
      ) continue;
      
    if( ['FEE'].contains( TXN.proc_class ) )
    {
        TXN.rej_status = "DBL";
        TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
        return REJECTED;
    }
    
    // AVIA пропускаем если ind_data не совпадает
    if( TXN.ind_data?.startsWith('01') && t.ind_data?.trim() != TXN.ind_data?.trim() ) {
        continue
    }

    if( t.merchantid == TXN.merchantid
     && t.slip_no == TXN.slip_no
     && t.trans_date == TXN.trans_date
     && t.misc_info == TXN.misc_info
    ) {
        TXN.rej_status = "DBL";
        TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
        return REJECTED;
    }

/*
    if( t.merchantid == TXN.merchantid
     && t.trans_date == TXN.trans_date
     && ![ 'DAYTRANS', 'EXTMDC', 'ALFA' ].contains( TXN.source_code.trim() )
    ) {
        TXN.rej_status = "SSP";
        TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
        return REJECTED;
    }
*/

    // Для внешних источников учитываем имя файла и номер слипа
    if( t.merchantid == TXN.merchantid
     && t.trans_date == TXN.trans_date
     && t.slip_no == TXN.slip_no
     && t.in_file_name != TXN.in_file_name
     && [ /*'EXTMDC', */ 'DAYTRANS' ].contains( TXN.source_code.trim() )
    ) {
        TXN.rej_status = "SSP";
        TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
        return REJECTED;
    }

    if( t.merchantid == TXN.merchantid
     && t.trans_date == TXN.trans_date
     && t.ext_data == TXN.ext_data
     && t.in_file_name != TXN.in_file_name
     && [ /*'EXTMDC',*/ 'DAYTRANS' ].contains( TXN.source_code.trim() )
    ) {
        TXN.rej_status = "SSP";
        TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
        return REJECTED;
    }


   // Historical
    if( t.proc_date < TXN.proc_date ) {
    }
    // Today
    else {
       // --- Check only for IBIS (11951) ---  для договора 11951
        if( TXN.ho_id == '11951' ) {
            TXN.rej_status = "DBL";
            TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
            return REJECTED;
        }
        // rem --- Check only PCPOS_ID 01906, 09414 (IMAGEPOINT) --- для  договоров 01906, 09414
        if( ['01906','09414' ].contains( TXN.ho_id ) && t.merchantid == TXN.merchantid ) {
            TXN.rej_status = "DBL";
            TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
            return REJECTED;
        }

        // --- DOUBLES CONTROL FOR DAYTRANS.DBF --- где не заполнено TRANS_TIME
        if( t.slip_no == TXN.slip_no
         && t.trans_date == TXN.trans_date
         && TXN.trans_time.trim() == ""
        ) {
            TXN.rej_status = "DBL";
            TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
            return REJECTED;
        }

        // --- Check only PCP and WWW txns with filled TRANS_TIME --- где заполнено TRANS_TIME
        // (кроме кредитовых транзакций по договору 05615, которые исключаются из этой проверки).
        if( [ 'PCP', 'WWW' ].contains( TXN.clerc_id )
         && ( TXN.trans_time.trim() != "" && TXN.trans_time.trim() != '00:00:00' )
         && t.trans_date == TXN.trans_date
         && t.trans_time == TXN.trans_time
         && !( TXN.ho_id == '05615' && TXN.trans_type == '010' )
        ) {
            TXN.rej_status = "DBL";
            TXN.rej_comment = "" + t.proc_date + ", id: " + t.trans_id
            return REJECTED;
        }

    }

}

