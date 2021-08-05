import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue

//def issue = ComponentAccessor.getIssueManager().getIssueObject("ACO-12")
//def issueTypes = ComponentAccessor.issueTypeSchemeManager.getIssueTypesForProject(issue.projectObject).collect{ [(it.name) : it.name] }

def projectKey = 'ACO';//getIssueContext().getProjectObject().getKey()
def projectManager = ComponentAccessor.getProjectManager()
def project = projectManager.getProjectByCurrentKey(projectKey)
def m = [:]
def issueTypes = ComponentAccessor.issueTypeSchemeManager.getIssueTypesForProject(project).each{ m [it.name] = it.name }

def pit = getFieldByName("Project Issue Types")
pit.setFieldOptions(m)
