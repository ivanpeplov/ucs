if( TXN.rej_status?.trim() == 'RDY' ) return

// Replace ENTRY_MODE=NULL to ' '
if( TXN.entry_mode == null ) {
 TXN.entry_mode = ' '
}

// Replace RUR to RUB
if( TXN.curr_code == 'RUR' ) {
    TXN.curr_code = 'RUB'
}

// Replace EXPARE_DATE=0 to ' '
if( !TXN.exp_date?.matches('(0[1-9]|1[0-2])(\\d{2})') )
  TXN.exp_date = ''

if( ['CBK', 'FEE'].contains(TXN.source_code?.trim()) ) {
    return
}

// Additional checking

TXN.c_audit_no = '100' + TXN.proc_date.format('DDD')


// Replace ENTRY_MODE for PPT (COMSTAR, MGTS)
if( TXN.clerc_id == 'PPT' ) {
    TXN.entry_mode = '90'
}

// REJECT Malina cards
if( TXN.card_no_dec.startsWith( '639300' ) ) {
    TXN.rej_status = "MALINA"
    return DELETED;
}

// Remove non-payment Raiffeisen cards from BP
if( TXN.ho_id == '01938' && TXN.card_no_dec.startsWith( '422287' ) && TXN.authsource == '4' ) {
    TXN.rej_status = "RAIFFEISEN"
    return DELETED;
}

//Все транзакции по договору 20020 снимаются с  процессинга со STATUS=’EXP’.
if( TXN.ho_id == '20020' ) {
    TXN.rej_status = "EXP"
    return DELETED;
}

//Все транзакции по MID 3033046001 снимаются с  процессинга со STATUS=’EXP’.
if( TXN.merchantid == '3033046001')
{  TXN.rej_status = "EXP"
   TXN.rej_comment = "Blacked transaction rule"
   return DELETED;
}


// Set ID_METHOD for credit
if( TXN.trans_type == '010' )
  {
    if( ['5','6','7'].contains(TXN.eci) )   
     TXN.id_method = 'E'
    else
    if( ['9'].contains(TXN.eci) ) 
     TXN.id_method = 'R'
    else 
     TXN.id_method = '@'   
  }

// Set ENTRY_MODE=01 for ENTRY_MODE пустое ( пробелы) for credit
if( [ '010' ].contains( TXN.trans_type ) &&
    [ null, 0 ].contains( TXN.entry_mode?.trim().length() )
  )
    TXN.entry_mode = '01'


// REJECT LOTY cards with trans_type == 'I35' || TXN.trans_type == 'I40'
if( TXN.trans_type == 'I35' || TXN.trans_type == 'I40' )
{
    TXN.rej_status = "NOUSED"
    return DELETED;
}
