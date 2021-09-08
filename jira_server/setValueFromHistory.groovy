import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import java.sql.Timestamp
import java.util.Date
import org.apache.log4j.Level
import org.apache.log4j.Logger
log = Logger.getLogger("com.acme.CreateIssue")
log.setLevel(Level.DEBUG)


def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
def searchProvider = ComponentAccessor.getComponent(SearchProvider)
def issueManager = ComponentAccessor.getIssueManager()
//def issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey("PROPER-225")
//def cfvalue = ComponentAccessor.getChangeHistoryManager().getChangeItemsForField(issue, "deadline")?.last().from
//log.debug(cfvalue)

def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def query = jqlQueryParser.parseQuery("project = 'Испытательный срок' AND deadline = empty")
def results = searchProvider.search(query, user, PagerFilter.getUnlimitedFilter())
log.debug("Total issues: ${results.total}")
results.getIssues().each { documentIssue ->
  def issue = issueManager.getIssueObject(documentIssue.id)
	def cf = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_10500")
	def cfvalue = ComponentAccessor.getChangeHistoryManager().getChangeItemsForField(issue, "deadline")?.last().from
	def pattern = "yyyy-MM-dd'T'HH:mm:ssZ"
	def date = Date.parse(pattern, cfvalue)
	Timestamp toTimestamp = date.toTimestamp()
	issue.setCustomFieldValue(cf, toTimestamp)
	issueManager.updateIssue(user, issue, EventDispatchOption.DO_NOT_DISPATCH, false)
  log.debug(issue)
	log.debug(toTimestamp)
}
