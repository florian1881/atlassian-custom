import com.atlassian.jira.component.ComponentAccessor

def workflowManager = ComponentAccessor.workflowManager
def schemeManager = ComponentAccessor.workflowSchemeManager

def sb = new StringBuffer()

workflowManager.workflows.each {
    if(!it.systemWorkflow) {
        def schemes = schemeManager.getSchemesForWorkflow(it)
        if (schemes.size() == 0) {
            sb.append("Deleting workflow: ${it.name}\n")
            workflowManager.deleteWorkflow(it)
        }
    }
}