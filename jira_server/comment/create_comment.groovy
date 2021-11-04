/*
* This example script  post function for ScriptRunner for Jira Server shows how you can add a comment to an issue when it is transitioned.
* "All right, title and interest in this code snippet shall remain the exclusive intellectual property of Adaptavist Group Ltd and its affiliates. Customers with a valid ScriptRunner
* license shall be granted a  non-exclusive, non-transferable, freely revocable right to use this code snippet only within their own instance of Atlassian products. This licensing notice cannot be removed or
* amended and must be included in any circumstances where the code snippet is shared by You or a third party." 
*/ 
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.user.ApplicationUser

// Get a pointer to the issue
Issue issueKey  = issue

// Get the current logged in user
def CurrentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser() as ApplicationUser

// Get access to the Jira comment and component manager
CommentManager commentManager = ComponentAccessor.getCommentManager()

// Get the last comment entered in on the issue to a String
def comment = "A Sample Comment"


// Check if the issue is not null
if(issueKey){
        // Create a comment on the issue
        commentManager.create(issueKey, CurrentUser,comment, true)
}
