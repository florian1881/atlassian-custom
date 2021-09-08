import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Level
import org.apache.log4j.Logger
import com.atlassian.auiplugin.*
import javax.ws.rs.core.MediaType
import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import javax.script.*

def projectManager = ComponentAccessor.projectManager
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def issueManager = ComponentAccessor.getIssueManager()
def searchService = ComponentAccessor.getComponent(SearchService)
def projectsList = []
def baseURL = "https://myjira.com"
def log = Logger.getLogger(getClass())
log.setLevel(Level.DEBUG)

projectManager.projects.findAll { project ->
// JQL criteria to search within projects. If it returns anything, the project DOESN'T get archived
def jqlSearch = 'project in ("'+"${project.key}"+'")'
def parseResult = searchService.parseQuery(user, jqlSearch)

if (!parseResult.valid) {
log.warn("The JQL '${jqlSearch}' is not valid. Parse result: ${parseResult.errors}")
return false
}
projectsList.push([project.key,
project.name,
project.getProjectLead(),
project.getProjectLead()?.getEmailAddress()
,project.getProjectLead()?.active
,searchService.searchCount(user, parseResult.query)
,project.getId()])
}
def result = """
<button class="aui-button" id="button-script">Launch script</button>
<h2>Total project number : ${projectsList.size()} </h2>
    <table class="aui aui-table-sortable" id="delayedSortedTable">
    	<thead>
            <tr>
                <th>Project Key</th>
                <th>Project Name</th>
                <th>Confluence estimated link</th>
                <th>Project Lead</th>
                <th>Project Lead Mail adress</th>
                <th>Project Lead Status </th>
                <th>Total issue number </th>
            </tr>
        </thead>
        <tbody>
"""
projectsList.forEach{project ->
result += """
    <tr>
        <td><a href="${baseURL}/jira/plugins/servlet/project-config/${project[0]}/roles" target="_blank">${project[0]}</a></td>
        <td><a href="${baseURL}/jira/plugins/servlet/project-config/${project[0]}/roles" target="_blank">${project[1]}</a></td>
        <td><a href="${baseURL}/confluence/spaces/spacepermissions.action?key=${project[0]}" target="_blank">Confluence possible - ${project[0]}</a></td>
    	<td><a href="${baseURL}/jira/secure/ViewProfile.jspa?name=${project[2]?.name}" target="_blank"><span class="aui-icon aui-icon-small aui-iconfont-person-circle"></span>${project[2]}</a></td>
        <td>${project[4] ? "<span class='aui-lozenge aui-lozenge-success'>ACTIVE</span>" :"<span class='aui-lozenge aui-lozenge-error'>INACTIVE</span>"} </td>
        <td><a href="mailto:${project[3]}">${project[3]}</a></td>
        <td><a href="${baseURL}/jira/projects/${project[0]}/issues" target="_blank">${issueManager.getIssueCountForProject(project[6])}</a></td>
    </tr>
"""
}
result +="""</tbody></table>"""
return result 