import com.atlassian.jira.component.ComponentAccessor

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Inherent Risk")
if(cf==null)return

def risk = issue.getCustomFieldValue(cf)
if(risk == null) return
def cfCE = customFieldManager.getCustomFieldObjectByName("Control Environment")
if(cfCE==null)return
def ce = issue.getCustomFieldValue(cfCE)

def m = [:]
m["low_adequate"] = "Immaterial"
m["low_moderate +"] = "Immaterial"
m["low_moderate -"] = "Immaterial"
m["low_insufficient"] = "Immaterial"
m["medium_adequate"] = "Immaterial"
m["medium_moderate +"] = "Immaterial"
m["medium_moderate -"] = "Low"
m["medium_insufficient"] = "Low"
m["high_adequate"] = "Low"
m["high_moderate +"] = "Low"
m["high_moderate -"] = "Moderate"
m["high_insufficient"] = "Moderate"
m["critical_adequate"] = "Moderate"
m["critical_moderate +"] = "Moderate"
m["critical_moderate -"] = "High"
m["critical_insufficient"] = "High"

def key = (risk+"_"+ce).toLowerCase()
def v = m[key]
if(v==null)key
else v