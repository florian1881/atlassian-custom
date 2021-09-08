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
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.User

 
def logit = Logger.getLogger("com.domain1.eu.logging")
 
String [] keys = [
"SCIC",
"SET - TW",
"SENZ",
"SEIN",
"SEPCO",
"SME",
"SAVINA",
"TSE",
"SERC",
"SIEL",
"SEHK",
"SEKZ",
    "SEO",
"SEUC",
"SEIL - PS",
"SGE",
"SELV"
] 
logit.info("start")
ProjectManager pMgr = ComponentAccessor.getProjectManager()
def grpMgr = ComponentAccessor.getGroupManager()
def proj = pMgr.getProjectObjects()
	proj.each() {Project p ->
        //logit.info(p.getName())
        keys.each() {String key ->
            if (p.getName().contains(key)) {
                logit.info(key + "," + p.getName() + "," + p.getKey())
            } else {
               // logit.info(key + ",nomatch,")
            }
            
        }
        

    }
