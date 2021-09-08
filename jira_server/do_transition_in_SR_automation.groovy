import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.version.Version
import com.atlassian.jira.project.version.VersionManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.IssueInputParametersImpl

def issueManager = ComponentAccessor.issueManager
//def issue = issueManager.getIssueObject(event.issue.key)
IssueService issueService = ComponentAccessor.getIssueService()
def actionId = 131 // change this to the step that you want the issues to be transitioned to
def transitionValidationResult
def transitionResult
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issue1 = issueManager.getIssueObject("NGP-273")

def systemUser = ComponentAccessor.userManager.getUserByName("system")


if (issue1.getIssueType().name == "Bug") 
{ 
	transitionValidationResult = issueService.validateTransition(systemUser, issue1.id, actionId,new IssueInputParametersImpl())
	if (transitionValidationResult.isValid()) {
		transitionResult = issueService.transition(systemUser, transitionValidationResult)
 		if (transitionResult.isValid()) { 
        	log.debug("Transitioned issue $issue through action $actionId") 
        }
 		else {
        	log.debug("Transition result is not valid") 
        }
    }
 	else {
 		log.debug("The transitionValidation is not valid")
 	}
}








