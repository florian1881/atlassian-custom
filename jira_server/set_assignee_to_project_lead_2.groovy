/**
    This code run in Scripte Runner in Jia Automation.
*/
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.version.Version
import com.atlassian.jira.project.version.VersionManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue

def versionManager = ComponentAccessor.getVersionManager()
def projectManager = ComponentAccessor.getProjectManager()
//def project = projectManager.getProjectObjByKey(issue.projectObject.key)
def issueManager = ComponentAccessor.getIssueManager()
def issueLinkManager = ComponentAccessor.getIssueLinkManager()
MutableIssue currentIssue = issueManager.getIssueObject("NGP-228")
//def issue1 = issueManager.getIssueObject("NGP-228")

//log.setLevel(Level.DEBUG)
String projectLead = issue.getProjectObject().getProjectLead().getKey()
currentIssue.setAssigneeId(projectLead)

issueManager.updateIssue(issue.getProjectObject().getProjectLead(), currentIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
