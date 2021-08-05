import com.atlassian.jira.component.ComponentAccessor

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Inherent Risk")
if(cf==null)return

def risk = issue.getCustomFieldValue(cf);
def m = [Critical:7,High:7,Medium:10,Low:180]
def days = m[risk]

if(days != null)
new Date(issue.created.getTime()).plus(days)
