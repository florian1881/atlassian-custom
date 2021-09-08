import com.atlassian.jira.component.ComponentAccessor
//import org.apache.log4j.Logger
//import org.apache.log4j.Level
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.version.Version
import com.atlassian.jira.project.version.VersionManager
import com.atlassian.jira.event.type.EventDispatchOption

def versionManager = ComponentAccessor.getVersionManager()
def projectManager = ComponentAccessor.getProjectManager()
def issueManager = ComponentAccessor.getIssueManager()
//def issueLinkManager = ComponentAccessor.getIssueLinkManager()
//def issue1 = issueManager.getIssueObject("NGP-181")

def parentIssueVersions = issue.getFixVersions()
//def log = Logger.getLogger("log")
//log.setLevel(Level.DEBUG)

issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue subtaskIssue = eachLink.getDestinationObject()
        Collection eachIssueVersions = subtaskIssue.getFixVersions();
        for (Version version: eachIssueVersions){
            //issue.setFixVersions(Arrays.asList(version))
            parentIssueVersions.add(version)
            //log.warn(version.getName())
        }
    }
}

issueLinkManager.getInwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "Blocks") {
        Issue blockLinkedIssue = eachLink.getSourceObject()
        Collection eachIssueVersions = blockLinkedIssue.getFixVersions();
        for (Version version: eachIssueVersions){
            //issue.setFixVersions(Arrays.asList(version))
            parentIssueVersions.add(version)
            //log.warn(version.getName())
        }
    }
}

issue.setFixVersions(parentIssueVersions)
