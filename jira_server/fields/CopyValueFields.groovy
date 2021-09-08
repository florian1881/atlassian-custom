import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.link.IssueLinkManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.link.IssueLink
import com.atlassian.jira.issue.Issue

//def issue = ComponentAccessor.getIssueManager().getIssueObject('TII-562')
def issue = event.getIssueLink().getSourceObject()
def issueLinkManager = ComponentAccessor.getIssueLinkManager()
def cfm = ComponentAccessor.getCustomFieldManager()
def changeHolder = new DefaultIssueChangeHolder()

// Получаем связанные заадчи у задачи с типом "Blocks"
//def issueLinks = issueLinkManager.getInwardLinks(issue.getId())
def issueLinks = issueLinkManager.getOutwardLinks(issue.getId())


def links = issueLinks.findAll {
    it.getIssueLinkType().getName() == "⑴ Связывание"
}

for(link in links){
    log.error('Номер связанной задачи ' + link.destinationObject.getKey())
    def component = link.getDestinationObject().getComponents()
    issue.setComponent(component)
    log.debug ( "Компоненты в тестинге:" + component)
    def label = link.getDestinationObject().getLabels()
    issue.setLabels(label)
    log.debug ( "Метки в тестинге:" + label)
    def fixVersions = link.getDestinationObject().getFixVersions()
    issue.setAffectedVersions(fixVersions)
    log.debug ( "Fix Version в тестинге:" + fixVersions)
    def priority = link.getDestinationObject().getPriority()
    issue.setPriority(priority)
    log.debug ( "Priority в тестинге:" + priority)
    def cfmm = ComponentAccessor.getCustomFieldManager()
    def cf = cfmm.getCustomFieldObject('customfield_13401')
    def value = link.getDestinationObject().getCustomFieldValue(cf)
    //def priority = link.getDestinationObject().getPriority()
    issue.setCustomFieldValue(cf, value)
    log.debug ( "Группы специалистов в тестинге:" + value)
    def analyst = cfmm.getCustomFieldObject('customfield_10514')
    def valueAnalyst = link.getDestinationObject().getCustomFieldValue(analyst)
    issue.setCustomFieldValue(analyst, valueAnalyst)
    log.debug ( "Аналитег в тестинге:" + valueAnalyst)

}
def user = ComponentAccessor.getJiraAuthenticationContext().loggedInUser
def im = ComponentAccessor.getIssueManager()
im.updateIssue(user, issue, EventDispatchOption.ISSUE_UPDATED, false)
