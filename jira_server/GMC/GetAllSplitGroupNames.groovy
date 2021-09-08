import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.bc.projectroles.ProjectRoleService  
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.user.util.UserUtil

def logit = Logger.getLogger("com.domain1.eu.logging")


def groupManager = ComponentAccessor.groupManager

 


def grps = groupManager.allGroups

grps.each(){Group g ->
    if (g.name.contains("split_")) {
        logit.info('"' + g.name + '",')
    }


      
    	
	
}
