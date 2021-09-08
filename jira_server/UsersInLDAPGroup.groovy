import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
//import com.atlassian.jira
def log = Logger.getLogger("com.domain1.eu.logging")
// the name of the new group
def groups = [
    "COMP_YDB1":""]
 
StringBuilder builder = new StringBuilder()
groups.each(){
Group groupIn = ComponentAccessor.groupManager.getGroup(it.key)//.createGroup(groupName)
log.info("source : " + groupIn)
Group groupOut = ComponentAccessor.groupManager.getGroup(it.value)
log.info("target : " + groupOut)
Collection<ApplicationUser> inUsers = ComponentAccessor.groupManager.getUsersInGroup(groupIn)
    inUsers.each(){
        builder.append(it.getEmailAddress() + ",")
       // log.info(it.getEmailAddress())
        //ComponentAccessor.groupManager.addUserToGroup(it, groupOut)
    }
}
log.info(builder.toString())
log.info("EOF")
