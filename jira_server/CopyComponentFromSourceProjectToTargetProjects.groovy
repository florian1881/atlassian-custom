import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.project.component.ProjectComponent
import com.atlassian.jira.bc.project.component.ProjectComponentManager
import org.apache.log4j.Logger

def logit = Logger.getLogger("com.domain1.eu.logging")

long projectId = 0

ProjectComponentManager pComMgr = ComponentAccessor.getProjectComponentManager()

def projectManager = ComponentAccessor.getProjectManager()

String[] targetProjects = [
    "P620200074",
    "P620200075",
    "P620200076"
]

String sourceProject = "P620200065"
					 

def sourceId =  projectManager.getProjectByCurrentKey(sourceProject).getId()
Collection<ProjectComponent> comps = pComMgr.findAllForProject(sourceId)

targetProjects.each() {String projectName ->
    projectId = projectManager.getProjectByCurrentKey(projectName).getId()
    comps.each() { ProjectComponent component ->
    	def test = pComMgr.findByComponentName(projectId, component.getName())
    	if (!test) {
    		def comp = pComMgr.create(component.getName(), null, null , 0, projectId)
            logit.info("Add " + component.getName() + " to " + projectName)
        } else {
            logit.info(component.getName() + " already exists in " + projectName)
        }
    }
}


