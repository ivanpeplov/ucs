
import java.lang.NumberFormatException

if( TXN.rej_status?.trim() == 'RDY' ) return

// Не проверяем операции с rej_status = SND
if( TXN.rej_status?.trim() == 'SND' && TXN.firma ) {
    return
}

// Skip Gold Crown
//if( TXN.source_code?.trim() == 'KRNA') {
//    return
//}

//if(TXN?.card_no_dec.contains('***'))
// return

try
{ range = SVC.REF.oldCards.get( new BigDecimal( TXN.card_no_dec.trim() ) ) }
catch( NumberFormatException nfe )
{  
    TXN.rej_status = 'WRONGCNFMT';
    return REJECTED;
}
if( !range )
 range = SVC.baseArdef.findRange( TXN.card_no_dec )
if( !range )
 range = SVC.ardef.findRange( TXN.card_no_dec )

if( !range ) {
    TXN.rej_status = 'UNC';
    return REJECTED;
}

TXN.firma =  range.paymentsystem?.trim()
TXN.subdomain = range.subdomain?.trim()
