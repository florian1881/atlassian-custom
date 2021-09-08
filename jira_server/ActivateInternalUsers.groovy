import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.User
import org.apache.log4j.Logger
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.atlassian.jira.bc.user.search.UserSearchService
import com.atlassian.jira.bc.user.UserService
import com.atlassian.jira.bc.user.search.DefaultUserPickerSearchService
import com.atlassian.jira.bc.user.ApplicationUserBuilder;
import com.atlassian.jira.bc.user.UserService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.util.ErrorCollection;


UserSearchService userSearchService = ComponentAccessor.getComponent(UserSearchService)
def logit = Logger.getLogger("com.domain1.eu.logging")

// don't change IT User's application access
String ignoreIfInGroup = "Cheil IT UK"

def  usrService = ComponentAccessor.getComponent(UserService)

def userUtil = ComponentAccessor.getUserUtil() 
 
GroupManager groupManager = ComponentAccessor.getGroupManager()

UserManager userManager = ComponentAccessor.getUserManager()
def adminUser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()

ErrorCollection errorCollection
logit.info("size=" + userUtil.getAllApplicationUsers().size())
for (ApplicationUser user in userUtil.getAllApplicationUsers()) {
    
        // user found, internal directory and not in ignore group and email match
        if (user != null   && !user.active && user.directoryId == 1 && !groupManager.isUserInGroup(user, ignoreIfInGroup) && (user.emailAddress.contains("domain2") || user.emailAddress.contains("cylndr")|| user.emailAddress.contains("centrade"))) {
             String[] usergroups = groupManager.getGroupNamesForUser(user)
            if (usergroups.size() == 0)
           // {     
            //if (user.getDisplayName().contains("[Peng"))
           //{ 
            logit.info("internal:" + user.getDisplayName() + "/" + user.emailAddress + "/" + user.directoryId + "/" + usergroups.toString())
			UserService.DeleteUserValidationResult result = usrService.validateDeleteUser(adminUser, user)
 	         //logit.info(result.getErrorCollection().getErrorMessages().isEmpty())
            if (result.getErrorCollection().getErrorMessages().isEmpty())
            {
               //--- logit.info( user.emailAddress + "/" + user.directoryId + "/" + usergroups.toString())
            }
            //  if delete errors the user has links to issues so set active
            else {
            	logit.info(result.getErrorCollection().getErrorMessages())
                ApplicationUserBuilder applicationUserBuilder = usrService.newUserBuilder(user);
                applicationUserBuilder.active(true);

                ApplicationUser userForValidation = applicationUserBuilder.build();
                UserService.UpdateUserValidationResult updateUserValidationResult = usrService.validateUpdateUser(userForValidation);
                if (updateUserValidationResult.isValid()) {
                     usrService.updateUser(updateUserValidationResult);
                     logit.info("internal update")
                 } else {
                     errorCollection = updateUserValidationResult.getErrorCollection();
                      logit.info(errorCollection)
                 }
            }
        }
   }

    
//}
