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
 
String [] categories = ["WSC" , "P6"] 
def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
Map projectUsers
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
String license = "jira-software-users"
ProjectManager pMgr = ComponentAccessor.getProjectManager()
def grpMgr = ComponentAccessor.getGroupManager()
def proj = pMgr.getProjectObjects()
ApplicationUser user
UserManager userManager = ComponentAccessor.getUserManager()
logit.info("User,Project Key,Project Name,Category,Project Role,Group,Region,License,Active")
//categories.each() {String category ->
//	def p6 = pMgr.getProjectCategoryObjectByName(category)
//	def proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
	proj.each() {Project p ->
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
                           def grps = grpMgr.getGroupsForUser(it) 
                            region = "external"
                            grps.each(){grp ->
                                    if (grp.name.contains("COMP")) {
                                        region = grp.name
                                    }
                            }
                            if (uniqueUsers(projectUsers, it.name)) {
                           		logit.info(it.name + "," + p.key + "," + p.getName() + "," + category + "," + r.getName() + "," + ra.getDescriptor() + "," + region
                                          + "," + grpMgr.isUserInGroup(it, license) + "," + it.active)
                            }
                         }
                    } else {
                        actorType = "user"
                        user = userManager.getUserByName(ra.getParameter())
                        String active = user?user.active:""
                          def grps = grpMgr.getGroupsForUser(ra.getParameter()) 
                            region = "external"
                        	//logit.info(ra.toString() + "/" + grps.size())
                            grps.each(){grp ->
                                //logit.info(grp.name)
                                if (grp.name.contains("COMP")) {
                                   region = grp.name
                                }
                            }
                            if (uniqueUsers(projectUsers, ra.getDescriptor())) {
 									logit.info(ra.getDescriptor() + "," + p.key + "," + p.getName() + "," + category + "," + r.getName() 
                                               + "," + "direct" + "," + region
                                               + "," + grpMgr.isUserInGroup(user, license) + "," + active)
                                                   }
                    }
                   // logit.info("<££>" + p.key + "," + r.getName() + "," + actorType + "," + ra.getDescriptor())
                }
            }
        }
        // user, project, role, region
    }
//}
logit.info("User,Project Key,Project Name,Category,Project Role,Group,Region,License,Active")


def boolean  uniqueUsers(Map map,String actor) {
   boolean retval = true
    if (map.containsKey(actor)) {
        retval= false
    } else {
        map.put(actor, actor)
        retval = true
    }
}
