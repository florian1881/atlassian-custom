{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 //Scripted field to calculate progress of a Capability.  \
//Needs to be added as a custom field to Capability Issue Navigator or Issue View Screens.\
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
Issue feature;\
\
@Field int featurePoints = 0\
@Field int totalFeaturePoints = 0\
@Field int countOfFeatures = 0\
\
@Field int countOfFunnelFeatures = 0\
@Field int countOfAnalyzingFeatures = 0\
@Field int countOfBacklogFeatures = 0\
@Field int countOfImplementingFeatures = 0\
@Field int countOfReleasingFeatures = 0\
@Field int countOfDoneFeatures = 0\
\
@Field int countOfFunnelFeaturePoints = 0\
@Field int countOfAnalyzingFeaturePoints = 0\
@Field int countOfBacklogFeaturePoints = 0\
@Field int countOfImplementingFeaturePoints = 0\
@Field int countOfReleasingFeaturePoints = 0\
@Field int countOfDoneFeaturePoints = 0\
\
@Field long sumOfFeaturePoints = 0;\
@Field long sumOfRemainingFeaturePoints = 0;\
\
\
def log = Logger.getLogger("Capability.progress")\
log.setLevel(Level.DEBUG)\
\
def CountFeaturePoints(Issue issue, Logger log) \{\
	CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()\
	SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager();\
    \
    log.info("Feature " + issue.getKey() );\
    \
    if (issue.getProjectObject().getName() != "SAFe Program" || issue.getStatus().getName() == "Discarded")  \
  		return 	\
    \
    //Add up the Total number of Features.\
    countOfFeatures = countOfFeatures + 1;\
    featurePoints = 0;\
    CustomField cfFeaturePoints = myCustomFieldManager.getCustomFieldObjects(issue).find \{it.name == "Feature Points"\}\
    if ( cfFeaturePoints ) \{\
        if ( issue.getCustomFieldValue(cfFeaturePoints) != null ) \{\
             featurePoints = (int)issue.getCustomFieldValue(cfFeaturePoints);\
        \}\
                \
        totalFeaturePoints = totalFeaturePoints + featurePoints;\
\
        //Depending on the Status of this Feature, we decide whether it's: Not Started, In Progress, or Done\
        String issueStatus = issue.getStatus().getName().toUpperCase();\
        log.info("issueStatus=" + issueStatus)\
        if ( issueStatus == "FUNNEL" ) \{\
            countOfFunnelFeatures += 1;\
            countOfFunnelFeaturePoints += featurePoints;\
        \}\
        else if ( issueStatus == "ANALYZING" ) \{\
            countOfAnalyzingFeatures += 1;\
            countOfAnalyzingFeaturePoints += featurePoints;\
        \}\
        else if ( issueStatus == "PROGRAM BACKLOG" ) \{\
            countOfBacklogFeatures += 1;\
            countOfBacklogFeaturePoints += featurePoints;\
        \}\
        else if ( issueStatus == "IMPLEMENTING" )\{\
            countOfImplementingFeatures += 1;\
            countOfImplementingFeaturePoints += featurePoints;\
        \} \
        else if ( issueStatus == "RELEASING" )\{\
            countOfReleasingFeatures += 1;\
            countOfReleasingFeaturePoints += featurePoints;\
        \} \
        else if ( issueStatus == "DONE" )\{\
            countOfDoneFeatures += 1;\
            countOfDoneFeaturePoints += featurePoints;\
        \} \
    \}  \
\}\
\
log.info("Capability Progress started")\
\
if (issue.getProjectObject().getName() != "SAFe Solution")  \{\
  return "Not SAFe Solution"\
\}\
\
//This will only take into account any Feature Point Estimates on the Feature itself.\
\
//For the Capability...Loop through all it's Children.\
issueLinkManager.getOutwardLinks(issue.id).each \{featureLink ->\
    \
    if ( featureLink.issueLinkType.getName() == "Parent/Child") \{\
        feature = featureLink.destinationObject\
        CountFeaturePoints( feature, log )\
    \}       //end each iterating through Features & other Issues associated with a Capability.\
\}\
\
def percentage = 0;\
if( totalFeaturePoints > 0 && countOfDoneFeaturePoints > 0) \{\
    percentage =  (countOfDoneFeaturePoints / totalFeaturePoints ) * 100    \
\}\
                                           \
String title = "Feature Points Total:" + totalFeaturePoints + "; Analyzing: " + countOfAnalyzingFeaturePoints + "; Backlog: " + countOfBacklogFeaturePoints + "; Implementing: " + countOfImplementingFeaturePoints + "; Releasing: " + countOfReleasingFeaturePoints + "; Done: " + countOfDoneFeaturePoints\
log.info(title)\
return "<progress style=\\"color:green\\" value=\\"" + countOfDoneFeaturePoints + "\\" max=\\"" + totalFeaturePoints + "\\" title='" + title + "'></progress>"\
\
\
}