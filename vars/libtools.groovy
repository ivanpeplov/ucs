// This is the 'include file'
import java.text.SimpleDateFormat

def getNameForRegion(Map args){
//input region:
//output server list
//example server = getServerForRegion(region:rdh)
//premise is set servers variable
    server_list = ''
    for (int i = 0; i < servers.size(); i++) {
        if ( servers[i].region.toString() == args.region.toString()) {
            if ( server_list == '' ) { server_list = "${servers[i].name}" }
            else { server_list = "${server_list} ${servers[i].name}" }
        }
    }
    return server_list
}
def getVmForRegion(Map args){
//input region:
//output server list
//example server = getServerForRegion(region:rdh)
//premise is set servers variable
    server_list = ''
    for (int i = 0; i < servers.size(); i++) {
        if ( servers[i].region.toString() == args.region.toString()) {
            if ( server_list == '' ) { server_list = "${servers[i].vm}" }
            else { server_list = "${server_list} ${servers[i].vm}" }
        }
    }
    return server_list
}
return this
