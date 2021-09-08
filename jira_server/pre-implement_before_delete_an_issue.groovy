import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.CustomFieldManager
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

IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager()
IssueService issueService = ComponentAccessor.getIssueService()


def issueManager = ComponentAccessor.getIssueManager()
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def issue = issueManager.getIssueObject("NGP-395")

//
//首先处理Block Link
//
//第1步: 不论当前issue是什么类型，先处理当前issue的outward block链接 
//检查当前issue的outward block链接的目标issue
//如果目标issue是Story/Bug时，则尝试将目标issue推进到“正在开发处理”的下一个状态
//如果目标issue是task或者Sub-task, 则不需要执行任何操作
//目标issue不可能是Epic，目前Jira限制了Epic不能产生任何Link
issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "Blocks") {
        Issue blockLinkedIssue = eachLink.getDestinationObject()
        log.warn("-- Blocks: " + blockLinkedIssue.getSummary())
        issueLinkManager.removeIssueLink(eachLink,systemUser)
        tryToEscapeBlock (blockLinkedIssue, systemUser)
    }
}
//第2步: 如果当前issue存在sub-task, 则遍历处理每个sub-task
//每个sub-task的处理流程与第1步中处理单个issue相同
issueLinkManager.getOutwardLinks(issue.getId()).each {eachSubtaskLink ->
    if (eachSubtaskLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue subtaskIssue = eachSubtaskLink.getDestinationObject()
        log.warn("Sub-task: " + subtaskIssue.getSummary())
        issueLinkManager.getOutwardLinks(subtaskIssue.getId()).each {eachLink ->
            if (eachLink.getIssueLinkType().getName() == "Blocks") {
                Issue blockLinkedIssue = eachLink.getDestinationObject()
                log.warn("-- Blocks: " + blockLinkedIssue.getSummary())
                issueLinkManager.removeIssueLink(eachLink,systemUser)
                tryToEscapeBlock (blockLinkedIssue, systemUser)
            }
        }
    }
}
//第3步：如果当前issue是sub-task, 还要检查一下父issue是否满足transition的条件
issueLinkManager.getInwardLinks(issue.getId()).each {eachSubtaskLink ->
    if (eachSubtaskLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue parentIssue = issue.getParentObject()
        log.warn("Parent: " + parentIssue.getSummary())
        issueLinkManager.removeIssueLink(eachSubtaskLink,systemUser)
        tryToEscapeBlock (parentIssue, systemUser)
    }
}

//
//其次处理Suspend Link
//
//第1步: 不论当前issue是什么类型，先处理当前issue的outward suspend链接 
//检查当前issue的outward suspend链接的目标issue
//不管目标issue是什么类型，都需要尝试将目标issue推进到“正在...”的状态, Epic也可能被suspend
issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "Suspends") {
        Issue suspendLinkedIssue = eachLink.getDestinationObject()
        log.warn("-- Suspends: " + suspendLinkedIssue.getSummary())
        issueLinkManager.removeIssueLink(eachLink,systemUser)
        tryToEscapeSuspend (suspendLinkedIssue, systemUser)
    }
}
//第2步: 如果当前issue存在sub-task, 则遍历处理每个sub-task
//每个sub-task的处理流程与第1步中处理单个issue相同
issueLinkManager.getOutwardLinks(issue.getId()).each {eachSubtaskLink ->
    if (eachSubtaskLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue subtaskIssue = eachSubtaskLink.getDestinationObject()
        log.warn("Sub-task: " + subtaskIssue.getSummary())
        issueLinkManager.getOutwardLinks(subtaskIssue.getId()).each {eachLink ->
            if (eachLink.getIssueLinkType().getName() == "Suspends") {
                Issue suspendLinkedIssue = eachLink.getDestinationObject()
                log.warn("-- Blocks: " + suspendLinkedIssue.getSummary())
                issueLinkManager.removeIssueLink(eachLink,systemUser)
                tryToEscapeBlock (suspendLinkedIssue, systemUser)
            }
        }
    }
}

