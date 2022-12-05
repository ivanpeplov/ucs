if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

if( TXN.rej_status?.trim() == 'RDY' ) return

// Не проверяем реверсалы из DCS
if( TXN.source_code?.trim() == 'DCS' && ["030", "040"].contains(TXN.trans_type)) {
    return
}

// Amex

if( TXN.firma == 'AMEX' && TXN.trans_type != 'D08' ) {

//    println( 'AMEX start ' + TXN.id )
//    println( 'CT_ID: ' + TXN.firma )

    amexInfo = SVC.mms.findByMidTieto( TXN.merchantid?.trim(), TXN.trans_date )

    // Use today's date if rej_status = SND
    if( amexInfo.error && TXN.rej_status?.trim() == 'SND' ) {
        amexInfo = SVC.mms.findByMidTieto( TXN.merchantid?.trim(), new java.sql.Date( new Date().getTime() ) )
    }

    if( amexInfo.error ) {
        TXN.rej_status = 'NOCLI'
        TXN.rej_comment = amexInfo.error
        return REJECTED
    }

    TXN.clishe_no =  amexInfo.clishe

//    println "AMEX id: $TXN.id, clishe: $TXN.clishe_no"

    if( ! (TXN.clishe_no?.startsWith( "959") || TXN.clishe_no?.startsWith( "929") || TXN.clishe_no?.startsWith( "829") || TXN.clishe_no?.startsWith( "855" )) ) {
        TXN.rej_status = 'N959'
        return REJECTED
    }

    TXN.br_latname = amexInfo.latname
    TXN.br_latcity = amexInfo.latcity
    TXN.br_country = amexInfo.country
    TXN.br_zip  = amexInfo.zip
    TXN.mbr_id  = amexInfo.memberid
    TXN.br_currcod = amexInfo.curr_code_3c

    if( TXN.br_currcod == 'RUR' ) {
        TXN.br_currcod = 'RUB'
    }

    TXN.mcc_id  = amexInfo.mcc
    TXN.mcc_name = amexInfo.mcc_name

//    TXN.accept_id   = TXN.merchantid
//    TXN.ct_id       = TXN.firma
//    TXN.proc_class  = 'MDC'
    TXN.authsource  = '1'


    batchId =  TXN.ho_id.substring( 1, 5 ) + TXN.proc_date.format( 'ddMM')
    if( TXN.trans_type == '000' ) {
        TXN.batch_id = '0' + batchId
    }
    if( TXN.trans_type == '010' ) {
        TXN.batch_id = 'C' + batchId
    }




/*
    cp_util REPLACE amex_out.dbf ACCEPT_ID MERCHANTID
    cp_util REPLACE amex_out.dbf CT_ID FIRMA
    cp_util REPLACE amex_out.dbf CLERK_ID CLERC_ID
    cp_util REPLACE amex_out.dbf BATCH_ID substr(HO_ID,2,4)+substr(dtoc(PROC_DATE),1,2)+substr(dtoc(PROC_DATE),4,2)
    cp_util REPLACE amex_out.dbf BATCH_ID '0'+alltrim(BATCH_ID) TRANS_TYPE='000'
    cp_util REPLACE amex_out.dbf BATCH_ID 'C'+alltrim(BATCH_ID) TRANS_TYPE='010'
    cp_util REPLACE amex_out.dbf PROC_CLASS 'MDC'
    cp_util REPLACE amex_out.dbf AUTHSOURCE '1'

    Из базы MMS ( сейчас c:\setlhost.201\tables\mer_arc.dbf)

     replace TERM_ID with vSE_TERM_ID
     replace BR_LATNAME with vSE_NAME
     replace BR_LATCITY with vSE_LOCAT
     replace BR_COUNTRY with vSE_COUNTRY
     replace BR_ZIP with vSE_ZIPCODE
     replace MBR_ID with vMBR_ID
     replace MCC_ID with vMCC_ID
     replace BR_CURRCOD with vCURR_CODE
     mcc=vMCC_ID+CT_ID

    Из базы MMS ( сейчас c:\setlhost.201\tables\mcc_file.dbf)

     replace MCC_NAME with vMCC_NAME
    replace BR_CAT_COD with vBR_CAT_COD

     */
}

