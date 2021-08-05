import java.util.Date
import java.sql.Timestamp
import com.atlassian.jira.component.ComponentAccessor
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Inherent Risk")

def m = ["<b><i>Critical</i></b>":7,"<b><i>High</i></b>":10,"<b><i>Medium</i></b>":10,"<b><i>Low</i></b>":180]
def risk = issue.getCustomFieldValue(cf) as String
def days = m.find { risk?.contains(it.key) }?.value
if(days != null){
def apDate = new Date(issue.created.getTime()).plus(days)
def cfApDate = customFieldManager.getCustomFieldObjectByName("Due Date to Approve Plan")
issue.setCustomFieldValue(cfApDate,new Timestamp(apDate.getTime()))
}