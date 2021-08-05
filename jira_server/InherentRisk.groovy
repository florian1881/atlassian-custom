import com.atlassian.jira.component.ComponentAccessor

def customFieldManager = ComponentAccessor.getCustomFieldManager()

def cf = customFieldManager.getCustomFieldObjectByName("Inherent Impact")
if(cf==null)return
def impact = issue.getCustomFieldValue(cf)?.toString()
if(impact == null) return

cf = customFieldManager.getCustomFieldObjectByName("Probability")
if(cf==null)return
def probability = issue.getCustomFieldValue(cf)
if(probability == null) return

def m = [:]
m["low_low"] = "Low"
m["low_medium"] = "Low"
m["low_high"] = "Medium"
m["low_very high"] = "Medium"
m["medium_low"] = "Low"
m["medium_medium"] = "Medium"
m["medium_high"] = "Medium"
m["medium_very high"] = "High"
m["high_low"] = "Medium"
m["high_medium"] = "Medium"
m["high_high"] = "High"
m["high_very high"] = "High"
m["very high_low"] = "Medium"
m["very high_medium"] = "High"
m["very high_high"] = "High"
m["very high_very high"] = "Critical"
def key = (impact+"_"+probability).toLowerCase()
def risk = m[key]

if(risk == null) "[" + key + "] not found"
else risk