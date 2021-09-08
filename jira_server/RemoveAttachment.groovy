import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.AttachmentManager

def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
def searchProvider = ComponentAccessor.getComponent(SearchProvider)
def issueManager = ComponentAccessor.getIssueManager()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def attachmentManager = ComponentAccessor.getAttachmentManager()

def query = jqlQueryParser.parseQuery("created >= 2015-01-01 AND created <= 2017-12-31 AND text ~ 'ЗАО ТВТорг' AND attachments is not EMPTY")
def results = searchProvider.search(query, user, PagerFilter.getUnlimitedFilter())

results.getIssues().each{ documentIssue ->
    def issue = issueManager.getIssueObject(documentIssue.id)
    log.debug(issue.number)    
    def attachments = ComponentAccessor.attachmentManager.getAttachments(issue)
    attachments.each {attachment ->
 		  attachmentManager.deleteAttachment(attachment)
    }    
}
