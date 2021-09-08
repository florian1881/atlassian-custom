{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 //(c) Clearvision 2017.\
//Scripted field to calculate progress of an Feature.  \
//Needs to be added as a custom field to Feature Issue Navigator, Agile Cards, or Issue View Screens.\
//Custom Fields needs to be HTML type.\
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder\
import com.atlassian.jira.issue.ModifiedValue\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.Issue\
import java.lang.Math\
import com.atlassian.jira.issue.link.IssueLinkManager\
import com.atlassian.jira.config.SubTaskManager\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.status.Status\
import com.atlassian.jira.issue.status.category.StatusCategory\
\
IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
import groovy.transform.Field\
\
Issue objective;\
\
@Field int countOfObjs = 0\
@Field int countOfAchievedObjs = 0\
@Field int countOfNotAchievedObjs = 0\
@Field int countOfGoodObjs = 0\
@Field int countOfMediumObjs = 0\
@Field int countOfPoorObjs = 0\
\
def log = Logger.getLogger("Goal.progress")\
log.setLevel(Level.DEBUG)\
\
def ReturnObjConfidence(Issue obj, Logger log) \{\
    String objConfidence\
\
    CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();\
\
    CustomField cfSprint1Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 1 Confidence' )\
    CustomField cfSprint2Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 2 Confidence' )\
    CustomField cfSprint3Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 3 Confidence' )\
    CustomField cfSprint4Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 4 Confidence' )\
    CustomField cfSprint5Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 5 Confidence' )\
\
    if ( cfSprint5Confidence ) \{\
        objConfidence = obj.getCustomFieldValue( cfSprint5Confidence )\
        if (objConfidence != null) \{\
            return "5 - " +objConfidence \
        \}\
    \}  \
\
    if ( cfSprint4Confidence ) \{\
        objConfidence = obj.getCustomFieldValue( cfSprint4Confidence )\
        if (objConfidence != null) \{\
            return "4 - " + objConfidence  \
        \}   \
    \} \
\
    if ( cfSprint3Confidence ) \{\
        objConfidence = obj.getCustomFieldValue( cfSprint3Confidence )\
        if (objConfidence != null) \{\
            return "3 - " + objConfidence  \
        \}   \
    \} \
\
    if ( cfSprint2Confidence ) \{\
        objConfidence = obj.getCustomFieldValue( cfSprint2Confidence )\
        if (objConfidence != null) \{\
            return "2 - " + objConfidence  \
        \}  \
    \} \
\
    if ( cfSprint1Confidence ) \{\
        objConfidence = obj.getCustomFieldValue( cfSprint1Confidence )\
        if (objConfidence != null) \{\
            return "1 - " + objConfidence  \
        \}\
    \} \
\
    return "none"\
\}\
\
\
def CountObjConfidence(Issue obj, Logger log) \{\
	CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()\
    \
    //log.info("Objective " + obj.getKey() );\
    \
    if (obj.getStatus().getName().toUpperCase() == "DISCARDED")\
   		return\
\
    //Add up the Total number of Objectives\
    countOfObjs = countOfObjs + 1;\
	String confidence = ReturnObjConfidence(obj,log)\
                  \
    //log.info("Confidence " + confidence)\
\
    //Depending on the Status of this Objective, we decide whether it's: Not Started, In Progress, or Done\
    String issueStatusCategory =obj.getStatus().getStatusCategory().getName();\
    String issueStatus = obj.getStatus().getName().toUpperCase();\
    //log.info("status=" + issueStatus + " category="+issueStatusCategory)\
    if ( issueStatus == "ACHIEVED" ) \{\
        countOfAchievedObjs += 1;\
    \}\
    else if ( issueStatus == "NOT ACHIEVED" ) \{\
        countOfNotAchievedObjs += 1;\
    \}\
    else if ( issueStatusCategory != "Complete" )\{\
        if ( confidence.contains("ACHIEVED")) \{\
            countOfAchievedObjs += 1;\
        \}  else if ( confidence.contains("POOR")) \{\
            countOfPoorObjs += 1;\
        \} else if (confidence.contains("MEDIUM")) \{\
            countOfMediumObjs += 1;\
        \} else if (confidence.contains("GOOD")) \{\
            countOfGoodObjs += 1;\
        \}\
    \}  \
    //log.info("Objectives Total:" + countOfObjs + "; Achieved: " + countOfAchievedObjs + "; Good: " + countOfGoodObjs + "; Medium: " + countOfMediumObjs + "; Poor: " + countOfPoorObjs)\
       \
\}\
\
log.info("Goal Confidence started")\
\
\
if (issue.getProjectObject().getName() != "SAFe PI Objectives" || issue.getIssueType().getName() != "Goal")  \{\
  return "Not SAFe Objective " + issue.getProjectObject().getName() + " " + issue.getIssueType().getName()\
\}\
\
//For the Goal...Loop through all it's outward relationships\
issueLinkManager.getOutwardLinks(issue.id).each \{objLink ->\
    \
    if ( objLink.issueLinkType.getName() == "Relates") \{\
        objective = objLink.destinationObject\
        \
        //log.info("Outward Obj Link" + objLink.issueLinkType.getName()+" for "+objective.getKey() +" Type "+objective.getIssueType().getName())\
\
        if (objective.getIssueType().getName() == "Objective") \{\
        	CountObjConfidence( objective, log )\
        \}\
    \}       //end each iterating through Objectives & other Issues associated with a Goal.\
\}\
\
//For the Goal...Loop through all it's relationships\
issueLinkManager.getInwardLinks(issue.id).each \{objLink ->\
    \
    if ( objLink.issueLinkType.getName() == "Relates") \{\
        objective = objLink.sourceObject\
        \
        //log.info("Inward Obj Link" + objLink.issueLinkType.getName()+" for "+objective.getKey()+" Type "+objective.getIssueType().getName())\
        \
        if (objective.getIssueType().getName() == "Objective") \{\
        	CountObjConfidence( objective, log )\
        \}\
    \}       //end each iterating through Objectives & other Issues associated with a Goal.\
\}\
                                           \
String title = "Objectives Total:" + countOfObjs + "; Achieved: " + countOfAchievedObjs + "; Not Achieved: " + countOfNotAchievedObjs + "; Good: " + countOfGoodObjs + "; Medium: " + countOfMediumObjs + "; Poor: " + countOfPoorObjs\
return "<progress style=\\"color:green\\" value=\\"" + countOfAchievedObjs + "\\" max=\\"" + countOfObjs + "\\" title='" + title + "'></progress>"\
}