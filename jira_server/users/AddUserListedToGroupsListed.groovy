import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
 
def log = Logger.getLogger("com.domain1.logging")

// **********************************************
String[] userList = [
"m.bartosiak@domain1.com",
"m.binkiewicz@domain1.com",
"o.bowzyk@domain1.com",
"m.bratukhin@domain1.com",
"m.celuch@domain1.com"

]
// **********************************************

String[] groups = ["WPC PL"]
ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 
UserManager userManager = ComponentAccessor.getUserManager()
GroupManager groupManager = ComponentAccessor.getGroupManager()


// for each user to process
userList.each() {
    user = userManager.getUserByName((String)it)
	
    log.info("" + user + "/" + (user.active?"active":"inactive"))
    // String[] groups = userUtil.getGroupNamesForUser(user.getName())
    groups.each() {
        log.info(it)
        if (user.active) {
            Group removeFromGroup = groupManager.getGroup((String)it)
            userUtil.AddUserToGroup(removeFromGroup, user)
            log.info("Added " + user.displayName + " to  " + removeFromGroup.getName())
        }
    }
}
