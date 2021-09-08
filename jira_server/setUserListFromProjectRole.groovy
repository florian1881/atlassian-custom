import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.link.*


def issueManager = ComponentAccessor.getIssueManager()
//def issue = issueManager.getIssueObject("NGP-704")
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def cfManager = ComponentAccessor.getCustomFieldManager()

def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager.class)
def projectRole = projectRoleManager.getProjectRole("方案评审人")
//    def project = ComponentAccessor.getProjectManager().getProjectObjByName(projectName)
def project = issue.getProjectObject()
def usersInRole = projectRoleManager.getProjectRoleActors(projectRole, project).getApplicationUsers().toList()

log.warn(usersInRole)
Collection cfs = cfManager.getCustomFieldObjectsByName("用户列表1")
CustomField cf = cfs[0]

issue.setCustomFieldValue(cf,usersInRole)
//issueManager.updateIssue(systemUser, issue, EventDispatchOption.DO_NOT_DISPATCH, false)
def parentIssue = issue.parentObject
assert (parentIssue)

def rilm = ComponentAccessor.getComponent(RemoteIssueLinkManager)
//def u = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
String storyDocumentUrl
rilm.getRemoteIssueLinksForIssue(parentIssue).findAll {
 it.applicationType == RemoteIssueLink.APPLICATION_TYPE_CONFLUENCE
}.each {
 storyDocumentUrl = it.getUrl()
 log.warn(storyDocumentUrl)
}

cfs = cfManager.getCustomFieldObjectsByName("Story Document")
cf = cfs[0]

issue.setCustomFieldValue(cf,storyDocumentUrl)