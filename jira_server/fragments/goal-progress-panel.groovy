//Scripted Web Panel to calculate progress of a Goal.
//Will calculate count of Objectives (linked to the Goal)
//Note: Displays separate line for Objective Confidence
//Note: Requires Relates/To link for this to work.
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

import org.apache.log4j.Logger
import org.apache.log4j.Level
import groovy.transform.Field

Issue objective;

@Field int countOfObjs = 0
@Field float sumOfBV = 0
@Field float sumOfAV = 0

@Field int countOfAchievedObjs = 0
@Field float sumOfAchievedBV = 0
@Field float sumOfAchievedAV = 0

@Field int countOfNotAchievedObjs = 0
@Field float sumOfNotAchievedBV = 0
@Field float sumOfNotAchievedAV = 0

@Field int countOfGoodObjs = 0
@Field float sumOfGoodBV = 0
@Field float sumOfGoodAV = 0

@Field int countOfMediumObjs = 0
@Field float sumOfMediumBV = 0
@Field float sumOfMediumAV = 0

@Field int countOfPoorObjs = 0
@Field float sumOfPoorBV = 0
@Field float sumOfPoorAV = 0

String strHTML

def issuePercentage = 0;
String formattedIssuePercentage
String formattedAVPercentage

def issue = context.issue as Issue

def log = Logger.getLogger("Goal.progress")
log.setLevel(Level.DEBUG)

def ReturnObjConfidence(Issue obj, Logger log) {
    String objConfidence

    CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

    CustomField cfSprint1Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 1 Confidence' )
    CustomField cfSprint2Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 2 Confidence' )
    CustomField cfSprint3Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 3 Confidence' )
    CustomField cfSprint4Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 4 Confidence' )
    CustomField cfSprint5Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 5 Confidence' )

    if ( cfSprint5Confidence ) {
        objConfidence = obj.getCustomFieldValue( cfSprint5Confidence )
        if (objConfidence != null) {
            return "5 - " +objConfidence  
        }
    }  

    if ( cfSprint4Confidence ) {
        objConfidence = obj.getCustomFieldValue( cfSprint4Confidence )
        if (objConfidence != null) {
            return "4 - " + objConfidence  
        }   
    } 

    if ( cfSprint3Confidence ) {
        objConfidence = obj.getCustomFieldValue( cfSprint3Confidence )
        if (objConfidence != null) {
            return "3 - " + objConfidence  
        }   
    } 

    if ( cfSprint2Confidence ) {
        objConfidence = obj.getCustomFieldValue( cfSprint2Confidence )
        if (objConfidence != null) {
            return "2 - " + objConfidence  
        }  
    } 

    if ( cfSprint1Confidence ) {
        objConfidence = obj.getCustomFieldValue( cfSprint1Confidence )
        if (objConfidence != null) {
            return "1 - " + objConfidence  
        }
    } 

    return "none"
}

def CountObjConfidence(Issue obj, Logger log) {
	CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()
    
    //log.info("Objective " + obj.getKey() );
    
    if (obj.getStatus().getName() == "Discarded")
   		return

    //Add up the Total number of Objectives
    countOfObjs = countOfObjs + 1;
	String confidence = ReturnObjConfidence(obj,log)
 
    CustomField cfBV = myCustomFieldManager.getCustomFieldObjectByName( 'Business value')
    CustomField cfAV = myCustomFieldManager.getCustomFieldObjectByName( 'Actual Value')
    
    float BV = 0.0
    float AV = 0.0
    
    if (cfBV) {
        if (obj.getCustomFieldValue(cfBV)!= null) {
        	BV = (float)obj.getCustomFieldValue(cfBV)
        }
    }
    if (cfAV) {
        if (obj.getCustomFieldValue(cfAV)!= null) {
        	AV = (float)obj.getCustomFieldValue(cfAV)
        }
    }
                       
    //log.info("Confidence " + confidence)

    //Depending on the Status of this Objective, we decide whether it's: Not Started, In Progress, or Done
    String issueStatusCategory =obj.getStatus().getStatusCategory().getName();
    String issueStatus = obj.getStatus().getName();
    //log.info("status=" + issueStatus + " category="+issueStatusCategory)
    if ( issueStatus.toUpperCase() == "ACHIEVED" ) {
        countOfAchievedObjs += 1;
        sumOfAchievedBV += BV;
        sumOfAchievedAV += AV;
    }
    else if ( issueStatus.toUpperCase() == "NOT ACHIEVED" ) {
        countOfNotAchievedObjs += 1;
        sumOfNotAchievedBV += BV;
       	sumOfNotAchievedAV += AV;
    }
    else if ( issueStatusCategory.toUpperCase() != "COMPLETE" ){
        if ( confidence.contains("ACHIEVED")) {
            countOfAchievedObjs += 1;
            sumOfAchievedBV += BV;
       		sumOfAchievedAV += AV;
        }  else if ( confidence.contains("POOR")) {
            countOfPoorObjs += 1;
            sumOfPoorBV += BV;
       		sumOfPoorAV += AV;
        } else if (confidence.contains("MEDIUM")) {
            countOfMediumObjs += 1;
            sumOfMediumBV += BV;
       		sumOfMediumAV += AV;
        } else if (confidence.contains("GOOD")) {
            countOfGoodObjs += 1;
            sumOfGoodBV += BV;
       		sumOfGoodAV += AV;
        }
    }  
    //log.info("Objectives Total:" + countOfObjs + "; Achieved: " + countOfAchievedObjs + "; Good: " + countOfGoodObjs + "; Medium: " + countOfMediumObjs + "; Poor: " + countOfPoorObjs)
       
}

