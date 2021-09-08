
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

import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.issue.index.IssueIndexingParams
import com.atlassian.jira.issue.index.IndexException

def log = Logger.getLogger("com.domain1.logging")

// **********************************************

// **********************************************
String oldUser = "omerbarisonal@gmail.com"
String newUser = "omer.onal@domain1.com"
def jqlSearch = 'issueFunction in commented("by ' + oldUser + '")'
List<Issue> issues = null
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()


//def userUtil = ComponentAccessor.getUserUtil() 
def commentMgr = ComponentAccessor.commentManager
UserManager userMgr = ComponentAccessor.getUserManager()
ApplicationUser newAppUser = userMgr.getUserByName(newUser)
ApplicationUser oldAppUser = userMgr.getUserByName(oldUser)
log.info("old user "  + oldAppUser)
List<Comment> comments
MutableComment mut
IssueManager issueManager = ComponentAccessor.getIssueManager()
def searchResult
SearchService.ParseResult parseResult =  searchService.parseQuery(user, jqlSearch)
if (parseResult.isValid()) {
   // searchService.search()
    searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
    
    // Transform issues from DocumentIssueImpl to the "pure" form IssueImpl (some methods don't work with DocumentIssueImps)
    searchResult.getResults().each { issue ->
    def issueData = issueManager.getIssueObject(issue.id)
    log.info (issueData.key)
    comments=  commentMgr.getCommentsForUser(issueData, oldAppUser)
        log.info("comments " + comments.size())
    	comments.each {comment ->
        if (commentMgr.isUserCommentAuthor(oldAppUser, comment)) {
        	log.info("author: " + comment.getAuthorApplicationUser())
            mut=commentMgr.getMutableComment(comment.getId())
            //log.info(mut.getBody())
            
            mut.setAuthor(newAppUser)
            try {
            	commentMgr.update(mut, true)
               
            } catch (Exception e)
            {
                log.info(e.getMessage())
            }
            log.info("newAuthor: " + mut.getAuthorApplicationUser())
            }
        }
    }
   // issues = searchResult.issues.collect {issueManager.getIssueObject(it.id)}
} else {
    log.error("Invalid JQL: " + jqlSearch);
}
