import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole

def projectManager = ComponentAccessor.projectManager
def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def adminuser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()

// def allProjects = projectManager.getProjects()
def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
// list the user groups to process
String[] groups = ["jira-software-users"]

// add 'groups' members to this group
//String addToGroupName = "xxProduction Test Team"

// remove 'groups' memebers from this group
String removeFromGroupName = "jira-software-users"

// don't change IT User's application access
String ignoreIfInGroup = "Cheil IT UK"
// **********************************************

ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 
 
GroupManager groupManager = ComponentAccessor.getGroupManager()
//Group addToGroup = groupManager.getGroup(addToGroupName)
Group removeFromGroup = groupManager.getGroup(removeFromGroupName)

UserManager userManager = ComponentAccessor.getUserManager()
// for each group to process
for (String groupName in groups) {
	// get the Group object
    Group group = groupManager.getGroup(groupName)
	// for each user in group
    for (String userId in groupManager.getUserNamesInGroup(group)) {
        user = userManager.getUserByName(userId)
        // user found, internal directory and not in ignore group
        if (user != null   && user.directoryId == 1 && !groupManager.isUserInGroup(user, ignoreIfInGroup)) {
            String[] usergroups = groupManager.getGroupNamesForUser(user)
            if (usergroups.size() == 1)
            {          
                logit.info( user.emailAddress + "/" + user.directoryId + "/" + usergroups.toString())
				// remove user from removeFromGroup
            userUtil.removeUserFromGroup(removeFromGroup, user)
            }
 		} 
    }
}
    
//}
