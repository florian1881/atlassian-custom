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

def logit = Logger.getLogger("com.domain1.eu.logging")


def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                         

def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)

ProjectManager pMgr = ComponentAccessor.getProjectManager()
def proj = pMgr.getProjectObjects()
proj.each() {Project p ->
    if (p.key.toString().contains("WSC") ||p.key.toString().contains("P6")  ){
    
       Collection roles = projectRoleService.getProjectRoles(errorCollection)
        roles.each(){ProjectRole r ->
           ProjectRoleActors actors=   projectRoleService.getProjectRoleActors(r, p, errorCollection)
            if (!actors.getRoleActors().empty)
            {
                actors.getRoleActors().collect().each() {RoleActor ra ->
                    logit.info("<~>" + p.key + "," + r.getName() + "," + ra.getType() + "," + ra.getDescriptor())
                }
            }
        } 
    }
}
