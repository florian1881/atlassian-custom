import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.IssueImpl
import com.atlassian.jira.issue.IssueInputParameters
import com.atlassian.jira.issue.IssueInputParametersImpl
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.workflow.TransitionOptions
import com.atlassian.jira.workflow.TransitionOptions.Builder
import com.atlassian.jira.issue.MutableIssue

IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager()
CommentManager commentManager = ComponentAccessor.getCommentManager()

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueManager = ComponentAccessor.getIssueManager()
def optionsManager = ComponentAccessor.getOptionsManager()
//def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def issue = issueManager.getIssueObject("MFMSW-1581")
Collection cfs = customFieldManager.getCustomFieldObjectsByName("暂停原因")
CustomField cf = cfs[0]
def fieldConfig = cf.getRelevantConfig(issue)
def options = optionsManager.getOptions(fieldConfig)
def option = options.find {it.value == "父事务已暂停"}
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def comment = "当前子任务的父事务『" + issue.key + "：" + issue.summary + "』已暂停，因此当前子任务也进入暂停状态。"

issueLinkManager.getOutwardLinks(issue.getId()).each {issueLink ->
    if (issueLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue subtaskLinkedIssue = issueLink.getDestinationObject()
        log.warn subtaskLinkedIssue.key+subtaskLinkedIssue.summary+subtaskLinkedIssue.status
        if (subtaskLinkedIssue.status.name == "正在开发处理"){
            def mutableIssue = issueManager.getIssueObject(subtaskLinkedIssue.getId())
            mutableIssue.setCustomFieldValue(cf,option)
            issueManager.updateIssue(systemUser, mutableIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
            commentManager.create(subtaskLinkedIssue, systemUser, comment, true)
            doTransition(systemUser, subtaskLinkedIssue, 21, true)
        }
    }
}

def doTransition(ApplicationUser user, 
                 Issue issue,
                 Integer actionId,
                 Boolean skipPermissionCondition){
    IssueService issueService = ComponentAccessor.getIssueService()
    def transitionValidationResult
    def transitionResult

    if(skipPermissionCondition){
        TransitionOptions trasitionOptions = new Builder().skipPermissions().skipValidators().setAutomaticTransition().skipConditions().build();
        transitionValidationResult = issueService.validateTransition(user, issue.id, actionId, issueService.newIssueInputParameters(), trasitionOptions)
    }else{
        transitionValidationResult = issueService.validateTransition(user, issue.id, actionId, issueService.newIssueInputParameters())
    }  
    log.warn("skipPermissionCondition:"+skipPermissionCondition+" issue.id:"+issue.id+" actionId:"+actionId)
    if (transitionValidationResult.isValid()) {
        transitionResult = issueService.transition(user, transitionValidationResult)
        if (!transitionResult.isValid()) { 
            log.warn("Transition result is not valid") 
        }
    } else {
        log.warn("transitionValidationResul is not valid") 
    }    
}