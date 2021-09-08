import com.atlassian.jira.issue.Issue

Integer stories = 0
Integer tasks = 0
Integer bugs = 0
Integer story_task = 0
issueLinkManager.getOutwardLinks(issue.getId()).each {epicLink ->
    if (epicLink.getIssueLinkType().getName() == "Epic-Story Link") {
        Issue epicLinkedIssue = epicLink.getDestinationObject()
        String liIssueType = epicLinkedIssue.getIssueType().getName()
        if (liIssueType == "Story")  {
            stories+=1
        }
        if (liIssueType == "Task")  {
            tasks+=1
        }
        if (liIssueType == "Bug")  {
            bugs+=1
        }
    }
}

story_task = stories + tasks
log.warn("Story:" + stories.toDouble())
log.warn("Task:" + tasks.toDouble())
log.warn("Bug:" + bugs.toDouble())
log.warn("Story+Task:" + story_task)
return(story_task)
