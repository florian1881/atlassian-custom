import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import com.atlassian.jira.bc.projectroles.ProjectRoleService  
import com.atlassian.jira.util.SimpleErrorCollection
import com.onresolve.scriptrunner.parameters.annotation.ShortTextInput
import com.onresolve.scriptrunner.parameters.annotation.ProjectPicker


def logit = Logger.getLogger("com.domain1.eu.logging")

long projectId = 0

//ProjectComponentManager pComMgr = ComponentAccessor.getProjectComponentManager()
ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                         
def errorCollection = new SimpleErrorCollection()
//ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)


ProjectManager projectManager = ComponentAccessor.getProjectManager()


@ProjectPicker(description = "Target Project Key", label = "Target Project Key")
Project targetProject
@ProjectPicker(description = "Source Project Key", label = "Source Project")
Project sourceProject
if (targetProject && sourceProject) {


Collection<String> actorCollection = new ArrayList<>();
       Collection roles = projectRoleService.getProjectRoles(errorCollection)
        if (errorCollection.hasAnyErrors()) {
            logit.info(errorCollection.getErrors())
        }
    
        roles.each() {ProjectRole r ->
           ProjectRoleActors actors = projectRoleService.getProjectRoleActors(r, sourceProject, errorCollection)
            logit.info(actors)
            if (!actors.getRoleActors().empty)
            {
              
                Set<RoleActor> players = actors.getRoleActors()
                players.each() {RoleActor player ->
                    //logit.info("actor " + player)
                    if (player.getType().equalsIgnoreCase(ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE) ) {
                    	actorCollection.add(player.getParameter())
                    	//logit.info("actor " + actorCollection)

                        projectRoleService.addActorsToProjectRole(actorCollection, r, 
                                                                  targetProject, 
                                                                  ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE , 
                                                                  errorCollection) 
                    	actorCollection.clear()
                    } 
                }
    		
                logit.info("add " + actors + " to " + r + "/" + targetProject) 
            }    
        }
        
} else {
    logit.info("Enter project details")
}





