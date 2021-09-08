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
def logit = Logger.getLogger("com.domain1.eu.logging")
import com.atlassian.crowd.manager.directory.DirectoryManager
final directoryToCheck = 'Cheilworldwide LDAP'
final groupName = 'jira-software-users'
def groupManager = ComponentAccessor.groupManager
def userManager = ComponentAccessor.userManager
//def loginManager = ComponentAccessor.getComponent(LoginService)
def directoryManager = ComponentAccessor.getComponent(DirectoryManager)
def directoryId = directoryManager.findAllDirectories()?.find { it.name.toString().toLowerCase() == directoryToCheck.toLowerCase() }?.id
def jiraUsersGroup = groupManager.getGroup(groupName)

// Get all users that belong to JIRA Internal Directory
def allDirectoryUsers = userManager.allApplicationUsers.findAll { it.directoryId == directoryId }

// Get all the users that belong to JIRA Directory and to group
def usersBelongToGroup = allDirectoryUsers?.findAll { groupManager.isUserInGroup(it, jiraUsersGroup) }

logit.info("users " + usersBelongToGroup.size())

//def userUtil = ComponentAccessor.getUserUtil()
//CrowdService crowdService = ComponentAccessor.crowdService

logit.info("Username, First Name, Last Name, Email Address")
int count
int count2
//logit.info("size=" + userUtil.getAllApplicationUsers().size())
for (ApplicationUser user in usersBelongToGroup) {
    def groups = groupManager.getGroupNamesForUser(user)
    groups.each() {String grp ->
        if (!grp.startsWith("COMP") && !grp.startsWith("ORG") && !grp.startsWith("Fn")) {
             logit.info(user.getUsername() + "," + grp)
        }
       // if (grp.name.)
            }
     //      count2++
      //      String[] names = user.getDisplayName().split(" ")
       //     def firstname = names[0]
        //        def lastname = ""
         //       if (names.size() > 1) {
          //          lastname = names[1]
           //     }
            //logit.info(user.getUsername() + "," + firstname + "," + lastname + "," + user.getEmailAddress() )
             //   count++
                       //user.getDisplayName() + "," + user.emailAddress + "," + d+ "," +(user.active?"active":"inactive") + ",Groups:" + usergroups.toString())
            }// }// } }
    count
count2
