import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.ModifiedValue
import org.apache.log4j.Level
import org.apache.log4j.Logger

log = Logger.getLogger("com.acme.UpdateCustomField")
log.setLevel(Level.DEBUG)

def issueManager = ComponentAccessor.getIssueManager()
def issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey("PROMO-17")
def customFieldManager = ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_13503") // Шаблоны
def valueCF = issue.getCustomFieldValue(customFieldManager)
log.debug("Значение в поля задаче: " + valueCF)
def userManager = ComponentAccessor.getUserManager()
def user = userManager.getUserByKey("robot")
def fieldConfig = customFieldManager.getRelevantConfig(issue)
log.debug(fieldConfig)
def parentOption = ComponentAccessor.getOptionsManager().getOptions(fieldConfig)?.find { it.toString() == 'Значение 1' }
log.debug(parentOption.optionId)
def childOption = ComponentAccessor.getOptionsManager().findByParentId(parentOption.optionId)?.find { it.toString() == 'Значение 2' }
log.debug(childOption.optionId)
def changeHolder = new DefaultIssueChangeHolder()
def newValue = [:]
newValue.put(null, parentOption)
newValue.put("1", childOption)
customFieldManager.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(customFieldManager), newValue),changeHolder)
// or 
issue.setCustomFieldValue(customFieldManager, newValue)
