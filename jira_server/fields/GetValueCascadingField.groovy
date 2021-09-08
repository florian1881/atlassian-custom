//For behaviours

import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.jira.groovy.user.FormField
import com.onresolve.jira.groovy.user.FieldBehaviours

FormField link = getFieldByName("Ссылка на youtube")
FormField comment = getFieldById("comment")
def optionsManager = ComponentAccessor.optionsManager
def customField = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_13503")
Map cfVal = underlyingIssue.getCustomFieldValue(customField) as Map  
String first = cfVal.get(null)    
comment.setFormValue(first)
if(first == "Переделка"){
    link.setRequired(false)
}
