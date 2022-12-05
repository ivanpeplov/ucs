if( TXN.rej_status?.trim() == 'RDY' ) return

def Migrated( txn )
{
 return true
}

def ToTieto(  )
{
 return true
 To = false
 
 if( ['MDC','DCS'].contains(TXN.proc_class) &&
    !Migrated( TXN )
   )
 switch( TXN.trans_type )
 {
  case '000': To = true; break; /* отправляем в Тието всегда */
  case '010': To = true; break; /* отправляем в Тието всегда */
  case '030': To = false; break; /* отправляем в КардПро всегда */
  case '040': To = false; break; /* отправляем в КардПро всегда */
 }

if( ['CBK'].contains(TXN.proc_class) &&
     ['33056','33058'].contains(TXN?.ho_id?.trim()) &&
    !TXN.card_no_dec.startsWith( '22001502' ) &&
     TXN.proc_date == Date.parse("yyyy-MM-dd","2018-10-04")
   ) To = true;

 return To
}

def Dublicate( TRG )
{
 def PriorTarget = [ [ 'MDC_PROC', 'MDC'], ['DCS','DCS'], ['CBK_PROC','CBK'], ['FEE_PROC','FEE'], ['AEB','AEB'] ]
 if( TRG instanceof List )
  T = TRG
 else
  T = [ TRG ]

 if( T.contains( 'MDC_PROC' ) || T.contains( 'DCS' ) || T.contains( 'CBK_PROC' ) || T.contains( 'FEE_PROC' ) || T.contains( 'AEB' ) )
 {
  T.add ( 'MDC_CALC' );
  TRG = T;
 }

for( Pair in PriorTarget )
   if( T.contains( Pair[0] ) )
   {
    ENV.EntityPrefix = Pair[1]
    if( Pair[1] == 'AEB' )
     TXN.proc_class = 'MDC'
    else
     TXN.proc_class = Pair[1]
    break
   }

 if( ToTieto() )
 {
  T.add('TIETO');
  TRG = T
 }
 
if( !(SVC.REF.whitePPTietoHOs.get( TXN.ho_id ) ))

if( T.contains( 'MDC_PROC' ) || T.contains( 'DCS' ) || T.contains( 'AEB' ) )
 {
  T.add ( 'PPTIETO' );
  T.remove ( 'TIETO' );
  T.remove ( 'MDC_CALC' );
  TRG = T;
 }

 return TRG
}

boolean void_flag =
           ( TXN.source_code.trim() == 'VOID'
        || ( TXN.mdc_trans_type != null && !['D','C'].contains( TXN.mdc_trans_type ) )
        || ( TXN.mdc_trans_type == 'D' && TXN.mdc_trans_id == '00' ) )

if( [ "BEEP","MEGP", "MTSP" ].contains( TXN.firma ) && !void_flag ) {
    TARGET = Dublicate( [ "MPCS", "MOBYPAY" ] )

    TXN.batch_id = 'C' + TXN.ho_id.substring( 1, 5 ) + TXN.proc_date.format( 'ddMM')
    TXN.subdomain = TXN.firma.substring( 0, 2 )

    if( TXN.ho_id == '09501' && TXN.firma == 'BEEP' ) {
        TARGET = Dublicate( [ "MPCS", "TNKB09501", "MOBYPAY" ] )
    }

    if( TXN.ho_id == '01938' && TXN.firma == 'BEEP' ) {
        TARGET = Dublicate( [ "MPCS", "TNKB01938", "MOBYPAY" ] )
    }

    if( TXN.ho_id == '30455' && TXN.firma == 'BEEP' ) {
        TARGET = Dublicate( [ "MPCS", "TNKB30455", "MOBYPAY" ] )
    }
    return
}

if( "BPRC" == TXN.firma ) {
    TARGET  = Dublicate( [ "BPRC", "BPREPORT" ] )
    return
}

if( "KRNA" == TXN.firma ) {
    TARGET  = Dublicate( "KRNA" )
    return
}

if( TXN.source_code?.trim() == 'DCS' ) {
    TARGET = Dublicate( "DCS" )
    return
}

if( TXN.source_code?.trim() == 'FEE' ) {
    TARGET = Dublicate( "FEE_PROC" )
    return
}

