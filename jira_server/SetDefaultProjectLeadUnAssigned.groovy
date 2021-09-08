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
import com.atlassian.jira.project.UpdateProjectParameters

def logit = Logger.getLogger("com.domain1.eu.logging")
 
String [] categories = ["WSC"] 
 
ProjectManager pMgr = ComponentAccessor.getProjectManager()

// for each category string
categories.each() {category ->
    // get the category
	def p6 = pMgr.getProjectCategoryObjectByName(category)
    // get all projects for category
	def proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
    // for each project
	proj.each() {Project p ->
        if (p.getLeadUserName().contentEquals("william.cho@domain2.com")) {
       logit.info(p.key + " assignee type: " + p.assigneeType)
        // get update params, set default assignee = unassigned ( option 3)
       def updateProjectParameters = UpdateProjectParameters.forProject(p.getId()).assigneeType(3)
       pMgr.updateProject(updateProjectParameters)
        }
    }
}
