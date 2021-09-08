//for Script Console
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.greenhopper.service.sprint.Sprint
import com.atlassian.greenhopper.service.sprint.SprintManager
import com.onresolve.scriptrunner.runner.customisers.PluginModuleCompilationCustomiser
import com.onresolve.scriptrunner.runner.customisers.WithPlugin

IssueService issueService = ComponentAccessor.getIssueService()
def issueManager = ComponentAccessor.getIssueManager()
def systemUser = ComponentAccessor.userManager.getUserByName("system")

def mutableIssue = issueManager.getIssueObject("NGP-922")

def customFieldManager = ComponentAccessor.getCustomFieldManager()
//def issueManager1 = ComponentAccessor.getIssueManager()
//def optionsManager = ComponentAccessor.getOptionsManager()

Collection cfs = customFieldManager.getCustomFieldObjectsByName("Sprint")
CustomField cf = cfs[0]
def sprints = mutableIssue.getCustomFieldValue(cf) as List <Sprint>

Integer sprintIsFuture = 0    


for (Sprint s in sprints){
    log.warn(s.name+s.state)
    if (s.state.toString() == "ACTIVE"){
        sprintIsFuture = sprintIsFuture + 1
    }
}
return sprintIsFuture
