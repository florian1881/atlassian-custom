
import com.atlassian.jira.issue.comments.MutableComment
import com.atlassian.jira.issue.comments.Comment
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager

import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.bc.issue.search.SearchService 
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.issue.index.IssueIndexingParams
import com.atlassian.jira.issue.index.IndexException
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder

def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************

// **********************************************

def jqlSearch = '(category = P6 OR category = WSC) AND "Issue management (old)" is not EMPTY'
List<Issue> issues = null
def customFieldManager = ComponentAccessor.getCustomFieldManager()
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()


//def userUtil = ComponentAccessor.getUserUtil() 
def commentMgr = ComponentAccessor.commentManager
UserManager userMgr = ComponentAccessor.getUserManager()
// Get a pointer to my Demo Text Field
def textField = customFieldManager.getCustomFieldObjectByName("Issue management (old)");
def outField = customFieldManager.getCustomFieldObjectByName("Issue management");

IssueManager issueManager = ComponentAccessor.getIssueManager()
def searchResult
SearchService.ParseResult parseResult =  searchService.parseQuery(user, jqlSearch)
if (parseResult.isValid()) {

    searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
    
     searchResult.getResults().each { issue ->
    def issueData = issueManager.getIssueObject(issue.id)
    logit.info (issueData.key +  " + "  + textField.getValue(issue).toString()+  " + "  + outField.getValue(issue).toString())

    double d = Double.parseDouble(textField.getValue(issue).toString())
    outField.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(outField), d), new DefaultIssueChangeHolder())
    }
   // issues = searchResult.issues.collect {issueManager.getIssueObject(it.id)}
} else {
    log.error("Invalid JQL: " + jqlSearch);
}
