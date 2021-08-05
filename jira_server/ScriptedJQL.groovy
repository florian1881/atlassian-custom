import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.ComponentManager
import com.atlassian.jira.issue.customfields.manager.OptionsManager

SearchService searchService = ComponentAccessor.getComponent(SearchService.class)

UserUtil userUtil = ComponentAccessor.getUserUtil()
User user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
IssueManager issueManager = ComponentAccessor.getIssueManager()
 
def componentManager = ComponentManager.instance
def optionsManager = ComponentManager.getComponentInstanceOfType(OptionsManager.class)
def customFieldManager = componentManager.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Fixed Issues JQL")
def myJQL = issue.getCustomFieldValue(cf)	// has a value such as "project = VOL and fixVersion = 6.0"
 
if (!user) {
    user = userUtil.getUserObject('kwhite')
}
 
List<Issue> issues = null
 
SearchService.ParseResult parseResult =  searchService.parseQuery(user, myJQL)
if (parseResult.isValid()) {
    def searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
    // Transform issues from DocumentIssueImpl to the "pure" form IssueImpl (some methods don't work with DocumentIssueImps)
    issues = searchResult.issues.collect {issueManager.getIssueObject(it.id)}
} else {
    log.error("Invalid JQL: " + myJQL);
}