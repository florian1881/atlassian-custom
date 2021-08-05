import com.atlassian.jira.component.ComponentAccessor
import com.valiantys.nfeed.api.IFieldDisplayService;
import com.onresolve.scriptrunner.runner.customisers.PluginModule;
import com.onresolve.scriptrunner.runner.customisers.WithPlugin;
 
@WithPlugin("com.valiantys.jira.plugins.SQLFeed")
@PluginModule
IFieldDisplayService fieldDisplayService;

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Inherent Risk")
if(cf==null)return

def displayResult = fieldDisplayService.getDisplayResult(issue.getKey(), cf.id);
def risk = displayResult.getDisplay()//.charAt(0)
def cfCE = customFieldManager.getCustomFieldObjectByName("Control Environment")
if(cfCE==null)return
def ce = issue.getCustomFieldValue(cfCE)

def m = [:]
m["low_adequate"] = "immaterial"
m["low_moderate +"] = "immaterial"
m["low_moderate -"] = "immaterial"
m["low_insufficient"] = "immaterial"
m["medium_adequate"] = "immaterial"
m["medium_moderate +"] = "immaterial"
m["medium_moderate -"] = "low"
m["medium_insufficient"] = "low"
m["high_adequate"] = "low"
m["high_moderate +"] = "low"
m["high_moderate -"] = "moderate"
m["high_insufficient"] = "moderate"
m["Critical_adequate"] = "moderate"
m["Critical_moderate +"] = "moderate"
m["Critical_moderate -"] = "high"
m["Critical_insufficient"] = "high"
m[risk.toLowerCase()+"_"+ce.toLowerCase()]