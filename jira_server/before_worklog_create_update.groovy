import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.*
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.worklog.Worklog
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.link.IssueLink

def worklogManager = ComponentAccessor.getWorklogManager()
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def issue = worklog.getIssue()


//检查当前状态
//---------------------------------------------------------------------------------------------
def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()
def tovalue
def changefield
def currentStatus = ""

for (int i = changeHistoryManager.getAllChangeItems(worklog.getIssue()).size()-1; i>=0; i--){
    def change  = changeHistoryManager.getAllChangeItems(worklog.getIssue()).get(i)
	  tovalue = change.getTos()
	  changefield = change.field
    if (changefield  == "status"){
        break
    }
}
for (Map.Entry<String, String> entry : tovalue.entrySet()) {
	    currentStatus = entry.getValue()
}

if (currentStatus.contains("等待") ||
    currentStatus.contains("完成") ||
    currentStatus.contains("协作") ||
    currentStatus.contains("排队") ||
    currentStatus.contains("发布")){
    return "当前状态是"+currentStatus+"，无法添加工时。"
  }
//---------------------------------------------------------------------------------------------


//检查当前人员的当天累计工时+当前输入工时
//---------------------------------------------------------------------------------------------
def theDate = new Date().format('yyyyMMdd') ;
def parsedDate = new Date().parse("yyyyMMdd", theDate);

List<Worklog> WorklogList = worklogManager.getWorklogsUpdatedSince(parsedDate.getTime())
ArrayList<Worklog> processingWorklog = new ArrayList()
ArrayList<Worklog> currentIssueWorklog = new ArrayList()

for (Worklog worklog in WorklogList){
  if (worklog.getAuthorObject() == currentUser){
    processingWorklog.add(worklog)
    if(worklog.getIssue() == issue){
      currentIssueWorklog.add(worklog)
    }
  }
}

Long totalSpentTime = 0

for (Worklog worklog in processingWorklog){
    if(worklog.getStartDate().getTime() > parsedDate.getTime()){
    	totalSpentTime += worklog.getTimeSpent()
    }
}

if ((totalSpentTime + worklogResult.getWorklog().getTimeSpent())/3600 > 16){
  return "帅哥，你今天的工时已经达到了创纪录的16小时！真的假的？！"
}
//---------------------------------------------------------------------------------------------

//检查今天是否已登记工时
//---------------------------------------------------------------------------------------------
Long totalSpentTimeCurrentIssue = 0

for (Worklog worklog in currentIssueWorklog){
    if(worklog.getStartDate().getTime() > parsedDate.getTime()){
    	totalSpentTimeCurrentIssue += worklog.getTimeSpent()
    }
}
if (totalSpentTimeCurrentIssue == 0 && worklogResult.getWorklog().getTimeSpent() == 3){
  return "请登记今天的工时"
}
//---------------------------------------------------------------------------------------------

//检查输入的时间
//if (worklogResult.getWorklog().getTimeSpent() < 600) { 
//  return "确定只花了不到10分钟处理这件事情吗？如果只是想要推迟处理，请选择“暂停处理”然后选择推迟时间。";
//}


//return worklog.getIssue().getStatus().getName()
//if(worklog.getIssue().getStatus().getStatusCategory().getName() != "In Progress"){
  //return worklog.getIssue().getStatus().getStatusCategory().getName()
  //return "当前事务未处于处理状态。"
//}

//指定类型的issue不能记录工时
//---------------------------------------------------------------------------------------------
if(worklog.getIssue().getProjectObject().getKey() == "REVIEW"){
  return "请勿在该类型事务下记录工时。"
}
//---------------------------------------------------------------------------------------------


//更新LastCommnet字段
//---------------------------------------------------------------------------------------------
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueManager = ComponentAccessor.getIssueManager()
Collection cfs = customFieldManager.getCustomFieldObjectsByName("LastComment")
CustomField cf = cfs[0]
def mutableIssue = issueManager.getIssueObject(worklog.getIssue().getKey())
mutableIssue.setCustomFieldValue(cf, worklog.getComment())
//return worklog.getComment()
def result = issueManager.updateIssue(systemUser, mutableIssue, EventDispatchOption.ISSUE_UPDATED, false)
if (result == "")
  return "更新失败，请通知管理员检查WorklogPro Scripts"
//---------------------------------------------------------------------------------------------
//if(currentUser.getName() != "hehaitao"){
//  def workDescription = worklog.getComment();
//  if (workDescription == null || workDescription.length() < 1){
//    return "请输入工作内容（Work Description）。"
//  }
//}
