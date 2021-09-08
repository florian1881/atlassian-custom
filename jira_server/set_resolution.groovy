import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.config.ResolutionManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.event.type.EventDispatchOption

def issueManager = ComponentAccessor.getIssueManager()
def mutableIssue = issueManager.getIssueObject("MFMSW-919")
def resolutionManager = ComponentAccessor.getComponent(ResolutionManager)

mutableIssue.setResolutionObject(resolutionManager.getResolutionByName("被否决"))
def systemUser = ComponentAccessor.userManager.getUserByName("system")
issueManager.updateIssue(systemUser, mutableIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
