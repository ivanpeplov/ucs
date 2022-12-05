if( TXN.capability == null ||
    [null,''].contains( TXN.term_id?.trim() ) ||
   'R'.equals( TXN.id_method ) && [null,''].contains( TXN.eci?.trim() ) 
  )
 TXN.capability = '1';
