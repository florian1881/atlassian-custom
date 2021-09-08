//https://denizoguz.atlassian.net/wiki/spaces/WLP/pages/882966529/Pre+Worklog+Entry+Scripts

import com.atlassian.jira.component.*;
import com.atlassian.jira.security.*;
import com.atlassian.jira.issue.*;

def authContext = ComponentAccessor.getJiraAuthenticationContext();
def loggedInUser = authContext.getLoggedInUser();
def issueManager = ComponentAccessor.getComponent(IssueManager.class);
def issue = issueManager.getIssueObject(worklogPreEntryParameters.issueKey);

def currentStatusAttr = worklogPreEntryParameters.attrTypes.find {it.name == "currentStatus"};
def defaultInvoice = "";

defaultInvoice = issue.getIssueType().getName() + ":" + issue.getStatus().getName()


if (currentStatusAttr != null) {
  script = '''
    var $invoiceNumber = AJS.$("#wa_%s");
    if ($invoiceNumber.val() === "") {
      $invoiceNumber.val("%s");
    }
  '''; 
  worklogPreEntryParameters.jsScript = String.format(script, currentStatusAttr.id, defaultInvoice);
}
worklogPreEntryParameters.timeSpent = "0"
return worklogPreEntryParameters;
