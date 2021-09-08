//Scripted Web Panel to calculate progress of a Capability.
//Will calculate Feature Points and count of Features (or other issues linked to the Capability)
//Note: Displays separate line for Feature Points estimated aginst the Capability itself.
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

long sumOfFeaturePoints = 0;
long sumOfRemainingFeaturePoints = 0;
long capabilityEstimate = 0;

import org.apache.log4j.Logger
import org.apache.log4j.Level

Issue feature;

int featurePoints = 0

 

def issue = context.issue as Issue

//Globals
@Field int countOfEpics = 0
@Field int countOfFeatures = 0 
    
@Field int totalFeaturePoints = 0
    
@Field int countOfFunnelFeatures = 0 
@Field int countOfAnalyzingFeatures = 0 
@Field int countOfBacklogFeatures = 0 
@Field int countOfImplementingFeatures = 0
@Field int countOfReleasingFeatures = 0
@Field int countOfDoneFeatures = 0
    
@Field int countOfFunnelFeaturePoints=0 
@Field int countOfAnalyzingFeaturePoints=0 
@Field int countOfBacklogFeaturePoints=0 
@Field int countOfImplementingFeaturePoints =0
@Field int countOfReleasingFeaturePoints =0
@Field int countOfDoneFeaturePoints =0

String strHTML
def percentage = 0;
def issuePercentage = 0;

String formattedPercentageComplete
String formattedIssuePercentage
CustomField cfFeaturePoints
String issueStatus

def log = Logger.getLogger("capability.progress")
log.setLevel(Level.DEBUG)

CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()

log.info("Capability Progress started")

def sumFeaturePoints(int featurePoints, String issueTypeName, String issueStatus ) {
    
    countOfFeatures += 1;
   
    totalFeaturePoints = totalFeaturePoints + featurePoints;
     
    //Depending on the Status of this Feature, we decide whether it's: Not Started, In Progress, or Done
    //StatusCategory issueStatusCategory = story.getStatus().getStatusCategory().getName();
    if ( issueStatus == "FUNNEL" ) {
        countOfFunnelFeatures += 1;            
        countOfFunnelFeaturePoints += featurePoints;
    }
    else if ( issueStatus == "ANALYZING" ) {
        countOfAnalyzingFeatures += 1;            
        countOfAnalyzingFeaturePoints += featurePoints;
    }
    else if ( issueStatus == "PROGRAM BACKLOG" ) {
        countOfBacklogFeatures += 1;            
        countOfBacklogFeaturePoints += featurePoints;
    }
    else if ( issueStatus == "IMPLEMENTING" ) {
        countOfImplementingFeatures += 1;            
        countOfImplementingFeaturePoints += featurePoints;
    }
    else if ( issueStatus == "RELEASING" ) {
        countOfReleasingFeatures += 1;            
        countOfReleasingFeaturePoints += featurePoints;
    }
    else if ( issueStatus == "DONE" ){
        countOfDoneFeatures += 1;            
        countOfDoneFeaturePoints += featurePoints;
    }   
}



//Main
if ((issue.getIssueType().getName() != "Capability") && (issue.getIssueType().getName() != "Enabler")){
  return "Not a Capability or Enabler"
}

//Collect the Feature Estimate (Story Points)
cfFeaturePoints = myCustomFieldManager.getCustomFieldObjects( issue ).find {it.name == "Feature Points"}
if ( cfFeaturePoints ) {
    if ( issue.getCustomFieldValue(cfFeaturePoints) != null ) {
        capabilityEstimate = (int)issue.getCustomFieldValue(cfFeaturePoints);
    }
    else {
        capabilityEstimate = 0
    }
}

//Loop through all linked issues associated with this Feature.  
issueLinkManager.getOutwardLinks(issue.id).each { issueLink ->
    if ( issueLink.issueLinkType.getName() == "Parent/Child" ) {
        feature = issueLink.destinationObject    
        
        if (feature.getStatus().getName() != "Discarded") {
  
       		cfFeaturePoints = myCustomFieldManager.getCustomFieldObjects( feature ).find {it.name == "Feature Points"}
        	if ( cfFeaturePoints ) {
            	if ( feature.getCustomFieldValue(cfFeaturePoints) != null ) {
                	featurePoints = (int)feature.getCustomFieldValue(cfFeaturePoints);
            	}
            	else {
                	featurePoints = 0
            	}
        	}
        
        	issueStatus = feature.getStatus().getName().toUpperCase();
        	sumFeaturePoints(featurePoints, feature.getIssueType().getName(), issueStatus )
        }
    }
}
        
        
if( totalFeaturePoints > 0 && countOfDoneFeaturePoints > 0) {
    percentage =  ( countOfDoneFeaturePoints / totalFeaturePoints ) * 100    
    formattedPercentageComplete = String.format( "%.2f", percentage ) + "%";
}
else {
    formattedPercentageComplete = "0%"
}

//Calculate percentage Issues closed.
if ( countOfFeatures > 0 && countOfDoneFeatures > 0 ) {
    issuePercentage = (countOfDoneFeatures / countOfFeatures ) * 100
    formattedIssuePercentage = String.format( "%.2f", issuePercentage ) + "%";
}
else {
    formattedIssuePercentage = "0%"
}
    
String title = "Percentage: " + formattedPercentageComplete 
strHTML = "<div style='background-color:white'>"
strHTML += "Feature Point Burn-up: <progress style=\"color:green\" value=\"" + countOfDoneFeaturePoints + "\" max=\"" + totalFeaturePoints + "\" title='" + title + "'></progress>"
strHTML += "<td> <small>(" + formattedPercentageComplete + ")</small></td>"
strHTML += "</p>"
strHTML += "Capability Estimate: " + capabilityEstimate
strHTML += "</p>"
strHTML += "<table border='0'>"
strHTML += "<tr>"
strHTML += "<th></th>"
strHTML += "<th>Count</th>"
strHTML += "<th>Feature Points</th>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Todo</td>"
strHTML += "<td style=text-align:center>" + (countOfBacklogFeatures + countOfFunnelFeatures + countOfAnalyzingFeatures) + "</td>"
strHTML += "<td style=text-align:center>" + (countOfBacklogFeaturePoints + countOfFunnelFeaturePoints + countOfAnalyzingFeaturePoints) + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>In Progress&nbsp&nbsp&nbsp</td>"
strHTML += "<td style=text-align:center>" + (countOfImplementingFeatures + countOfReleasingFeatures) + "</td>"
strHTML += "<td style=text-align:center>" + (countOfImplementingFeaturePoints + countOfImplementingFeaturePoints) + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Done</td>"
strHTML += "<td style=text-align:center>" + countOfDoneFeatures 
strHTML += "<td style=text-align:center>" + countOfDoneFeaturePoints 
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td><b>Total</b></td>"
strHTML += "<td style=text-align:center>" + countOfFeatures + "</td>"
strHTML += "<td style=text-align:center>" + totalFeaturePoints + "</td>"
strHTML += "</tr>"
strHTML += "</table>"
strHTML += "</div>"
writer.write strHTML
