/**
 * Task ntk-495 09.11.2018.
 */

if( TXN.rej_status?.trim() == 'RDY' ) return

if( !TXN.c_retr_ref?.matches( '\\d{12}' ) )
     TXN.c_retr_ref=null
