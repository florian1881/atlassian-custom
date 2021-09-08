import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.bc.projectroles.ProjectRoleService  
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.user.util.UserUtil

def logit = Logger.getLogger("com.domain1.eu.logging")

ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
//SimpleErrorCollection errorCollection
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
UserUtil userUtil = ComponentAccessor.userUtil
def groupManager = ComponentAccessor.groupManager

def groups = [
    "jira-software-users"
]
 
Group inactive = groupManager.getGroup("jira-inactive-users")
Group license = groupManager.getGroup("jira-software-users")

String[] userGroups
groups.each(){
	Group groupIn = groupManager.getGroup(it)

	if (groupIn) {
	//logit.info("source : " + groupIn.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){ApplicationUser user ->
            //errorCollection = new SimpleErrorCollection()
            //projectRoleService.
            userGroups = groupManager.getGroupNamesForUser(user)
            logit.info("" +user + "  " + userGroups)
            /*
            boolean deactivate = true 
            if (groupManager.isUserInGroup(user, inactive)) {
                def grps = groupManager.getGroupsForUser(user)
                String list = ""
                grps.each() {
                    list = list + " " + it.name + ","
                }
                logit.info(""  + user.username + " " + inactive.name + " " + list)
            }
            */

 
    	}
	}
}
