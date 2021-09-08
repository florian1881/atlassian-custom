import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.bc.issue.search.SearchService 
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.bc.issue.IssueService.IssueValidationResult
import com.atlassian.jira.bc.issue.IssueService.UpdateValidationResult
import com.atlassian.jira.bc.issue.IssueService.IssueResult
import com.onresolve.scriptrunner.parameters.annotation.UserPicker
import com.onresolve.scriptrunner.parameters.annotation.Checkbox


def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
// user replacements
// **********************************************
// key : value
@UserPicker(label = "Source User", description = "Get a list of group memberships for this user")
ApplicationUser oldU
@UserPicker(label = "Target User", description = "Add this user to the list of groups")
ApplicationUser newU
@Checkbox(label = "Make changes", description = "Check to apply changes, Uncheck to preview changes")
boolean apply

String mode = (apply && apply == true)?"Apply":"Preview"
boolean adduser= (apply && apply == true)?true:false
if (oldU && newU) {
	String assigneeclause1 = 'assignee in ( "' 
	String reporterclause1 = 'reporter in ( "' 
	String clause2 = '")'

	// set true to add key (lefthand value), set false to add value (righthand value)
def jqlSearch 
List<Issue> issues = null
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
UserManager userMgr = ComponentAccessor.getUserManager()
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
IssueManager issueManager = ComponentAccessor.getIssueManager()

IssueService issueService = ComponentAccessor.issueService

def searchResult


    // ASSIGNEE
    jqlSearch = assigneeclause1 + oldU.username + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
    	searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
        StringBuffer sBuf1 = new StringBuffer()
        logit.info("Assignee Results : " + searchResult.getResults().size())
    	searchResult.getResults().each { issue ->
           sBuf1.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setAssigneeId(newU.username)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
                if (adduser) {
    			IssueResult updateResult = issueService.update(user, updateValidationResult);
    			if (!updateResult.isValid())
    			{
        			sBuf1.append("Y, ")
    			}
                }
			}
	    }
        logit.info(sBuf1.toString())
        } else {
    		logit.error("Invalid JQL: " + jqlSearch);
		}
   
    // REPORTER
    jqlSearch = reporterclause1 + oldU.username + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult2 =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
   	searchResult = searchService.search(user, parseResult2.getQuery(), PagerFilter.getUnlimitedFilter())
        //logit.info(searchResult.getResults().size())
        StringBuffer sBuf2 = new StringBuffer()
        logit.info("Reporter Results : " + searchResult.getResults().size())

    	searchResult.getResults().each { issue ->
        sBuf2.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setReporterId(newU.username)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
                if (adduser) {
    			IssueResult updateResult = issueService.update(user, updateValidationResult);
    			if (!updateResult.isValid())
    			{
        			sBuf2.append("Y, ")
    			}
                }
			}
	    }
        logit.info(sBuf2.toString())
        } else {
    		log.error("Invalid JQL: " + jqlSearch);
		}
        
    // move groups
    def groupManager = ComponentAccessor.groupManager
    def userUtil = ComponentAccessor.getUserUtil() 
    def grps = groupManager.getGroupsForUser(oldU)
    grps.each() {
        if (adduser) {
            if (!it.getName().contains("COMP_") && !it.getName().contains("ORG_") && !it.getName().contains("FnSec_")) {
				userUtil.removeUserFromGroup(it, oldU)
	        	groupManager.addUserToGroup(newU, it)
            }
    	    logit.info("moved " + it.name  + " from " + oldU + " to " + newU)
        } else {
            logit.info("will move " + it.name  + " from " + oldU + " to " + newU)
        }
    }
} else {
   	logit.info("Enter both user names to proceed")
}







