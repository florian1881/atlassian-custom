import com.atlassian.jira.component.ComponentAccessor
import com.opensymphony.workflow.InvalidInputException

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def epiclinkfield = customFieldManager.getCustomFieldObjectsByName("史诗链接").first() //Change史诗连接to the actual name of Epic Link
def epicValue = issue.getCustomFieldValue(epiclinkfield)

log.warn("epicVaue is: " + epicValue)
return epicValue
