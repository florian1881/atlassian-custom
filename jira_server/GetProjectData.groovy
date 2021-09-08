import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager

import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
 
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.User

 
def logit = Logger.getLogger("com.domain1.eu.logging")
 
def errorCollection = new SimpleErrorCollection()
ProjectManager pMgr = ComponentAccessor.getProjectManager()
def proj = pMgr.getProjectObjects()
ApplicationUser user
logit.info("Project Key,Project Name,Category,Lead")
//categories.each() {String category ->
//	def p6 = pMgr.getProjectCategoryObjectByName(category)
//	def proj = pMgr.getProjectObjectsFromProjectCategory(p6.getId())
	proj.each() {Project p ->
        def cat = p.getProjectCategory()
        def category
        if (!cat) {category = "none"} else { category = cat.getName()}
        logit.info(p.getKey() + "," + p.getName() + "," + category + "," + p.getLead().getName())
        p.getLead()
 
    }
//}
logit.info("Project Key,Project Name,Category,Lead")

