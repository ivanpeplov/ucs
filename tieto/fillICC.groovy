
if( TXN.ext_data?.trim()?.length() > 0 )
{
 TXN.icc_data = TXN.ext_data.substring(14);
}

icc_data = TXN.icc_data
l = icc_data?.length()
while( l > 0 )
{
 if( icc_data[l-1] != ' ' )
  break;
 else
  l --;
}

rep = "40"
if( l == 31)
 TXN.icc_data = icc_data[0..30]+rep
else
if( l >= 33 )
if( '  '.equals(icc_data[31,32]) )
{
 sb = new StringBuilder(icc_data)
 sb.replace(31,33,rep)
 TXN.icc_data = sb.toString()
}
