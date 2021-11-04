import com.atlassian.jira.issue.Issue

Integer totalIssues = 0

issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue epicLinkedIssue = eachLink.getDestinationObject()
        String status = epicLinkedIssue.getStatus().getName()
        if (status != "已完成")
        	totalIssues ++
    }
}

issueLinkManager.getInwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "Blocks") {
        Issue epicLinkedIssue = eachLink.getSourceObject()
        String status = epicLinkedIssue.getStatus().getName()
        if (status != "已完成")
        	totalIssues ++
    }
}

return(totalIssues >=1 )
