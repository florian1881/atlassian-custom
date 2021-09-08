import com.atlassian.jira.issue.Issue

Integer sub_tasks = issue.getSubTaskObjects().size();

log.warn("numb_of_sub-task:" + sub_tasks)
return(sub_tasks)
