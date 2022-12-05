if( TXN.pin_cpblty != '1' && ( 'P' == TXN.id_method || '1' == TXN.devicetype ) )
 TXN.pin_cpblty = '1'

if( TXN.pin_cpblty == null || TXN.pin_cpblty.trim().isEmpty() )
 TXN.pin_cpblty = '0'