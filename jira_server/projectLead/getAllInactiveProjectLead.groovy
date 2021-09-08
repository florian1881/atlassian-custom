import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Level
import org.apache.log4j.Logger
import com.atlassian.auiplugin.*

def projectManager = ComponentAccessor.projectManager
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def issueManager = ComponentAccessor.getIssueManager()
def searchService = ComponentAccessor.getComponent(SearchService)
def inactiveProjectLeadList = []
def log = Logger.getLogger(getClass())
def baseURL = "https://myjira.com"
log.setLevel(Level.DEBUG)

projectManager.projects.findAll { project ->
// JQL criteria to search within projects. If it returns anything, the project DOESN'T get archived
def jqlSearch = 'project in ("'+"${project.key}"+'")'
def parseResult = searchService.parseQuery(user, jqlSearch)

if (!parseResult.valid) {
log.warn("The JQL '${jqlSearch}' is not valid. Parse result: ${parseResult.errors}")
return false
}
    if(!project.getProjectLead()?.active){
        inactiveProjectLeadList.push([project.key,project.name,project.getProjectLead(),project.getProjectLead()?.getEmailAddress(),project.getProjectLead()?.active,project.getId()])
    }
}

def result = """
<div class="aui-message aui-message-change">
    <p class="title">
        <strong>To get the correct result running the request you need to be part of the group "jira-administrator"</strong>
        <br>
    </p>
    <p>You can add yourself on this group into Crowd</p>
    <p style="text-align:center"><strong><a href="https://mydoc.com">Documentation</a></strong></p>
</div>
<h2>Projects with Inactive Project Lead : ${inactiveProjectLeadList.size()} </h2>
<table class="aui">
	<thead>
        <tr>
        	<th>Project key</th>
            <th>Project name</th>
           	<th>Confluence estimated link</th>
            <th>Project Lead</th>
            <th>Project Lead Mail adress</th>
            <th>Project Lead Status </th>
            <th>Project Issues</th>
        </tr>
    </thead>
    <tbody>
"""
inactiveProjectLeadList.forEach{project ->
result += """
<tr>
	<td><a href="${baseURL}/jira/plugins/servlet/project-config/${project[0]}/roles" target="_blank">${project[0]}</a></td>
	<td><a href="${baseURL}/jira/plugins/servlet/project-config/${project[0]}/roles" target="_blank">${project[1]}</a></td>
	<td><a href="${baseURL}/confluence/spaces/spacepermissions.action?key=${project[0]}" target="_blank">Confluence possible - ${project[0]}</a></td>
    <td><a href="${baseURL}/jira/secure/ViewProfile.jspa?name=${project[2]?.name}" target="_blank"><span class="aui-icon aui-icon-small aui-iconfont-person-circle"></span>${project[2]}</a></td>
	<td>${project[4] ? "<span class='aui-lozenge aui-lozenge-success'>ACTIVE</span>" :"<span class='aui-lozenge aui-lozenge-error'>INACTIVE</span>"} </td>
    <td><a href="mailto:${project[3]}">${project[3]}</a></td>
	<td><a href="${baseURL}/jira/projects/${project[0]}/issues" target="_blank">${issueManager.getIssueCountForProject(project[5])}</a></td>
</tr>
"""
}
result +='<tbody></table>'

return result