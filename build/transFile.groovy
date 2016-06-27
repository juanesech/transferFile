import com.urbancode.air.AirPluginTool;
import com.urbancode.air.CommandHelper;

def apTool = new AirPluginTool(this.args[0], this.args[1])
def props = apTool.getStepProperties();

def udclientP = props['udclientPath'];
def url = props['url'];
def user = props['user'];
def pass = props['pass'];
def basedir = props['base'];
def include = props['included'];
def component = props['component'];
def vName = props['vName'];
def udclient = "$udclientP/udclient"
//logical

//Create Version
println "Creando version temporal"
def crtVer= ["$udclient","-weburl", url,"-username", user,"-password", pass,"createVersion","-component", component,"-name", vName].execute().text
println crtVer

//Load artifacts
println "Cargando artefactos a version $vName"
def loadV= ["$udclient","-weburl", url,"-username", user,"-password", pass,"addVersionFiles", "-component", component, "-version", vName, "-base", basedir, "-include", include, "--verbose"].execute().text
println loadV

//Set an output property
apTool.setOutputProperty("Version Name", vName);
//apTool.setOutputProperty("Load files", loadV);

apTool.storeOutputProperties();//write the output properties to the file