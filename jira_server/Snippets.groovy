import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.UpdateIssueRequest
import com.atlassian.jira.user.ApplicationUser

// get issue property
issue.get("project").getKey().equals("TSP") && ["High","Emergency"].contains(issue.get("priority").name)

//
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.ComponentManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.event.type.EventDispatchOption
import org.apache.log4j.Category

log = Category.getInstance("com.custom.Reviewer")
log.setLevel(org.apache.log4j.Level.DEBUG)
log.debug("---- begin reviewer post-fuction -----")

def assignee = ComponentAccessor.getUserUtil().getUserByName(issue.getAssignee().getName())
def reviewerCustomField = ComponentManager.getInstance().getCustomFieldManager().getCustomFieldObjectByName("Reviewer")
def reviewer = issue.getCustomFieldValue(reviewerCustomField)
log.debug("assignee: $assignee || reviewer: $reviewer")

if (reviewer == null) {
	log.debug ("Empty reviewer.  Set to assignee.")
	MutableIssue myIssue = issue
	IssueManager issueManager = ComponentAccessor.getIssueManager()
	UserManager userManager = ComponentAccessor.getUserManager()
	myIssue.setCustomFieldValue(reviewerCustomField, assignee)
	issueManager.updateIssue(userManager.getUser("automation"), myIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
}
log.debug("---- end reviewer post-fuction -----")