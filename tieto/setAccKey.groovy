/**
 * Created by vadik on 16.10.2014.
 */

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// Set proc_class
if( TXN.proc_class == null ) // <- если вдруг значение ещё не установлено
TXN.proc_class = 'MDC'

// if( TXN.source_code?.trim() == 'DCS' /* && ["000", "010"].contains(TXN.trans_type) */  ) {
//    TXN.proc_class = 'DCS'
//}

if( ["AMEX", "DCL"].contains(TXN.firma?.trim())) {
    if( !'AMEX'.equals(ENV?.subct) )
    return
}

if( ENV.feeInfo == null ) {
    return
}

// Set batch_id
if( ENV.feeInfo.type.name() == 'HO' ) {
    TXN.batch_id = "HO_COMM"
}

if( ENV.feeInfo.type.name() == 'BR' ) {
    TXN.batch_id = "MER_COMM"
}

if( ENV.feeInfo.type.name() == 'GRP' ) {
    TXN.batch_id = "GRP_COMM"
}

// ignore BSP acc_key
if( TXN.acc_key?.startsWith('BSP') ) {
    if( TXN.id_fi == '900007' )
     TXN.acc_key = 'MDC'+TXN.acc_key.substring(3);   
    return
}

//println "ENV: ${ENV}"

// set real CT for non Group fee type
if( ENV.feeInfo.type.name() != 'GRP' && ["ABVI", "ABMC"].contains(TXN.vt_ct_id) ) {
    TXN.vt_ct_id = TXN.firma
}


// Fill acc_key
// HO fee
if( ENV.feeInfo.type.name() == 'HO' ) {
    TXN.acc_key = TXN.proc_class + TXN.trans_type + TXN.proc_date.format("ddMMyy") + TXN.vt_ct_id.trim().padRight(4,'0') + '0000'
}

if( ENV.feeInfo.type.name() == 'BR' ) {
    TXN.acc_key = TXN.proc_class + TXN.trans_type + TXN.proc_date.format("ddMMyy") +
            TXN.vt_ct_id.trim().padRight(4,'0') + ENV.feeInfo.comm.movePointRight(5).toString().padLeft( 4  , "0" )

//    println( ENV.feeInfo.comm )
//    println( ENV.feeInfo.comm.movePointRight(5) )
//    println( ENV.feeInfo.comm.movePointRight(5).toString().padLeft( 4, "0" ) )
//    println("ACC_KEY = ${TXN.acc_key}")
//    println(" ${TXN.id}IF BR")
}

if( ENV.feeInfo.type.name() == 'GRP' ) {
    TXN.acc_key = TXN.proc_class + TXN.trans_type + TXN.proc_date.format("ddMMyy") + TXN.vt_ct_id.trim().padRight(4,'0') + ENV.feeInfo.grp_id.substring(6, 10)
//    println(" ${TXN.id}, IF GRP")
}

if( [ '33056', '33058' ].contains( TXN.ho_id ) )
 TXN.acc_key = TXN.acc_key.substring(0, TXN.acc_key.length()-4)+'0000'

