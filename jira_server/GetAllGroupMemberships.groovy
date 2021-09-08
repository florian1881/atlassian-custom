import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
 
def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
String[] userList = [

]
// **********************************************

ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 
UserManager userManager = ComponentAccessor.getUserManager()
GroupManager groupManager = ComponentAccessor.getGroupManager()

Collection<Group> groups = groupManager.getAllGroups()
logit.info "Groups"
groups.each() {Group grp ->
	logit.info grp.name
}

groups.each() {Group grp ->
	Collection<String> grpUsers =   groupManager.getUserNamesInGroup(grp)
    grpUsers.each() {String u ->
    	user = userManager.getUser(u)
    	if (user!= null && user.active) {
    	// logit.info("Added " + it)
    	logit.info(grp.name + ", " + u)
    	}
  	}
}



