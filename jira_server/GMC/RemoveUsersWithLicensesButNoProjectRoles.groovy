import com.atlassian.jira.component.ComponentAccessor
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
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager

import org.apache.log4j.Logger
def logit = Logger.getLogger("com.domain1.eu.logging")

ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
//SimpleErrorCollection errorCollection
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
UserUtil userUtil = ComponentAccessor.userUtil
def groupManager = ComponentAccessor.groupManager

def groups = [
    "jira-software-users"
]
 
Group inactive = groupManager.getGroup("jira-inactive-users")
Group license = groupManager.getGroup("jira-software-users")

String[] userGroups
groups.each(){
	Group groupIn = groupManager.getGroup(it)

	if (groupIn) {
	//logit.info("source : " + groupIn.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){ApplicationUser user ->
            //errorCollection = new SimpleErrorCollection()
            //projectRoleService.
            userGroups = groupManager.getGroupNamesForUser(user)
            String remove = (userGroups && userGroups.size() == 1 )?"remove":"valid"
            String roles = getRoles(user.username, userGroups)
            //if (remove.compareTo("remove") == 0) 
            logit.info(remove + " " +user + " " + user.active + "  " + userGroups + " " + roles)
  /*
            if (roles.empty) {
        		groupManager.addUserToGroup(user, inactive)
            	userUtil.removeUserFromGroup(license, user)
            }
            */
 
    	}
	}
}

def String getRoles(String username, String[] usergroups) {
    StringBuilder output = new StringBuilder();
ProjectManager projectManager = ComponentAccessor.getProjectManager();
ProjectRoleService projectRoleService = (ProjectRoleService) ComponentAccessor.getComponentOfType(ProjectRoleService.class);
ProjectRoleManager projectRoleManager = (ProjectRoleManager) ComponentAccessor.getComponentOfType(ProjectRoleManager.class);

final Collection<ProjectRole> projectRoles = projectRoleManager.getProjectRoles();
for(Project project : projectManager.getProjectObjects())
{
    for(ProjectRole projectRole: projectRoles)
    {
        final ProjectRoleActors projectRoleActors = projectRoleManager.getProjectRoleActors(projectRole, project);
        for (RoleActor actor : projectRoleActors.getRoleActors()) {
            if(actor.getDescriptor().equals(username) || usergroups.contains(actor.getDescriptor())){
            
                output.append(project.getKey()).append(" : ").append(projectRole.getName()).append(" : ").append(actor.getDescriptor()).append("\n");
            }
        }
    }
}
return output.toString();
}
