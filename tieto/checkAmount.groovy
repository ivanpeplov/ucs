import org.springframework.dao.DuplicateKeyException

if( TXN.rej_status?.trim() == 'RDY' ) return

// Amount less 0.5 VISA return
if( TXN.amount <= 0.50 && TXN.amount > 0 &&
    TXN.firma?.trim() == 'VISA'
  )
 try
 { SVC.REF.blackTransByDate.put( TXN.id, TXN.proc_date, 'FC5' ) }
 catch( DuplicateKeyException dke ) {}


if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// Не проверяем операции с rej_status = SND
if( TXN.rej_status?.trim() == 'SND' ) {
    return
}

// Не проверяем реверсалы из DCS
if( TXN.source_code?.trim() == 'DCS' && ["030", "040"].contains(TXN.trans_type)) {
    return
}


// Amount checking

TXN.rej_status = null

if( TXN.amount < 0 ) {
    TXN.rej_status = 'BIG'
}

// Check Gold Crown
if( TXN.source_code?.trim() == 'KRNA' ) {
    if( TXN.amount <= 0) {
        TXN.rej_status = 'BIG'
        return REJECTED
    } else {
        return
    }
}

// Exeption HO
if( ['A1479','A7820','A8266','C0046','C0057'].contains( TXN.ho_id ) )
 return

// Exeptiom MID
if( ['3083640001', '3097878001', '3083640002', '3089981001', '3089981003', '3094303001', '3094303002', '3094128001', '3094128002', '4949584001', '4948896001', '3077501001', '3077501002', '3097878001', '4943698001', '3041822001', '4992343001', '4992343002', '3089981001', '3089981003', '4992787001', '3092787002', '3083640001', '3083640002', '3094128001', '3094128002', '2656421001', '2656421002', '3099681001', '3099681002', '3006692001', '3006692002', '9710885001', '9711856001', '9711856002', '9711272001', '9711479001', '9710074001', '3038760001', '3098373001', '3048097001', '9713537001', '9711856003', '9715299001', '9716157001', '9714701001', '9714319001', '9715299001', '9716483001', '9715710002', '3043698002', '7392958001','3088529001'].contains( TXN.merchantid )  )
 return
// Exeption HO only for trans_type='000'
if( ['92343', '56421', '99681', '92787', '77501'].contains( TXN.ho_id ) && ["000"].contains(TXN.trans_type) )
 return

if( TXN.ho_id == '02844' && TXN.amount > 7000000 ) {
    TXN.rej_status = 'BIG'
}

if( TXN.ho_id != '02844' && TXN.amount > 5000000 ) {
    TXN.rej_status = 'BIG'
}

// For HO 33056 Sum 20000000
if( ['9903703198'].contains(TXN.merchantid) && TXN.amount < 20000001 && TXN.rej_status == 'BIG' ) {
    TXN.rej_status = ''
}

if( TXN.rej_status == 'BIG' ) {
    return REJECTED;
}