
if( ['RDY', 'SND'].contains(TXN.rej_status?.trim() ) ) 
 return

if( ['CBK', 'FEE'].contains(TXN.proc_class) )   
 return

if( ['REV2MMS'].contains( TXN.source_code?.trim() ) )
 return


if( SVC.REF.skipScript.get( this.class.getName().toUpperCase().replace('.','_').split("_")[0], "HOINLIST", "RETURN", "NEXT" )?.cond_value?.split(",")?.contains( TXN.ho_id.trim() ) )
{
 return 
}

ct = TXN.firma.trim()
if( ct == "EURO")
 ct = "MAST"

if( SVC.REF.hadMerchCt.get( ct, TXN.merchantid.trim(), TXN.proc_date ) == null )
{
  TXN.rej_status = "NOFLAGCT"
  return REJECTED 
}
