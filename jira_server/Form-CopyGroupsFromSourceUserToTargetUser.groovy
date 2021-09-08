import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
import com.onresolve.scriptrunner.parameters.annotation.UserPicker
import com.onresolve.scriptrunner.parameters.annotation.Checkbox

def logit = Logger.getLogger("com.domain1.eu.logging")


@UserPicker(label = "Source User", description = "Get a list of group memberships for this user")
ApplicationUser source
@UserPicker(label = "Target User", description = "Add this user to the list of groups")
ApplicationUser target
@Checkbox(label = "Make changes", description = "Check to apply changes, Uncheck to preview changes")
boolean apply
String mode = (apply && apply == true)?"Apply":"Preview"
if (source && target) {

	logit.info("**** Mode : " + mode + " changes")

	def groupManager = ComponentAccessor.groupManager
	def groups = groupManager.getGroupsForUser(source)
	groups.each(){Group groupIn ->
	    if (!groupIn.getName().contains("COMP_") && !groupIn.getName().contains("ORG_") && !groupIn.getName().contains("FnSec_")) {
			//logit.info("source : " + groupIn.name)
	    	if (apply && apply == true) {
	        	logit.info(mode + " " + target.displayName + " to " + groupIn.getName())
	        	groupManager.addUserToGroup(target, groupIn)
	    	} else {
    	    	logit.info(mode + " " + target.displayName + " to " + groupIn.getName())        
   			}
		}
	}
} else {
   	logit.info("Enter both user names to proceed")
}
