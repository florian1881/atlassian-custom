import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group

def logit = Logger.getLogger("com.domain1.eu.logging")

def groups = [
"COMP_YDB0"
]
 
def groupManager = ComponentAccessor.groupManager
groups.each(){
Group groupIn = groupManager.getGroup(it)
if (groupIn) {
	logit.info("source : " + groupIn.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){
            
        	logit.info(groupIn.getName()  + "," +it.displayName + "," + it.username + "," +  it.emailAddress)
    	}
	}
}
