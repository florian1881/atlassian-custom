
//import com.atlassian.jira.issue.comments.MutableComment
//import com.atlassian.jira.issue.comments.Comment
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
import com.onresolve.scriptrunner.parameters.annotation.Checkbox


def logit = Logger.getLogger("com.domain1.eu.logging")
@Checkbox(label = "Make changes", description = "Check to apply changes, Uncheck to preview changes")
boolean apply
String mode = (apply && apply == true)?"Apply":"Preview"
// **********************************************
// user replacements
// **********************************************
// key : value
// old : new
def users = [
"e.viss@domain2.com":"e.viss@domain1.com",
"a.zwada@domain2.com":"a.zwada@domain1.com",
"a.zwolak@domain2.com":"a.zwolak@domain1.com",
"j.kowalski@domain2.com":"j.kowalski@domain1.com"
]
String oldUser
String newUser
String assigneeclause1 = 'assignee in ( "' 
String reporterclause1 = 'reporter in ( "' 
String clause2 = '")'

// set true to add key (lefthand value), set false to add value (righthand value)
boolean adduser = true
def jqlSearch 
List<Issue> issues = null
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
UserManager userMgr = ComponentAccessor.getUserManager()
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
IssueManager issueManager = ComponentAccessor.getIssueManager()

IssueService issueService = ComponentAccessor.issueService

def searchResult

users.each() {
    if (adduser) {
        oldUser = it.key
        newUser = it.value
    } else {
        newUser = it.value
        oldUser = it.key
    }
    ApplicationUser oldU = userMgr.getUser(oldUser)
    ApplicationUser newU = userMgr.getUser(newUser)
    logit.info(((oldU == null)?"MISSING old ":"old ")  + oldUser + " " + oldU + ((newU == null)? " MISSING new ":" new ") + newUser + " " + newU)  


    // ASSIGNEE
    jqlSearch = assigneeclause1 + oldUser + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
    	searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
        StringBuffer sBuf1 = new StringBuffer()
    	searchResult.getResults().each { issue ->
           sBuf1.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setAssigneeId(newUser)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
                if (apply) {
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
    jqlSearch = reporterclause1 + oldUser + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult2 =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
   	searchResult = searchService.search(user, parseResult2.getQuery(), PagerFilter.getUnlimitedFilter())
        //logit.info(searchResult.getResults().size())
        StringBuffer sBuf2 = new StringBuffer()
    	searchResult.getResults().each { issue ->
        sBuf2.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setReporterId(newUser)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
                if (apply) {
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
        if (apply) {
  			if (!it.getName().contains("COMP_") && !it.getName().contains("ORG_") && !it.getName().contains("FnSec_")) {
				userUtil.removeUserFromGroup(it, oldU)
	    	   	groupManager.addUserToGroup(newU, it)
    		    logit.info("moved " + it.name  + " from " + oldU + " to " + newU)       
    		}    
        }
        else {
            logit.info("to move " + it.name  + " from " + oldU + " to " + newU)  
        }
    }
    
    
    }





