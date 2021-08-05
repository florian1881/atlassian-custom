import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.user.util.UserManager
import com.atlassian.crowd.embedded.api.User

//def issue = ComponentAccessor.getIssueManager().getIssueObject("RISK-5")

UserManager userManager = ComponentAccessor.getUserManager()
def m = [:]
m["<b><i>critical</i></b>"] = "ProgramManagementTeam"
m["<b><i>high</i></b>"] = "CountryLeadsTeam"
m["<b><i>medium</i></b>"] = "CountryLeadsTeam"
m["<b><i>low</i></b>"] = "RiskManagerTeam"

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cf = customFieldManager.getCustomFieldObjectByName("Inherent Risk")
if(cf==null)return
def risk = issue.getCustomFieldValue(cf)

def userKey = m[risk.toString().toLowerCase()]
def user = userManager.getUserByKey(userKey);
issue.setAssignee(user)
