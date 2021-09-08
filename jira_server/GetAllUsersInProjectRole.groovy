import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import org.apache.log4j.FileAppender
import org.apache.log4j.Appender
import org.apache.log4j.PatternLayout
import org.apache.log4j.Level
import com.atlassian.jira.user.util.UserUtil
   
def logit = Logger.getLogger("com.domain1.logging")
 
 
UserUtil userUtil = ComponentAccessor.getUserUtil()
/*
**************
set your role name and project here
****************
*/
// HQ data names
String roleName = "Project Administrators" //"WSC - WPC members"
String[] projectNames  = ["KRS18686001",
"KRS18686002",
"KRS18686003",
"KRS18686004",
"KRS18686006",
"KRS18686007",
"KRS18686008",
"KRS18686009",
"KRS18686010",
"KRS18686011",
"KRS18686012",
"KRS18686013",
"KRS18686014",
"KRS18686015",
"KRS18686016",
"KRS18686017",
"KRS18686018",
"KRS18686020",
"KRS18686021",
"KRS18686022",
"KRS18686023",
"KRS18686024",
"KRS18686025",
"KRS18686026",
"KRS18686027",
"KRS18686028",
"KRS18686029",
"KRS18686030",
"KRS18686031",
"KRS18686032",
"KRS18686033",
"KRS19D90001",
"KRS19D90002",
"WSC20QAWW",
"WSCBLOOM",
"WSCHUBBLE",
"WSCSEAD",
"WSCSEAG",
"WSCSEB",
"WSCSEBN",
"WSCSECZ",
"WSCSEF",
"WSCSEG",
"WSCSEGR",
"WSCSEH",
"WSCSEI",
"WSCSENA",
"WSCSEROM",
"WSCSESA",
"WSCSESG",
"WSCSEUK"]
//String projectName
  
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectRole role
ProjectManager pMgr = ComponentAccessor.getProjectManager()
def map = [: ]

projectNames.each() { 

Project project = pMgr.getProjectByCurrentKey(it)
    if (project) {
		role = projectRoleManager.getProjectRole(roleName)
 
		ProjectRoleActors actors = projectRoleManager.getProjectRoleActors(role, project)
 
		Set<ApplicationUser> users = actors.getApplicationUsers()
		users.each() {
    		ApplicationUser u = (ApplicationUser)it
            if (!map.containsKey(it.emailAddress)) {
                map.put(it.emailAddress, it.emailAddress + "," + it.getUsername() +  "," + u.displayName + "," + (u.active?"active":"***inactive"))
    			//logit.info("" + it.emailAddress + "," + it.getUsername() +  "," + u.displayName + "," + (u.active?"active":"***inactive"))
            }
		}
    }
}

def list = map.entrySet().asList()
list.each() {
    logit.info(it)
}
