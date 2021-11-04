import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.link.IssueLink

import com.atlassian.jira.component.ComponentAccessor;

IssueManager im = ComponentAccessor.getIssueManager();
MutableIssue issue = im.getIssueObject("WLR-275");


def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()
def created
def fromvalue
def tovalue
def changefield
def currentStatus = ""

for (int i = changeHistoryManager.getAllChangeItems(issue).size()-1; i>=0; i--){
    def change  = changeHistoryManager.getAllChangeItems(issue).get(i)
    created = change.created.toString()
	fromvalue = change.getFroms().value
	tovalue = change.getTos()
	changefield = change.field
    if (changefield  == "status"){
        break
    }
}
for (Map.Entry<String, String> entry : tovalue.entrySet()) {
	    currentStatus = entry.getValue()
}
log.warn currentStatus