//Main
if ((issue.getIssueType().getName() != "Goal")){
  return "Not a Goal"
}

//For the Goal...Loop through all it's outward relationships
issueLinkManager.getOutwardLinks(issue.id).each {objLink ->
    
    if ( objLink.issueLinkType.getName() == "Relates") {
        objective = objLink.destinationObject
        
        //log.info("Outward Obj Link" + objLink.issueLinkType.getName()+" for "+objective.getKey() +" Type "+objective.getIssueType().getName())

        if (objective.getIssueType().getName() == "Objective") {
            //log.info("Calling count objs " + objective.getKey())
        	CountObjConfidence( objective, log )
        }
    }       //end each iterating through Objectives & other Issues associated with a Goal.
}

//For the Goal...Loop through all it's relationships
issueLinkManager.getInwardLinks(issue.id).each {objLink ->
    
    if ( objLink.issueLinkType.getName() == "Relates") {
        objective = objLink.sourceObject
        
        //log.info("Inward Obj Link" + objLink.issueLinkType.getName()+" for "+objective.getKey()+" Type "+objective.getIssueType().getName())
        
        if (objective.getIssueType().getName() == "Objective") {
            //log.info("Calling count objs " + objective.getKey())
        	CountObjConfidence( objective, log )
        }
    }       //end each iterating through Objectives & other Issues associated with a Goal.
}

sumOfBV = sumOfAchievedBV + sumOfNotAchievedBV + sumOfGoodBV + sumOfMediumBV + sumOfPoorBV;
sumOfAV = sumOfAchievedAV + sumOfNotAchievedAV + sumOfGoodAV + sumOfMediumAV + sumOfPoorAV;

//Calculate percentage Objectives Achieved
if ( countOfAchievedObjs > 0 && countOfObjs > 0 ) {
    issuePercentage = (countOfAchievedObjs / countOfObjs ) * 100
    formattedIssuePercentage = String.format( "%.2f", issuePercentage ) + "%";
}
else {
    formattedIssuePercentage = "0%"
}

//Calculate percentage Objectives Actual Value
if ( sumOfAV > 0 && sumOfBV > 0 ) {
    issuePercentage = (sumOfAV / sumOfBV ) * 100
    formattedAVPercentage = String.format( "%.2f", issuePercentage ) + "%";
}
else {
    formattedAVPercentage = "0%"
}

String title = "Percentage: " + formattedIssuePercentage
strHTML = "<div style='background-color:white'>"
strHTML += "Count Progress: <progress style=\"color:green\" value=\"" + countOfAchievedObjs + "\" max=\"" + countOfObjs + "\" title='" + title + "'></progress>"
strHTML += "<td> <small>(" + formattedIssuePercentage + ")</small></td>"
strHTML += "</p>"
strHTML += "<table border='0'>"
strHTML += "<tr>"
strHTML += "<th>Confidence</th>"
strHTML += "<th>Count</th>"
strHTML += "<th>BV</th>"
strHTML += "<th>AV</th>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Achieved</td>"
strHTML += "<td style=text-align:center>" + countOfAchievedObjs + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfAchievedBV + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfAchievedAV + "</td>" 
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Good&nbsp&nbsp&nbsp</td>"
strHTML += "<td style=text-align:center>" + countOfGoodObjs + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfGoodBV + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfGoodAV + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Medium</td>"
strHTML += "<td style=text-align:center>" + countOfMediumObjs 
strHTML += "<td style=text-align:center>" + (int)sumOfMediumBV 
strHTML += "<td style=text-align:center>" + (int)sumOfMediumAV 
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Poor</td>"
strHTML += "<td style=text-align:center>" + countOfPoorObjs 
strHTML += "<td style=text-align:center>" + (int)sumOfPoorBV 
strHTML += "<td style=text-align:center>" + (int)sumOfPoorAV 
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td>Not Achieved</td>"
strHTML += "<td style=text-align:center>" + countOfNotAchievedObjs + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfNotAchievedBV + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfNotAchievedAV + "</td>"
strHTML += "</tr>"
strHTML += "<tr>"
strHTML += "<td><b>Total</b></td>"
strHTML += "<td style=text-align:center>" + countOfObjs + "</td>" 
strHTML += "<td style=text-align:center>" + (int)sumOfBV + "</td>"
strHTML += "<td style=text-align:center>" + (int)sumOfAV + "</td>"
strHTML += "</tr>"
strHTML += "</table>"
strHTML += "</div>"
writer.write strHTML


