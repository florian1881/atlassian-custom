import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.event.type.EventDispatchOption


Collection cfs = customFieldManager.getCustomFieldObjectsByName("Some Text")
CustomField cf = cfs[0]

String Summary = "This is a summary"

issue.setCustomFieldValue(cf,Summary)
issueManager.updateIssue(systemUser, issue, EventDispatchOption.DO_NOT_DISPATCH, false)
