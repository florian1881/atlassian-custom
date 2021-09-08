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
/*
**************
set your role name and project here
****************
*/
String[] roleNames = ["Clients", "Project Managers", "Project Members","WSC - WPC members", "WSC - WPC managers", "Administrators"]
String[] projectNames  = [
"P620200001","P620200002","P620200003","P620200004","P620200005","P620200006","P620200007",
"P620200008","P620200009","P620200010","P620200011","P620200012","P620200013","P620200014",
"P620200015","P620200016","P620200017","P620200019","P620200020","P620200021","P620200022",
"P620200023","P620200024","P620200025","P620200026","P620200027","P620200028","P620200029",
"P620200030","P620200031","P620200032","P620200033","P620200034","P620200035","P620200036",
"P620200039","P620200043","P620200044","P620200047","P620200048","P620200049","P620200050",
"P620200051","P620200052","P620200053","P620200054","P620200055","P620200056","P620200057",
"P620200058","P620200059","P620200060","P620200061","P620200062","P620200063","P620200064",
"P620200065","P620200066","P620200067",
"WSC20200001","WSC20200003","WSC20200004","WSC20200005","WSC20200006","WSC20200007","WSC20200008",
"WSC20200009","WSC20200010","WSC20200011","WSC20200012","WSC20200013","WSC20200015","WSC20200016",
"WSC20200017","WSC20200018","WSC20200019","WSC20200020","WSC20200021","WSC20200022","WSC20200023",
"WSC20200024","WSC20200025","WSC20200026","WSC20200027","WSC20200028","WSC20200029","WSC20200030",
"WSC20200031","WSC20200032","WSC20200033","WSC20200034","WSC20200035","WSC20200036","WSC20200039",
"WSC20200043","WSC20200044","WSC20200046","WSC20200047","WSC20200048","WSC20200051","WSC20200053",
"WSC20200054","WSC20200055","WSC20200056","WSC20200057","WSC20200058","WSC20200059","WSC20200060",
"WSC20200061","WSC20200062","WSC20200063","WSC20200064","WSC20200065","WSC20200066","WSC20200067"]
//"KRS18686028"]

def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                         
Project project
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectRole role
ProjectManager pMgr = ComponentAccessor.getProjectManager()
Collection<String> actorCollection = new ArrayList<>();
actorCollection.add("Cheil IT DE")

projectNames.each() { projectName ->
	project = pMgr.getProjectByCurrentKey((String)projectName)
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
 