def tryToEscapeBlock (Issue issue, ApplicationUser user) {
    String issueStatus = issue.getStatus().getName()
    Integer actionId = 0
    if (issueStatus == "正在开发处理" || issueStatus == "等待开发处理")
        if (issue.getIssueType().name == "Story")
            actionId = 131
        else if (issue.getIssueType().name == "Bug")
            actionId = 131
    
    if (getNumOfOpenLinkedIssues(issue, "Suspends") + getNumOfOpenLinkedIssues(issue, "Block") == 0)
        doTransition(user, issue, actionId, false)
    else
        return
    //transitionValidationResult = issueService.validateTransition(systemUser, blockLinkedIssue.id, bugActionId, new IssueInputParametersImpl())    
}
//检查issue的suspending issue数量，以及当前状态，确定是否返回“正在...”状态
def tryToEscapeSuspend (Issue issue, ApplicationUser user) {
    CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
    IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager()
    IssueService issueService = ComponentAccessor.getIssueService()
    CommentManager commentManager = ComponentAccessor.getCommentManager()

    Collection cfs = customFieldManager.getCustomFieldObjectsByName("暂停原因")
    CustomField cf = cfs[0]
    def cfValueObject = issue.getCustomFieldValue(cf) as String
    log.warn(cfValueObject)

    if (!issue.getStatus().getName().contains("等待") || cfValueObject != "被前置事务suspend") 
        return

    if (getNumOfOpenLinkedIssues(issue, "Suspends") > 0)
        return

    Integer actionId = 0
    if(issue.getIssueType().name == "Epic"){
        switch(issue.getStatus().getName()) {
        case "等待方案评审":
            actionId = 391
            break
        case "等待方案设计":
            actionId = 161 
            break
        case "等待分解Epic":
            actionId = 381 
            break       
        case "等待开发处理":
            actionId = 181 
            break        
        case "等待提交成果":
            actionId = 331 
            break        
        case "等待验收":
            actionId = 351 
            break   
        default:
            return
        }
    }

    if(issue.getIssueType().name == "Story"){
        switch(issue.getStatus().getName()) {
        case "等待测试":
            actionId = 341
            break
        case "等待分配Sprint":
            actionId = 401 
            break
        case "等待开发处理":
            actionId = 161 
            break       
        case "等待提交成果":
            actionId = 361 
            break        
        case "等待需求评审1":
            actionId = 411 
            break        
        case "等待需求评审2":
            actionId = 421 
            break        
        case "等待需求评审3":
            actionId = 431 
            break 
        case "等待需求设计":
            actionId = 41 
            break 
        case "等待验收":
            actionId = 381 
            break 
        default:
            return
        }
    }

    if(issue.getIssueType().name == "Bug"){
        switch(issue.getStatus().getName()) {
        case "等待测试":
            actionId = 391
            break
        case "等待分配Sprint":
            actionId = 481 
            break
        case "等待开发处理":
            actionId = 231 
            break       
        case "等待审核Bug调查结果":
            actionId = 491 
            break        
        case "等待提交成果":
            actionId = 411 
            break        
        case "等待调查Bug":
            actionId = 341 
            break        
        case "等待验收":
            actionId = 431 
            break             
        default:
            return
        }
    }

    if(issue.getIssueType().name == "Task"){
        switch(issue.getStatus().getName()) {
        case "等待开发处理":
            actionId = 201
            break
        case "等待验收":
            actionId = 251 
            break
        default:
            return
        }
    }

    if(issue.getIssueType().name == "Sub-task"){
        switch(issue.getStatus().getName()) {
        case "等待方案评审":
            actionId = 271
            break
        case "等待方案设计":
            actionId = 231 
            break
        case "等待开发处理":
            actionId = 11 
            break
        default:
            return
        }
    }

    doTransition(user, issue, actionId, true)

    def comment = "目前已不存在未完成的前置事务，系统已自动将当前事务置为正常处理状态，请经办人及时处理。"
    commentManager.create(issue, user, comment, true)
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

Integer getNumOfOpenLinkedIssues(Issue issue, String link){
    IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager()
    Integer totalIssue = 0
    issueLinkManager.getInwardLinks(issue.getId()).each {eachLink ->
        if (eachLink.getIssueLinkType().getName() == link) {
            Issue suspendLinkedIssue = eachLink.getSourceObject()
            if (suspendLinkedIssue.getStatus().getName() != "已完成")
                totalIssue ++
            log.warn(link + ": " + suspendLinkedIssue.getSummary())
        }
    }    
    log.warn("totalIssue: "+totalIssue)
    return totalIssue
}
