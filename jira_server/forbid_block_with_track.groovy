import com.atlassian.jira.issue.Issue

Integer totalIssues = 0
if(cfValues['暂停原因'].getValue() == '被其它事务Block'){
    def selectedIssueList = cfValues['Block事务'] as List<Issue>

    issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
        if (eachLink.getIssueLinkType().getName() == "Tracks") {
            Issue trackLinkedIssue = eachLink.getDestinationObject()
            if (selectedIssueList.contains(trackLinkedIssue)) {
                log.warn("通过Contains发现了")
                totalIssues ++
            }
        }
	}
    return(totalIssues == 0 )
}
else
    return 1