import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.atlassian.jira.bc.user.search.UserSearchService

Logger  logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
String[] userList = [
]
// **********************************************

import com.atlassian.crowd.embedded.api.CrowdService
//def logit = Logger.getLogger("com.domain1.eu.logging")
import com.atlassian.crowd.manager.directory.DirectoryManager
final directoryToCheck = 'Cheil Worldwide US'
final groupName = 'jira-software-users'
def groupManager = ComponentAccessor.groupManager
def userManager = ComponentAccessor.userManager
def directoryManager = ComponentAccessor.getComponent(DirectoryManager)
def directoryId = directoryManager.findAllDirectories()?.find { it.name.toString().toLowerCase() == directoryToCheck.toLowerCase() }?.id
def jiraUsersGroup = groupManager.getGroup(groupName)

// Get all users that belong to JIRA Internal Directory
def allDirectoryUsers = userManager.allApplicationUsers.findAll { it.directoryId == directoryId }

def usersBelongToGroup = allDirectoryUsers?.findAll { groupManager.isUserInGroup(it, jiraUsersGroup) }
def userUtil = ComponentAccessor.getUserUtil() 
UserSearchService userSearchService = ComponentAccessor.getComponent(UserSearchService)

//def users 
// for each user to process
usersBelongToGroup.each() {
   //String name = (String)it
 
    
    	def users = userSearchService.findUsersByEmail(it.emailAddress)
    	if (users.size()== 0) {
    	} else {
                users.each(){user ->
		String[] groups = userUtil.getGroupNamesForUser(user.getName())
    logit.info("" + user + "," + (user.active?"active":"inactive") + ',"' + groups + '"')

                }         }
}
 



