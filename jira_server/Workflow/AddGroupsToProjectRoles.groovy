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
set your ["project","role","group"] here
****************
*/

def data = [
["P620200069","Administrators","CHEIL HQ"],
["P620200069","Administrators HQ","Support Team"],
["P620200069","DMM","seil-dmm"],
["P620200069","SDS - ITC","SDS"],
["P620200069","WSC - WPC managers","HQ Support Team"],
["P620200069","WSC - WPC members","Artience"],
["P620200069","WSC - WPC members","GMC"],
["P620200069","WSC - WPC members","P6 OPTS"],
["P620200069","WSC - WPC members","RSC"],
["P620200069","WSC - WPC members","SDS"],
["P620200069","WSC - WPC members","WMC PMO"],
["P620200069","WSC - WPC members","WPC CN"],
["P620200069","WSC - WPC members","WPC PL"],
["P620200069","WSC - WPC members","Wisewire QA"],
["P620200069","WSC - WPC members","seil-lpm"],
["WSC20200069","Administrators","CHEIL HQ"],
["WSC20200069","Administrators HQ","Support Team"],
["WSC20200069","DMM","seil-dmm"],
["WSC20200069","SDS - ITC","SDS"],
["WSC20200069","WSC - WPC managers","HQ Support Team"],
["WSC20200069","WSC - WPC members","Artience"],
["WSC20200069","WSC - WPC members","GMC"],
["WSC20200069","WSC - WPC members","WSC OPTS"],
["WSC20200069","WSC - WPC members","RSC"],
["WSC20200069","WSC - WPC members","SDS"],
["WSC20200069","WSC - WPC members","WMC PMO"],
["WSC20200069","WSC - WPC members","WPC CN"],
["WSC20200069","WSC - WPC members","WPC PL"],
["WSC20200069","WSC - WPC members","Wisewire QA"],
["WSC20200069","WSC - WPC members","seil-lpm"]
]

def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                         
Project project
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectRole role
ProjectManager pMgr = ComponentAccessor.getProjectManager()

data.each() {List roles ->
    logit.info(roles[0])
    project = pMgr.getProjectByCurrentKey((String)roles[0])
    role = projectRoleManager.getProjectRole((String)roles[1])
    Collection<String> actorCollection = new ArrayList<>();
	actorCollection.add((String)roles[2])
    projectRoleService.addActorsToProjectRole(actorCollection,
    	role,
        project,
        ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE ,
        errorCollection)
	
    logit.info("add " + actorCollection + " to " + role + "/" + project)
}

