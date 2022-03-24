package scriptrunner.console
 
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.user.util.UserManager
import org.apache.log4j.Level
import org.apache.log4j.Logger
 
String filename = "C:\\temp\\user_groups.20210818.csv"
GroupManager groupManager = ComponentAccessor.groupManager
UserManager userManager = ComponentAccessor.userManager
 
 
Logger log = Logger.getLogger("com.valiantys")
log.setLevel(Level.DEBUG)
File file = new File(filename)
if (file.exists()) {
    FileReader reader = new FileReader(file)
    reader.readLines().each { ug ->
        String[] ugt = ug.split(";")
        String username = ugt[0]
        String groupname = ugt[1]
        ApplicationUser user = userManager.getUserByName(username)
 
        if (user != null) {
            Group group = groupManager.getGroup(groupname)
 
            if (group == null) {
                group = groupManager.createGroup(groupname)
                log.debug("### Group ${groupname} has been created")
            }
 
            groupManager.addUserToGroup(user, group)
            log.debug("# User ${username} added to group ${groupname}")
 
        } else {
            log.debug "### User $username does not exist in directory"
        }
    }
 
} else {
    log.error "file error"
}
