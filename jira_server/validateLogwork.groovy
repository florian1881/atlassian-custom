import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.*
import com.atlassian.jira.issue.worklog.Worklog
import com.atlassian.jira.user.ApplicationUser

def worklogManager = ComponentAccessor.getWorklogManager()
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

def theDate = new Date().format('yyyyMMdd') ;
def parsedDate = new Date().parse("yyyyMMdd", theDate);

List<Worklog> WorklogList = worklogManager.getWorklogsUpdatedSince(parsedDate.getTime())
ArrayList<Worklog> processingWorklog = new ArrayList()

for (Worklog worklog in WorklogList){
  if (worklog.getAuthorObject() == currentUser)
  processingWorklog.add(worklog)
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


if (worklogResult.getWorklog().getTimeSpent() < 600) { 
  return "确定只花了不到10分钟处理这件事情吗？如果只是想要推迟处理，请选择“暂停处理”然后选择推迟时间。";
}

if(worklog.getIssue().getStatus().getStatusCategory().getName() != "In Progress"){
  return "当前事务未处于处理状态。"
}