import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.User
import org.apache.log4j.Logger
import com.atlassian.jira.bc.user.UserService
//import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.crowd.embedded.api.UserWithAttributes
import com.atlassian.crowd.embedded.api.CrowdService
import com.atlassian.jira.bc.user.search.UserSearchService
import com.atlassian.jira.bc.JiraServiceContextImpl
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.onresolve.scriptrunner.parameters.annotation.*
import com.onresolve.scriptrunner.parameters.annotation.meta.Option

def logit = Logger.getLogger("com.domain1.eu.logging")

@ShortTextInput(description = "Search will check for user emails ending with this text", label = "Email ends with? ")
String emailEnd
def userUtil = ComponentAccessor.getUserUtil()
CrowdService crowdService = ComponentAccessor.crowdService
GroupManager groupManager = ComponentAccessor.getGroupManager()

//crowdService.

Collection<ApplicationUser> users
UserSearchParams.Builder paramBuilder = UserSearchParams.builder()
        .allowEmptyQuery(true)
        .includeActive(true)
        .includeInactive(true).maxResults(10000)

ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
JiraServiceContextImpl jiraServiceContext = new JiraServiceContextImpl(loggedInUser)

users = ComponentAccessor.getComponent(UserSearchService).findUsers(jiraServiceContext, "", paramBuilder.build())
Date d
logit.info("size=" + users.size())
users.each(){user ->
        if (user != null   && user.directoryId == 10200 && user.getEmailAddress().endsWith(emailEnd)) { //10200 = external vLDAP, 10301 US
            String[] usergroups = groupManager.getGroupNamesForUser(user)
            //if (usergroups.size() > 0) {
             UserWithAttributes userPlus 
            try  {
            userPlus = crowdService.getUserWithAttributes(user.getName())
            } catch (Exception e) {
                logit.info(e.getMessage())
            }
             String lastLoginMillis = userPlus.getValue('login.lastLoginMillis')
             d = null
             if (lastLoginMillis?.isNumber()) { 
                d = new Date(Long.parseLong(lastLoginMillis))
            }
            logit.info(emailEnd + "," + user.getDisplayName() +  "," + user.getUsername()  + ","+ user.emailAddress + "," + d + ",Groups:" + usergroups.toString())
       // }
        }
    }
