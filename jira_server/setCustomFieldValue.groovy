//set select list customfield
import com.atlassian.jira.component.ComponentAccessor

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def customField = customFieldManager.getCustomFieldObjectByName("暂停原因") // name of CF
def optionsManager = ComponentAccessor.getOptionsManager()
def fieldConfig = customField.getRelevantConfig(issue)
def option = optionsManager.getOptions(fieldConfig).find { it.value == "父事务已暂停" } // value of option 

issueInputParameters.addCustomFieldValue(customField.id, option.optionId as String)
issueInputParameters.setSkipScreenCheck(true)

//set text customefiled
def cf = customFieldManager.getCustomFieldObjects(issue).find {it.name == 'My Custom Field'}

issueInputParameters.addCustomFieldValue(cf.id, 'cf value')