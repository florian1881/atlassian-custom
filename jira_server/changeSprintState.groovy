import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.greenhopper.service.sprint.Sprint
import com.atlassian.greenhopper.service.sprint.SprintManager
import com.onresolve.scriptrunner.runner.customisers.PluginModuleCompilationCustomiser
import com.onresolve.scriptrunner.runner.customisers.WithPlugin

IssueService issueService = ComponentAccessor.getIssueService()
def issueManager = ComponentAccessor.getIssueManager()
def systemUser = ComponentAccessor.userManager.getUserByName("system")

def mutableIssue = issue
//def mutableIssue = issueManager.getIssueObject("NGP-922")

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueManager1 = ComponentAccessor.getIssueManager()
def optionsManager = ComponentAccessor.getOptionsManager()

Collection cfs = customFieldManager.getCustomFieldObjectsByName("Sprint")
CustomField cf = cfs[0]
def sprints = mutableIssue.getCustomFieldValue(cf) as Collection <Sprint>

for (Sprint s in sprints)
{
    updateSprintState(s, Sprint.State.CLOSED) //Sprint.State.ACTIVE, Sprint.State.CLOSED 
}

@WithPlugin("com.pyxis.greenhopper.jira")
def sprintServiceOutcome = PluginModuleCompilationCustomiser.getGreenHopperBean(SprintManager).getAllSprints()

def updateSprintState(Sprint sprint, Sprint.State state) {

    def sprintManager = PluginModuleCompilationCustomiser.getGreenHopperBean(SprintManager)
    def newSprint = sprint.builder(sprint).state(state).build()
    def outcome = sprintManager.updateSprint(newSprint)

    if (outcome.isInvalid()) {
        log.debug "Could not update sprint with name ${sprint.name}, ${outcome.getErrors()}"
    } else {
        log.debug "${sprint.name} updated."
    }
}