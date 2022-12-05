if( !['DCS', 'MDC'].contains(TXN.proc_class) )
    return

if( ['05','07'].contains(TXN.entry_mode) && (TXN.icc_data == null || TXN.icc_data.trim().isEmpty()) )
 TXN.entry_mode = '01'
