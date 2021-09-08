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
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.user.util.UserUtil
/* *********************************
Put all role members into split_<grp> groups for identification
Remove roles from projects in category processed
*  ********************************** */

Logger logit = Logger.getLogger("com.domain1.eu.logging")
GroupManager groupManager = ComponentAccessor.getGroupManager()
Group group
UserManager userManager = ComponentAccessor.getUserManager()
//String [] categories = ["Canada", "Deutsch", "DIA", "France" , "IT projects", "Italy", "Nordic", "Poland", "Retail Process", "SSO2", "UK", "Ukraine"] 
String [] categories = ["WSC", "P6", "BOLD"]
// SSO1
def projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)
def currentUser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()
UserUtil userUtil = ComponentAccessor.userUtil
String splitRoles = "split_UserRoles" // hold users in direct roles
Group split
split = groupManager.getGroup(splitRoles)
log.info(split)
if (!split) {
    split = groupManager.createGroup(splitRoles)
}
 
def errorCollection = new SimpleErrorCollection()
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectManager pMgr = ComponentAccessor.getProjectManager()
//def proj = pMgr.getProjectObjects()
Collection<String> actorNames
categories.each() {category ->
	def p6 = pMgr.getProjectCategoryObjectByName(category)
	def proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
	proj.each() {Project p ->
       Collection roles = projectRoleService.getProjectRoles(errorCollection)
        if (errorCollection.hasAnyErrors()) {
            logit.info(errorCollection.getErrors())
        }
        roles.each() {ProjectRole r ->
           ProjectRoleActors actors=   projectRoleService.getProjectRoleActors(r, p, errorCollection)
            if (!actors.getRoleActors().empty)
            {
                actors.getRoleActors().collect().each() {RoleActor ra ->
                    boolean include = true
                    //def license= false
                    def directoryid = "N/A"
                    // user in role
                    errorCollection = new SimpleErrorCollection()
                    if (ra.getType().contentEquals("atlassian-user-role-actor")) {
                        Set<ApplicationUser> users = ra.getUsers()
                        users.each() {
                            //logit.info("user " + it.name + " " + p + " " + r)                           
                            actorNames = new ArrayList<String>()
                            actorNames.add(it.name)
                            projectRoleService.removeActorsFromProjectRole(actorNames, r, p, ra.getType().toString(), errorCollection)
                            //logit.info(errorCollection.errorMessages)
                            try {
                            	groupManager.addUserToGroup(it, split)
                            } catch (Exception e) {
                                logit.info(e.getMessage())
                            }
                            logit.info("add " + it + " to " + split.name + " " + p + " " + r)  
                            if (errorCollection.hasAnyErrors()) {
                                logit.info(errorCollection.errorMessages)
                            }
                        }
                   } else {
                       // group in role
                        errorCollection = new SimpleErrorCollection()
                        //logit.info(ra.getDescriptor() + " " + p + " " + r) 
                        Group grp = groupManager.getGroup(ra.getDescriptor())
                        
                        String splitName = "split_" + ra.getDescriptor()
                        if (grp) {
                            //logit.info(grp.name)
                            Group splitgrp
                            splitgrp = groupManager.getGroup(splitName)
                            if (!splitgrp) {
                                splitgrp = groupManager.createGroup("split_"+ra.getDescriptor())
                            }
                            def users = groupManager.getUsersInGroup(grp)
                            users.each() {ApplicationUser u ->
                        		groupManager.addUserToGroup(u, splitgrp)
                                // do not remove users from original groups until later
            					//userUtil.removeUserFromGroup(grp, u)
                                logit.info("move " + u + " from " + grp.name + " to " + splitgrp.name)
                            }
                            actorNames = new ArrayList<String>()
                            actorNames.add(grp.name)
                            projectRoleService.removeActorsFromProjectRole(actorNames, r, p, ra.getType().toString(), errorCollection)
                            if (errorCollection.hasAnyErrors()) {
                                logit.info(errorCollection.errorMessages)
                            }
                        }
                    }
               }
            }
        }
    }
}
