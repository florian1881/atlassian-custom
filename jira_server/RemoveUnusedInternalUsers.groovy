import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.atlassian.jira.bc.user.UserService


def projectManager = ComponentAccessor.projectManager
def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def adminUser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()

// def allProjects = projectManager.getProjects()
def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
// list the user groups to process
String[] groups = ["jira-software-users"]


String ignoreIfInGroup = "Cheil IT UK"
// **********************************************
def userSearchService = ComponentAccessor.getUserSearchService()
def  usrService = ComponentAccessor.getComponent(UserService)

//ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 

GroupManager groupManager = ComponentAccessor.getGroupManager()

UserManager userManager = ComponentAccessor.getUserManager()

logit.info("size=" + userUtil.getAllApplicationUsers().size())
for (ApplicationUser user in userUtil.getAllApplicationUsers()) {
    

    if (user != null   && user.directoryId == 1 ){
             String[] usergroups = groupManager.getGroupNamesForUser(user)
          
                userUtil.removeUser(adminUser, user)
                
                UserService.DeleteUserValidationResult result = usrService.validateDeleteUser(adminUser, user)
                
                 if (result.getErrorCollection().getErrorMessages().isEmpty())
                {
                    logit.info( user.emailAddress + "/" + user.directoryId + "/" + usergroups.toString())
                }
            }
}
