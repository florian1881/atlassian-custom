import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import com.atlassian.jira.bc.projectroles.ProjectRoleService   
import com.atlassian.jira.util.SimpleErrorCollection

def logit = Logger.getLogger("com.domain1.eu.logging")

String[] roleNames = ["Global WPL"] //"Clients", "Project Managers", "Project Members","WSC - WPC members", "WSC - WPC managers", "Administrators"]

Collection<String> actorCollection = new ArrayList<>();
actorCollection.add("global-wpl")

def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                         
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectRole role
ProjectManager pMgr = ComponentAccessor.getProjectManager()

def p6 = pMgr.getProjectCategoryObjectByName("P6")
def p6Proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())

p6Proj.each() { project ->
	//project = pMgr.getProjectByCurrentKey((String)projectName)
  	roleNames.each() { roleName ->             
	role = projectRoleManager.getProjectRole((String)roleName)
	ProjectRoleActors actors = projectRoleManager.getProjectRoleActors(role, project)
	projectRoleService.addActorsToProjectRole(actorCollection,
    	role,
        project,
        ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE ,
        errorCollection)
	}
    logit.info("add " + actorCollection + " to " + role + "/" + project)
}

def wsc = pMgr.getProjectCategoryObjectByName("WSC")
def wscProj = pMgr.getProjectObjectsFromProjectCategory(wsc.getId())

wscProj.each() { project ->
	//project = pMgr.getProjectByCurrentKey((String)projectName)
  	roleNames.each() { roleName ->             
	role = projectRoleManager.getProjectRole((String)roleName)
	ProjectRoleActors actors = projectRoleManager.getProjectRoleActors(role, project)
	projectRoleService.addActorsToProjectRole(actorCollection,
    	role,
        project,
        ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE ,
        errorCollection)
	}
    logit.info("add " + actorCollection + " to " + role + "/" + project)
}
 
 
