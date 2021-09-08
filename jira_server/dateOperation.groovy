import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.comments.Comment
import com.atlassian.jira.issue.comments.CommentManager
import java.sql.Timestamp

def systemUser = ComponentAccessor.userManager.getUserByName("system")

def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()

def issue = issueManager.getIssueObject("NGP-1080")
MutableIssue mutableIssue = issueManager.getIssueObject(issue.getId())

Timestamp currentTime = new Timestamp((new Date()).time)
Timestamp planedFinishTime = new Timestamp((new Date() + 7).time)

Collection cfsStartDate = customFieldManager.getCustomFieldObjectsByName("Start date")
CustomField cfStartDate = cfsStartDate[0]
Date startDate = mutableIssue.getCustomFieldValue(cfStartDate) as Date

Collection cfsSlaDate = customFieldManager.getCustomFieldObjectsByName("SLA date")
CustomField cfSlaDate = cfsSlaDate[0]
Date slaDate = mutableIssue.getCustomFieldValue(cfSlaDate) as Date

Collection cfsFinishDate = customFieldManager.getCustomFieldObjectsByName("Finish date")
CustomField cfFinishDate = cfsFinishDate[0]
Date finishDate = mutableIssue.getCustomFieldValue(cfFinishDate) as Date

if (startDate == null){
    mutableIssue.setCustomFieldValue(cfStartDate, currentTime)
}

if (finishDate == null){
    mutableIssue.setCustomFieldValue(cfFinishDate, planedFinishTime)
}

startDate = mutableIssue.getCustomFieldValue(cfStartDate) as Date
finishDate = mutableIssue.getCustomFieldValue(cfFinishDate) as Date

log.warn(startDate)
log.warn(finishDate)

if (slaDate == null){
    if(finishDate - startDate < 4)
    	mutableIssue.setCustomFieldValue(cfSlaDate, startDate)
    else {
        Integer diffDate = (finishDate - startDate)/2 as Integer
        slaDate = startDate + diffDate
        log.warn(slaDate)
        mutableIssue.setCustomFieldValue(cfSlaDate, slaDate)
    }
}

issueManager.updateIssue(systemUser, mutableIssue, EventDispatchOption.ISSUE_UPDATED, false)


