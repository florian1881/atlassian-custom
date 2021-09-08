import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
 import com.atlassian.jira.bc.user.search.UserSearchService
def logit = Logger.getLogger("com.domain1.eu.logging")
UserSearchService userSearchService = ComponentAccessor.getComponent(UserSearchService)
// **********************************************
// Enter a list of usernames
String[] userList = [
"e.viss@domain2.com"

]
// **********************************************

ApplicationUser user

UserManager userManager = ComponentAccessor.getUserManager()
// for each user to process
userList.each() {userid ->
    user = userManager.getUserByName((String)userid)
	//logit.info(user)
    if (user == null ) {
        logit.info("not found " + userid)
        def users = userSearchService.findUsersByEmail((String)userid)
        users.each() {
        	logit.info("id : " + userid + ", email search : " +it)
        }
    } else {
   		logit.info("found " + user)// + "/") + (user.active?"active":"inactive"))
    }
}
 


