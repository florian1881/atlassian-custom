import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.User
import org.apache.log4j.Logger
import com.atlassian.jira.bc.user.UserService
import com.atlassian.jira.bc.user.UserService;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.crowd.embedded.api.UserWithAttributes
import com.atlassian.crowd.embedded.api.CrowdService
def logit = Logger.getLogger("com.domain1.logging")
 
def userUtil = ComponentAccessor.getUserUtil()
CrowdService crowdService = ComponentAccessor.crowdService
GroupManager groupManager = ComponentAccessor.getGroupManager()
 
Date d
logit.info("size=" + userUtil.getAllApplicationUsers().size())
for (ApplicationUser user in userUtil.getAllApplicationUsers()) {
        if (user != null   && user.directoryId == 1 && !user.active) {
            String[] usergroups = groupManager.getGroupNamesForUser(user)
            UserWithAttributes userPlus = crowdService.getUserWithAttributes(user.getName())
             String lastLoginMillis = userPlus.getValue('login.lastLoginMillis')
             d = null
             if (lastLoginMillis?.isNumber()) { 
                d = new Date(Long.parseLong(lastLoginMillis))
            }
            logit.info("internal:-," + user.getDisplayName() + "," + user.emailAddress + "," + d+ "," +(user.active?"active":"inactive") + ",Groups:" + usergroups.toString())
        }
    }
