if( TXN.rej_status?.trim() == 'RDY' ) return

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

/**
 * Created by vadik on 15.10.2014.
 * Устанавливаем виртуальный кардтайп
 */

//*** HO_ID=05501, 05502, 03817 *** for (substr(CARD_NO,1,10)='5189010002').AND.(HO_ID='05501'  || HO_ID='05502'  || HO_ID='03817')

TXN.vt_ct_id = TXN.firma?.trim()

// TIAP
if( ['05501', '05502', '03817'].contains(TXN.ho_id) && TXN.card_no_dec?.startswith('5189010002') ) {
    TXN.vt_ct_id = "TIAP"
}

// ABVI && ABMC
// ищем ранг в справочнике

abRange = SVC.abArdef.findRange( TXN.card_no_dec )

if( abRange && (TXN.id_fi == '900007' 
    &&  TXN.firma?.trim() != 'MIR' )   // -  Temporally close MIRD
  ) {
    TXN.vt_ct_id = abRange.paymentsystem?.trim()
    ENV.abRange = abRange
}