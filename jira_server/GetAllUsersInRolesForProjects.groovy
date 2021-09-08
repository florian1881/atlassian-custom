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
   
def logit = Logger.getLogger("com.domain1.eu.logging")
 
 
//UserUtil userUtil = ComponentAccessor.getUserUtil()
/*
**************
set your role name and project here
****************
*/
String[] roleNames = [ "Administrators","Clients", "Project Managers", "Project Members","WSC - WPC members", "WSC - WPC managers"]
String[] projectNames  = [
    "KRS18686001",
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
"WSCHUBBLE"/*,
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
"WSCSEUK",
"WSCSESG"

 */                      
]
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectRole role
ProjectManager pMgr = ComponentAccessor.getProjectManager()
Map<String,Map> projects  = new HashMap<>();
projectNames.each() { projectName ->
    
		def project = pMgr.getProjectByCurrentKey((String)projectName)
    	logit.info(projectName)
     	Map<String,List> roles  = new HashMap<>();
  	    roleNames.each() { roleName ->             
    	role = projectRoleManager.getProjectRole((String)roleName)
        if (role){
        	List<String> ul = new ArrayList()
	   		ProjectRoleActors actors = projectRoleManager.getProjectRoleActors(role, project)
	   		Set<ApplicationUser> users = actors.getUsers()
	   		users.each() {
    	   		ApplicationUser u = (ApplicationUser)it
           		ul.add( u.emailAddress)
 			}	
            roles.put((String)roleName, ul)
        }
    }
    projects.put((String)projectName, roles)
}
    
logit.info(projects)
