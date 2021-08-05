import org.apache.log4j.Category
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueInputParameters
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.issuetype.IssueType
import com.atlassian.jira.issue.IssueImpl
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.ModifiedValue

//def issue = ComponentAccessor.getIssueManager().getIssueObject("ACO-12")
def issue = (MutableIssue)event.issue
if( issue?.status?.name != "Draft")return

def cf = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Project Issue Types")
def nit =  issue.getCustomFieldValue(cf).toString()
if(issue.issueType.name.toLowerCase() == nit.toLowerCase())return

/* Define a Logger */

def Category log = Category.getInstance("com.onresolve.jira.groovy.PostFunction")
log.setLevel(org.apache.log4j.Level.DEBUG)

def constantsManager = ComponentAccessor.getConstantsManager()

log.debug ("### Other method ###")
def newIssueType = ComponentAccessor.issueTypeSchemeManager.getIssueTypesForProject(issue.projectObject).find{ it.name == nit }

IssueService issueService = ComponentAccessor.getIssueService();

if (newIssueType){
    issue.setIssueTypeObject(newIssueType)
    
	def cfCN = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Client Name")
	def cn =  issue.getCustomFieldValue(cfCN).toString()
    issue.setSummary(nit+": "+cn)
    
    def changeHolder = new DefaultIssueChangeHolder();
	cf.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(cf), null), changeHolder);

    issue.store()
}

log.debug("#############################  IssueType changed from 'Bug' to 'Task' ########################################")