if( TXN.source_code?.trim() == 'CBK' ) {
    TARGET = Dublicate( "CBK_PROC" )
    return
}

// LOTY part
if( "LOTY" == TXN.firma && !void_flag && TXN.source_code.trim() != 'EXTMDC' ) {
    TARGET  = Dublicate( "LOTYTRN" )
    return
}

if( "LOTY" == TXN.firma && void_flag && TXN.trans_type.substring(1).trim() != '00' ) {
    TARGET  = Dublicate( "LOTYBNS" )
    return
}

if( "LOTY" == TXN.firma && TXN.source_code.trim() == 'EXTMDC' ) {
    TARGET  = Dublicate( "LOTYVRT" )
    return
}

if( "LOTY" == TXN.firma ) {
    TARGET  = Dublicate( "VOID" )
    return
}

// BBR
if( TXN.card_no_dec.startsWith('427263') && TXN.trans_type.startsWith('I') ) {
    TARGET  = Dublicate( "LOTYVRT" )
    return
}

// LUNCH in UCS OFFICE only
if( ![ "000", "010"].contains( TXN.trans_type ) && TXN.ho_id == '08165' ) {
    TARGET  = Dublicate( "UCSLBNS" )
    return
}

// FRB
//if( TXN.ho_id >= "05100" && TXN.ho_id <= "05169" && TXN.source_code.trim() != "FRB_OUT" && !void_flag ) {
//
//    // Разрешены только эти операции
//    if( ![ '000', '010', '030', '040' ].contains( TXN.trans_type ) ) {
//        TXN.rej_status = 'FTT'
//        return REJECTED
//    }
//
//    TARGET  = "FRB"
//    return
//}

if( "AMEX" == TXN.firma ) {
    if( [ "000", "010"].contains( TXN.trans_type ) && !void_flag ) {
        
      //  if( TXN.clishe_no?.startsWith( "855" ) && TXN.mbr_id?.equals( "AMEXAE" ) )
        if( 'AEB'.equals(ENV?.subct) )
         TARGET  = Dublicate( "AEB" )
        else
          if( 'AMEX'.equals(ENV?.subct) )
           TARGET  = Dublicate( "MDC_PROC" )
          else
         TARGET  = Dublicate( "AMEX" )
    } else {
        TARGET  = Dublicate( "VOID" )
    }
    return
}

// Международные карты могут ходить только с этими валютами
if( !['RUB','EUR','USD' ].contains( TXN.curr_code ) ) {
    TXN.rej_status = "UCC"
    TXN.rej_comment = "Bad currency"
    return REJECTED
}

// Учитываем тип trans_type в MDC
if( [ "000", "010"].contains( TXN.trans_type )
        && !void_flag ) {
    TARGET  = Dublicate( "MDC_PROC" )

    if( TXN.ho_id == '09501' ) {
        TARGET = Dublicate( [ "MDC_PROC", "TNKT09501" ] )
    }

    if( TXN.ho_id == '01938' ) {
        TARGET = Dublicate( [ "MDC_PROC", "TNKT01938" ] )
    }

    if( TXN.ho_id == '30455' ) {
        TARGET = Dublicate( [ "MDC_PROC", "TNKT30455" ] )
    }

} else {

    if( [ "I00", "000", "I23", "D08" ].contains( TXN.trans_type )
        && ["VISA", "EURO", "JCB", "CUP" ].contains( TXN.firma )
        && void_flag ) {
        TARGET = Dublicate( "VOID_NTK" )
        return
    }

    TARGET  = Dublicate( "VOID" )
    return
}

// AVIA target forces DAP authorization
if( TXN.auth_code?.trim() == ""
&& ( ( TXN.ho_id == '60392') && ( TXN.merchantid != '3260392140' ) && ( TXN.merchantid != '3260392027' ) && ( TXN.merchantid != '3160392299' )
  || [ '4926284001', '4926284002',  ].contains( TXN.merchantid?.trim() )
  || ( TXN.ho_id == '26284')
  || ( TXN.ho_id == '32401')
  ) ) {
    TARGET = Dublicate( [ "MDC_PROC", "AVIA" ] )
    return
}