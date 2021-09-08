import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager

def projectManager = ComponentAccessor.projectManager
def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def user = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()

def allProjects = projectManager.getProjects()
def adminProjects = []

allProjects.each{
    def projectRoles = projectRoleManager.getProjectRoles(user, it)
    if(projectRoles.find(){it.getName() == "Developer"}){
        adminProjects.push(it.key)
    }
}

return (adminProjects)
