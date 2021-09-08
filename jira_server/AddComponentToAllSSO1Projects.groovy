import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.project.component.ProjectComponent
import com.atlassian.jira.bc.project.component.ProjectComponentManager

/* new component */
//String componentName = "EE"
//String description = "desc"
long projectId = 0

ProjectComponentManager pComMgr = ComponentAccessor.getProjectComponentManager()
ProjectComponent component
def projectManager = ComponentAccessor.getProjectManager()

String[] projects = ["WSC20200020",
"WSC20200001",
"WSC20200019",
"WSC20200021",
"WSC20200022"
]
					 
String[] components = ["01 Task - Issue & Inquiry Only",
"02 PD Registration",
"03 PD Modification",
"04 PD Update (Spec)",
"05 MKT PD Creation",
"06 MKT PD Modification",
"07 QA",
"08 Static Page",
"09 Offer & Promotion",
"10 Home Page",
"11 GNB",
"12 Footer",
"13 PCD / PFS / PF",
"14 About Us",
"15 Accessibility",
"16 Info - Sitemap",
"17 DTM Rule",
"gnm",
"homepage"
]
					 

projects.each() {String projectName ->
    projectId = projectManager.getProjectByCurrentKey(projectName).getId()
    component.each() {String componentName ->
    component = pComMgr.create(componentName, null, null , 0, projectId)
    }
}

