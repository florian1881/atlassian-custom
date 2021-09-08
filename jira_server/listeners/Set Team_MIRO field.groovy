import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.UpdateIssueRequest
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.label.LabelManager

import org.apache.log4j.Logger
import org.apache.log4j.Level

def logger = Logger.getLogger("Team label.Update.Listener")
logger.setLevel(Level.INFO)

logger.info("Starting Team Label Update Listener.")

IssueManager issueManager = ComponentAccessor.getIssueManager();
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def labelMgr = ComponentAccessor.getComponent(LabelManager)

Issue issue = event.issue

if (issue.getIssueType().getName() == "Feature" || issue.getIssueType().getName() == "Enabler" || issue.getIssueType().getName() == "Capability" || 
    issue.getIssueType().getName() == "Spike" || issue.getIssueType().getName() == "Objective" || issue.getIssueType().getName() == "PI Risk" || 
    issue.getIssueType().getName() == "Dependency" || issue.getIssueType().getName() == "Activity" || issue.getIssueType().getName() == "Dependency") {
    
    logger.info("Found feature, capability etc")
    
    CustomField agileTeamCf = customFieldManager.getCustomFieldObject(12302) // "Agile Team(s)"
    CustomField teamsCf = customFieldManager.getCustomFieldObject(14100) // "Teams"
    CustomField pdtCf = customFieldManager.getCustomFieldObject(11947) // "PDT(s)"
    CustomField dependsOnCf = customFieldManager.getCustomFieldObject(11701) // "Depends On"
   
    String destLabels = "";
    String currLabels = "";
    
    int teams=0
	    
    if (agileTeamCf && teamsCf && pdtCf && dependsOnCf) {
   
    	Set sourceLabels = (Set)agileTeamCf.getValue(issue); 
        currLabels = teamsCf.getValue(issue)
         
        if (sourceLabels) {
   
            for (String label : sourceLabels) {

                if (label.toUpperCase().contains("TEAM_")){
                     destLabels = destLabels + label.substring(5) + " "
                }
            }
        }
        
        sourceLabels = (Set)pdtCf.getValue(issue); 
        
        if (sourceLabels) {
   
            for (String label : sourceLabels) {

                if (label.toUpperCase().contains("PDT_")){
                    destLabels = destLabels + label.substring(4) + " "
                } else
                {
                    destLabels = destLabels + label + " "
                }
            }
        }
        
        sourceLabels = (Set)dependsOnCf.getValue(issue); 
        
        if (sourceLabels) {
   
            for (String label : sourceLabels) {

                if (label.toUpperCase().contains("PDT_")){
                    destLabels = destLabels + label.substring(4) + " "
                } 
                else if (label.toUpperCase().contains("TEAM_")){
                    destLabels = destLabels + label.substring(5) + " "
                } 
                else
                {
                    destLabels = destLabels + label + " "
                }
            }
        }

        
        if (!currLabels) {
            currLabels = ""
        }
        
        logger.info("Destination Labels: "+destLabels)
        logger.info("Current Labels: "+currLabels)
        
        if (currLabels.equals(destLabels)) {
            logger.info("No update required")
        }
        else {
            
            logger.info("Updating issue "+issue.key)
            
        	//JIRA requires a mutable issue to update, so we need to create one, referenced by the issue we are viewing.
        	MutableIssue mutableIssue = issueManager.getIssueObject(issue.id) as MutableIssue

        	mutableIssue.setCustomFieldValue(teamsCf, destLabels)

            def authContext = ComponentAccessor.jiraAuthenticationContext
            def user = authContext.getLoggedInUser()
            
            UpdateIssueRequest updateIssueRequest = UpdateIssueRequest.builder().eventDispatchOption(EventDispatchOption.ISSUE_UPDATED).sendMail(false).build();
            Issue finalIssue = ComponentAccessor.getIssueManager().updateIssue(user, mutableIssue, updateIssueRequest);
        }
    }

}
