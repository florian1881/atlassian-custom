import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.UpdateIssueRequest
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.ComponentManager
import com.atlassian.jira.issue.comments.CommentManager
import org.apache.log4j.Category
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField

// needs testing
//def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def compManager = ComponentManager.getInstance()

CommentManager commentMgr = ComponentAccessor.getCommentManager()
commentMgr = (CommentManager) compManager.getComponentInstanceOfType(CommentManager.class)
commentMgr.create(issue, currentUser, "Approved by [~"+currentUser.key+"]", false)