import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.user.ApplicationUser

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def watcherManager = ComponentAccessor.getWatcherManager()
def userManager = ComponentAccessor.getUserManager()

Issue parentIssue = issue.getParentObject()
Collection cfs = customFieldManager.getCustomFieldObjectsByName("负责人")
CustomField cf = cfs[0]

def cfValueObject = issue.getCustomFieldValue(cf) as ApplicationUser

watcherManager.startWatching(cfValueObject, parentIssue)

