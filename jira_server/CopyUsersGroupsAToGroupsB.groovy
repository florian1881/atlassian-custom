import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
//import com.atlassian.jira
def logit = Logger.getLogger("com.domain1.eu.logging")
// the name of the new group
// format :
// "Source group":"target group"
def groups = [
    "SDS":"jira-software-users",
    "WSC Team":"jira-software-users",
    "WPC PL":"jira-software-users",
    "WPC CN":"jira-software-users",
    "Cheil HQ":"jira-software-users"
]
 
def groupManager = ComponentAccessor.groupManager
groups.each(){
Group groupIn = groupManager.getGroup(it.key)//.createGroup(groupName)
if (groupIn) {
	logit.info("source : " + groupIn.name)
	Group groupOut = groupManager.getGroup(it.value)
    if (groupOut) {
		logit.info("target : " + groupOut.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){
        	logit.info("add " + it.displayName + " to " + groupOut.getName())
        	groupManager.addUserToGroup(it, groupOut)
    	}
	}
}
}
