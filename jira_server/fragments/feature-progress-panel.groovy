//(c) Clearvision 2017.
//Scripted Web Panel to calculate progress of an Feature.
//Will calculate Story Points and count of Stories (or other issues linked to the Feature)
//Note: Displays separate line for Story Points estimated aginst the Feature itself.
//Note: Requires Parent/Child link for this to work.
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import java.lang.Math
import com.atlassian.jira.issue.link.IssueLinkManager
import com.atlassian.jira.config.SubTaskManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.status.Status
import com.atlassian.jira.issue.status.category.StatusCategory
import groovy.transform.Field

IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager();

long sumOfStoryPoints = 0;
long sumOfRemainingStoryPoints = 0;
long featureEstimate = 0;

import org.apache.log4j.Logger
import org.apache.log4j.Level

Issue story;

int storyPoints = 0

def issue = context.issue as Issue

//Globals
@Field int countOfEpics = 0
@Field int countOfStories = 0 
    
@Field int totalStoryPoints = 0
    
@Field int countOfToDoStories = 0 
@Field int countOfInProgressStories = 0
@Field int countOfDoneStories =0
    
@Field int countOfToDoEpics=0
@Field int countOfInProgressEpics=0
@Field int countOfDoneEpics=0

@Field int countOfToDoStoryPoints=0 
@Field int countOfInProgressStoryPoints =0
@Field int countOfDoneStoryPoints =0

String strHTML
def percentage = 0;
def issuePercentage = 0;
def epicPercentage = 0;
String formattedPercentageComplete
String formattedEpicPercentage
String formattedIssuePercentage
CustomField cfFeaturePoints
CustomField cfStoryPoints
String issueStatusCategory

def log = Logger.getLogger("feature.progress")
log.setLevel(Level.DEBUG)

CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()

//log.info("Feature Progress started")

def sumStoryPoints(int storyPoints, String issueTypeName, String issueStatusCategory ) {
    
    countOfStories += 1;
   
    totalStoryPoints = totalStoryPoints + storyPoints;
     
    //Depending on the Status of this Story, we decide whether it's: Not Started, In Progress, or Done
    //StatusCategory issueStatusCategory = story.getStatus().getStatusCategory().getName();
    if ( issueStatusCategory == "New" ) {
        countOfToDoStories += 1;            
        countOfToDoStoryPoints += storyPoints;
    }
    else if ( issueStatusCategory == "In Progress" ) {
        countOfInProgressStories += 1;            
        countOfInProgressStoryPoints += storyPoints;
    }
    else if ( issueStatusCategory == "Complete" ){
        countOfDoneStories += 1;            
        countOfDoneStoryPoints += storyPoints;
    }   
}

//Main
if ((issue.getIssueType().getName() != "Feature") && (issue.getIssueType().getName() != "Enabler") && (issue.getIssueType().getName() != "Spike")){
  return "Not a Feature or Enabler or Spike"
}

//Collect the Feature Estimate (Story Points)
cfFeaturePoints = myCustomFieldManager.getCustomFieldObjects( issue ).find {it.name == "Feature Points"}
if ( cfFeaturePoints ) {
    if ( issue.getCustomFieldValue(cfFeaturePoints) != null ) {
        featureEstimate = (int)issue.getCustomFieldValue(cfFeaturePoints);
    }
    else {
        featureEstimate = 0
    }
}

//Loop through all linked issues associated with this Feature.  
issueLinkManager.getOutwardLinks(issue.id).each { issueLink ->
    if ( issueLink.issueLinkType.getName() == "Parent/Child" ) {
        story = issueLink.destinationObject    
        
        if (story.getStatus().getName() != "Discarded") {
  
       		cfStoryPoints = myCustomFieldManager.getCustomFieldObjects( story ).find {it.name == "Story Points"}
        	if ( cfStoryPoints ) {
            	if ( story.getCustomFieldValue(cfStoryPoints) != null ) {
                	storyPoints = (int)story.getCustomFieldValue(cfStoryPoints);
            	}
            	else {
                	storyPoints = 0
            	}
        	}
        
        	issueStatusCategory = story.getStatus().getStatusCategory().getName();
        	sumStoryPoints(storyPoints, story.getIssueType().getName(), issueStatusCategory )
        }
    }
}
        
if( totalStoryPoints > 0 && countOfDoneStoryPoints > 0) {
    percentage =  (countOfDoneStoryPoints / totalStoryPoints ) * 100    
    formattedPercentageComplete = String.format( "%.2f", percentage ) + "%";
}
else {
    formattedPercentageComplete = "0%"
}

//Calculate percentage Issues closed.
if ( countOfStories > 0 && countOfDoneStories > 0 ) {
    issuePercentage = (countOfDoneStories / countOfStories ) * 100
    formattedIssuePercentage = String.format( "%.2f", issuePercentage ) + "%";
}
else {
    formattedIssuePercentage = "0%"
}
    

String title = "Percentage: " + formattedPercentageComplete 
strHTML = "<div style='background-color:white'>"
strHTML += "Story Point Burn-up: <progress style=\"color:green\" value=\"" + countOfDoneStoryPoints + "\" max=\"" + totalStoryPoints + "\" title='" + title + "'></progress>"
strHTML += "<td> <small>(" + formattedPercentageComplete + ")</small></td>"
strHTML += "</p>"
strHTML += "Feature Estimate: " + featureEstimate
strHTML += "</p>"
strHTML += "<table border='0'>"
strHTML += "<tr>"
strHTML += "<th></th>"
strHTML += "<th>Issues</th>"
strHTML += "<th>Story Points</th>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>To Do</td>"
strHTML += "<td>" + countOfToDoStories + "</td>"
strHTML += "<td>" + countOfToDoStoryPoints + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>In Progress&nbsp&nbsp&nbsp</td>"
strHTML += "<td>" + countOfInProgressStories + "</td>"
strHTML += "<td>" + countOfInProgressStoryPoints + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Complete</td>"
strHTML += "<td>" + countOfDoneStories 
strHTML += "<td>" + countOfDoneStoryPoints 
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td><b>Total</b></td>"
strHTML += "<td>" + countOfStories + "</td>"
strHTML += "<td>" + totalStoryPoints + "</td>"
strHTML += "</tr>"
strHTML += "</table>"
strHTML += "</div>"
writer.write strHTML


