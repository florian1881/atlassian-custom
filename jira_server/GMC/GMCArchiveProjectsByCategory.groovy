import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.project.archiving.ArchivedProjectService

Logger logit = Logger.getLogger("com.domain1.eu.logging")
ArchivedProjectService archivedProjectService = ComponentAccessor.getComponent(ArchivedProjectService)

String [] categories = ["Canada", "Deutsch", "DIA", "France" , "IT projects", "Italy", "Nordic", "Poland", "Retail Process", "SSO1" ,"SSO2", "UK", "Ukraine"] 
//String [] categories = ["WSC", "P6", "BOLD"]
def currentUser = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()
def errorCollection = new SimpleErrorCollection()
ProjectManager pMgr = ComponentAccessor.getProjectManager()
categories.each() {category ->
   
	def p6 = pMgr.getProjectCategoryObjectByName(category)
	def proj =  pMgr.getProjectObjectsFromProjectCategory(p6.getId())
    logit.info(proj.size())
	proj.each() {Project p ->

		try {
    		def projectKey = p.key
            logit.info("key " + projectKey)
        
        	//final ProjectService.DeleteProjectValidationResult result = projectService.validateDeleteProject(currentUser, projectKey)
        	//if (result.isValid()) {
                ArchivedProjectService.ValidationResult  validationResult = archivedProjectService.validateArchiveProject(currentUser, projectKey);
            

				if (validationResult.isValid()) {
    				archivedProjectService.archiveProject(validationResult);
					logit.info(" project " + projectKey + " correctly archived");
        		} else {
            		logit.info("project " + projectKey + " wrongly archived " + validationResult.getErrorCollection().getErrors());
        		}
 
  		} catch (Exception e) {
       		logit.info("Exception with project "+ p.key + "\n"+ e);
     	}
	}

}
