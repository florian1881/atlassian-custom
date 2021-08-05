import com.atlassian.jira.issue.Issue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.model.ChangeGroup
import com.atlassian.jira.model.ChangeItem
import com.atlassian.jira.issue.history.ChangeItemBean
import com.atlassian.jira.issue.comments.CommentManager
import com.valiantys.nfeed.api.IFieldDisplayService;
import com.onresolve.scriptrunner.runner.customisers.PluginModule;
import com.onresolve.scriptrunner.runner.customisers.WithPlugin;
import com.atlassian.jira.event.type.EventDispatchOption
import groovy.util.XmlSlurper
import org.apache.log4j.Logger
import org.apache.log4j.Level
  
def log = Logger.getLogger("com.acme.CreateSubtask")
log.setLevel(Level.INFO)
  
@WithPlugin("com.valiantys.jira.plugins.SQLFeed")
@PluginModule
IFieldDisplayService fieldDisplayService;

def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

def saName = "Service Area Team"
def change = event?.getChangeLog()?.getRelated("ChildChangeItem")?.find {it.field == saName}
//log.info("Value changed from ${change.oldstring} to ${change.newstring}")

if (change) {

def issue = (MutableIssue)event.issue

def saDBCF = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Service Area").id
Object serviceArea = fieldDisplayService.getDisplayResult(issue.getKey(), saDBCF);
if (serviceArea == null) {
    return
}
def commentManager = ComponentAccessor.getCommentManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def saRawCF = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName(saName)
def saRaw = issue.getCustomFieldValue(saRawCF)[0] as ApplicationUser//.getName() as String


//def list = new XmlSlurper().parseText(saRaw) as groovy.util.slurpersupport.GPathResult
  def groupManager = ComponentAccessor.getGroupManager()
def stepUsers = groupManager.getUsersInGroup(saRaw.getName())

String comment = "Service Area in ${issue.getKey()} was updated to [${saRaw}:${saRaw.getClass()}] -- " + serviceArea.getDisplay()+"\n"+ stepUsers
CommentManager commentMgr = ComponentAccessor.getCommentManager()
commentMgr = (CommentManager) ComponentAccessor.getComponentOfType(CommentManager.class)
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
commentMgr.create(issue, currentUser, comment, true)

  //issue.assigneeId= "IROTeam"
  issue.setAssignee(saRaw)
  def stepUsersCF = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Step Team")
  issue.setCustomFieldValue(stepUsersCF,stepUsers)
  ComponentAccessor.getIssueManager().updateIssue(user, issue, EventDispatchOption.DO_NOT_DISPATCH, false)

}
//def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()
//def changeItems = changeHistoryManager.getChangeItemsForField(issue,'SomeDate')