import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import com.atlassian.jira.bc.projectroles.ProjectRoleService  
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.jira.bc.project.ProjectService.DeleteProjectValidationResult
import com.atlassian.jira.bc.project.ProjectService.DeleteProjectResult
import com.atlassian.jira.bc.project.ProjectService

Logger logit = Logger.getLogger("com.domain1.eu.logging")
GroupManager groupManager = ComponentAccessor.getGroupManager()
//Group group
//UserManager userManager = ComponentAccessor.getUserManager()

ProjectService projectService = ComponentAccessor.getComponent(ProjectService)

//String [] categories = ["Canada", "Deutsch", "DIA", "France" , "IT projects", "Italy", "Nordic", "Poland", "Retail Process", "SSO2", "UK", "Ukraine"] 
String [] categories = ["WSC", "P6", "BOLD"]
// SSO1
def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)
def currentUser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()
/*
UserUtil userUtil = ComponentAccessor.userUtil
String splitRoles = "splitUserRoles"
Group split
split = groupManager.getGroup(splitRoles)
log.info(split)
if (!split) {
    split = groupManager.createGroup(splitRoles)
}
 */
def errorCollection = new SimpleErrorCollection()
//ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectManager pMgr = ComponentAccessor.getProjectManager()
//def proj = pMgr.getProjectObjects()
//Collection<String> actorNames
categories.each() {category ->
	def p6 = pMgr.getProjectCategoryObjectByName(category)
	def proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
    logit.info(proj.size())
	proj.each() {Project p ->

		try {
    		def projectKey = p.key
            logit.info("key " + projectKey)
        
        	final ProjectService.DeleteProjectValidationResult result = projectService.validateDeleteProject(currentUser, projectKey)
        	if (result.isValid()) {
        		final ProjectService.DeleteProjectResult projectResult = projectService.deleteProject(currentUser, result);
        		if (projectResult.isValid()) {
        			logit.info(" project " + projectKey + " correctly deleted");
        		} else {
            		logit.error("project " + projectKey + " wrongly deleted "  + projectResult.getErrorCollection().getErrors());
        		}
     		}
                //break
  		} catch (Exception e) {
       		logit.error("Exception with project "+ p.key + "\n"+ e);
                //break
    	}
	}

}
