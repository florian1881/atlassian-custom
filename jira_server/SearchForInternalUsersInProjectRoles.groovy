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
 
String [] categories = ["WSC"] // , "P6"] 
def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
Map projectUsers
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
UserManager userManager = ComponentAccessor.getUserManager()
ApplicationUser appUser
ProjectManager pMgr = ComponentAccessor.getProjectManager()
def grpMgr = ComponentAccessor.getGroupManager()
// def projs = pMgr.getProjectObjects()
categories.each() {String categoryName ->
	def p6 = pMgr.getProjectCategoryObjectByName(categoryName)
	def projs = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
	projs.each() {Project p ->
        def cat = p.getProjectCategory()
        def category
        if (!cat) {category = "none"} else { category = cat.getName()}
        projectUsers = [:]
       Collection roles = projectRoleService.getProjectRoles(errorCollection)
        if (errorCollection.hasAnyErrors()) {
            logit.info(errorCollection.getErrors())
        }
        roles.each() {ProjectRole r ->
           ProjectRoleActors actors =   projectRoleService.getProjectRoleActors(r, p, errorCollection)
            if (!actors.getRoleActors().empty)
            {
               String actorType = null
                String region = null
                actors.getRoleActors().collect().each() {RoleActor ra ->
                    //logit.info(ra.getType().compareTo("atlassian-group-role-actor"))
                    if (ra.getType().compareTo("atlassian-group-role-actor") == 0) {
                        actorType = "group"
                       
                        def users = grpMgr.getUsersInGroup(ra.getDescriptor())
                        users.each() {ApplicationUser it ->
                           long dir = it.directoryId
                           def grps = grpMgr.getGroupsForUser(it) 
                            
                            if (uniqueUsers(projectUsers, it.name)) {
                                if (dir == 1) {
                           		logit.info(it.name + "," + p.key + "," + p.getName() + "," + category + "," + r.getName() + "," + ra.getDescriptor() + "," + region )
                                }
                            }
                         }
                    } else {
                        actorType = "user"
                          def grps = grpMgr.getGroupsForUser(ra.getParameter()) 
                            region = "external"
                        	//logit.info(ra.toString() + "/" + grps.size())
                            grps.each(){grp ->
                                //logit.info(grp.name)
                                if (grp.name.contains("COMP")) {
                                   region = grp.name
                                }
                            }
                      	appUser = userManager.getUserByName(ra.getParameter().trim())
                       // ra.getParameter()
                        //logit.info("" + appUser + "/" + ra.getDescriptor())
                            if (uniqueUsers(projectUsers, ra.getDescriptor())) {
                                if (appUser && appUser.directoryId == 1 ) {
 									logit.info(ra.getDescriptor() + "," + p.key + "," + p.getName() + "," + category + "," + r.getName() + "," + "direct" + "," + region)
                                } else {
                                    
                                }
                                                   }
                    }
                   // logit.info("<££>" + p.key + "," + r.getName() + "," + actorType + "," + ra.getDescriptor())
                }
            }
        }
        // user, project, role, region
    }
}

def boolean  uniqueUsers(Map map,String actor) {
   boolean retval = true
    if (map.containsKey(actor)) {
        retval= false
    } else {
        map.put(actor, actor)
        retval = true
    }
}
