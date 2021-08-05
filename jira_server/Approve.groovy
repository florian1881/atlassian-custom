import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.UpdateIssueRequest
import com.atlassian.jira.user.ApplicationUser

//MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject('DIT-95515')

IssueManager issueManager = ComponentAccessor.getIssueManager();
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
CustomField multiUser = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Approver(s)")
List<ApplicationUser> users = (List<ApplicationUser>) issue.getCustomFieldValue(multiUser)

def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
users?.remove(currentUser)
issue.setCustomFieldValue(multiUser, users);

multiUser = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Approved By")
users = (List<ApplicationUser>) issue.getCustomFieldValue(multiUser)
if(users == null)
    users = new ArrayList<>();
users.add(currentUser)
issue.setCustomFieldValue(multiUser, users);

