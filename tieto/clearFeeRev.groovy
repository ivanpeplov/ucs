/**
 * Created by konovalov_sv on 07.12.2015.
 */

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

if( ["DCS","MDC","REV2MMS","TMS2MMS"].contains(TXN.proc_class) &&
        ["010","030","040"].contains(TXN.trans_type) &&
        (SVC.REF.blackRevHOs.get( TXN.ho_id ) == null)
)
    TXN.fee_amount = 0.0;
