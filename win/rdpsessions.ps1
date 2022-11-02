
    $rdp_server=$args[0]
    # Run the qwinsta.exe and parse the output (the qwinsta.exe tool can normally be found in most windows servers in the 'C:\Windows\System32' folder)
    $queryResults = (C:\Windows\System32/qwinsta /server:$rdp_server | foreach { (($_.trim() -replace "\s+",","))} | ConvertFrom-Csv)  
     
    # Pull the session information from each instance 
    ForEach ($queryResult in $queryResults) { 
        $RDPUser = $queryResult.USERNAME 
        $sessionType = $queryResult.SESSIONNAME 
         
        # We only want to display where a "person" is logged in. Otherwise unused sessions show up as USERNAME as a number 
        If (($RDPUser -match "[a-z]") -and ($RDPUser -ne $NULL)) {  
            # When running interactively, uncomment the Write-Host line below to show the output to screen 
            Write-Host $ServerName logged in by $RDPUser on $sessionType 
            rwinsta /server:$rdp_server $sessionType
        } 
    } 