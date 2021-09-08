import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.IssueInputParametersImpl

//def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def issueManager = ComponentAccessor.issueManager
//def issue = issueManager.getIssueObject(event.issue.key)
IssueService issueService = ComponentAccessor.getIssueService()
def actionId = 21 // change this to the step that you want the issues to be transitioned to
def transitionValidationResult
def transitionResult
def customFieldManager = ComponentAccessor.getCustomFieldManager()
log.debug("The issue type is: " + issue.getIssueType().name)
 
if (issue.getIssueType().name == "Task") {
 
 transitionValidationResult = issueService.validateTransition(currentUser, issue.id, actionId,new IssueInputParametersImpl())

 if (transitionValidationResult.isValid()) {
 transitionResult = issueService.transition(currentUser, transitionValidationResult)
 if (transitionResult.isValid())
 { log.debug("Transitioned issue $issue through action $actionId") }
 else
 { log.debug("Transition result is not valid") }
 }
 else {
 log.debug("The transitionValidation is not valid")
 }
}
