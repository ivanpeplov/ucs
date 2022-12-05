if( TXN.rej_status?.trim() == 'RDY' ) return

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

if (ENV.merchInfo == null) {
        TXN.rej_status = "LGCERR"
        TXN.rej_comment = "merchInfo suddenly absent"
        return ERROR
    }
    else {
        // Сравниваем валюту транзакции с валютой мерчанта
        // но сначала перебиваем валюты на правильные значения
        mid_curr_code = ENV.merchInfo.curr_code_3c

        if( mid_curr_code == 'RUR' ) { // Потому что в MMS нет валюты RUB
            mid_curr_code = 'RUB'
        }
        if( ['RUR','643','810'].contains( TXN.curr_code ) ) {
            TXN.curr_code = 'RUB'
        }
        if( TXN.curr_code == '840' ) {
            TXN.curr_code = 'USD'
        }
        if( TXN.curr_code == '978' ) {
            TXN.curr_code = 'EUR'
        }
        if( TXN.curr_code == '398' ) {
            TXN.curr_code = 'KZT'
        }
        if( TXN.curr_code == '980' ) {
            TXN.curr_code = 'UAH'
        }
        if( TXN.curr_code == '603' ) {
            TXN.curr_code = 'BYN'
        }

        if( !['RUB','EUR','USD','KZT','UAH','BYN'].contains( TXN.curr_code ) ) {
            TXN.rej_status = "UCC"
            TXN.rej_comment = "Bad currency"
            return REJECTED
        }

        if( TXN.curr_code != mid_curr_code ) {
            TXN.rej_status = "UCC"
            TXN.rej_comment = "MMS currency: " + mid_curr_code
            return REJECTED
        }
    }
