import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.event.type.EventDispatchOption


def groupManager = ComponentAccessor.getGroupManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueManager = ComponentAccessor.getIssueManager()
//def issue = issueManager.getIssueObject("NGP-1003")
def systemUser = ComponentAccessor.userManager.getUserByName("system")


def userGroup = groupManager.getGroup("软件组Task评审") 
def List<ApplicationUser> userListInGroup = groupManager.getUsersInGroup(userGroup).toList()

log.warn("1:"+userListInGroup)

Collection cfs = customFieldManager.getCustomFieldObjectsByName("负责人")
CustomField cf = cfs[0]
def cfValueObject = issue.getCustomFieldValue(cf) as ApplicationUser
if(!groupManager.isUserInGroup(cfValueObject, '软件组')){
    return 
}

log.warn("2:"+userListInGroup)
log.warn("3:"+cfValueObject)

if(userListInGroup.indexOf(cfValueObject) != -1){
	log.warn(userListInGroup.remove(userListInGroup.indexOf(cfValueObject)))
}
log.warn("4:"+userListInGroup)

Collection userList1cfs = customFieldManager.getCustomFieldObjectsByName("用户列表1")
CustomField userList1cf = userList1cfs[0]

issue.setCustomFieldValue(userList1cf,userListInGroup)
//issueManager.updateIssue(systemUser, issue, EventDispatchOption.DO_NOT_DISPATCH, false)


