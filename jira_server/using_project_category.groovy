import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.component.ComponentAccessor
//Test para Console
//def issueKey = "UDAP-17"
//def issueManager = ComponentAccessor.getIssueManager()
//def issue = issueManager.getIssueObject(issueKey)
//def issue = issue as Issue
log.warn("issue: "+ issue)

def projectManager = ComponentAccessor.getProjectManager()
def projectCategory = projectManager.getProjectCategoryForProject(issue.getProjectObject())

log.warn(projectCategory.name)

if (projectCategory.name == "VersionFlow-A")
	return 1
else
    return 0
