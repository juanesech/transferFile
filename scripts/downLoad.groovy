import com.urbancode.air.AirPluginTool;
import com.urbancode.air.CommandHelper;
import groovy.json.JsonOutput
import groovyx.net.http.*
import org.apache.http.*
import groovyx.net.http.RESTClient
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme
import groovyx.net.http.HttpResponseException
import groovyx.net.http.ContentType.*
import groovyx.net.http.Method.*
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import java.security.SecureRandom
import org.apache.http.conn.scheme.SchemeRegistry

def apTool = new AirPluginTool(this.args[0], this.args[1])
def props = apTool.getStepProperties();

def udclientP = props['udclientPath'];
def url = props['url'];
def user = props['user'];
def pass = props['pass'];
def downloadPath = props['downloadPath'];
def component = props['component'];
def vName = props['vName'];
def includes = props['includes'];
def excludes = props['excludes'];
def zipFile = "${downloadPath}/${component}_${vName}_artifacts.zip"
def udclient = "$udclientP/udclient"

//Download Version
println "Descargar desde version $vName"
def crtVer= ["$udclient","-weburl", url,"-username", user,"-password", pass,"downloadVersionArtifacts","-component", component,"-name", vName,"-version", vName,"-location", downloadPath,"--verbose"].execute().text
println crtVer

//Unzip Version
def builder = new AntBuilder()
builder.unzip(src:"${zipFile}", dest:"${downloadPath}", overwrite:true)

//Delete Zipe File
def deleteF = new File(zipFile)
deleteF.delete()

//Delete version
/*print "Deleting version $vName"
def credencial="${user}:${pass}"
def server=url[0..-4]
println server
def restClient = new RESTClient( "$server" )

//=== SSL UNSECURE CERTIFICATE ===
def sslContext = SSLContext.getInstance("SSL")
sslContext.init(null, [
		new X509TrustManager() {public X509Certificate[] getAcceptedIssuers() {null }
				public void checkClientTrusted(X509Certificate[] certs, String authType) { }
				public void checkServerTrusted(X509Certificate[] certs, String authType) { } } ] as TrustManager[], new SecureRandom())
def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
def httpsScheme = new Scheme("https", sf, 8443)
restClient.client.connectionManager.schemeRegistry.register( httpsScheme )
//================================

//print credencial.bytes.encodeBase64()
restClient.headers['Authorization'] = 'Basic ' + credencial.bytes.encodeBase64()

//Delete Version
def resp

try {
		resp = restClient.delete( path: "/rest/deploy/version/"+$vName )
} catch (Exception e) {
		println "Error deletnig version"
		 e.printStackTrace()
		System.exit(1)
}

println resp.getStatus()
println  resp.getData()*/

//Set an output property
apTool.setOutputProperty("Downloaded package", "$zipFile");
apTool.setOutputProperty("Temp version", "$vName");
//apTool.setOutputProperty("Load files", loadV);

apTool.storeOutputProperties();//write the output properties to the file