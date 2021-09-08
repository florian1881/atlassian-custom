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
Issue story;\
\
@Field int storyPoints = 0\
@Field int totalStoryPoints = 0\
@Field int countOfStories = 0\
\
@Field int countOfToDoStories = 0\
@Field int countOfInProgressStories = 0\
@Field int countOfDoneStories = 0\
\
@Field int countOfToDoStoryPoints = 0\
@Field int countOfInProgressStoryPoints = 0\
@Field int countOfDoneStoryPoints = 0\
\
@Field long sumOfStoryPoints = 0;\
@Field long sumOfRemainingStoryPoints = 0;\
\
\
def logger = Logger.getLogger("Feature.progress")\
logger.setLevel(Level.DEBUG)\
\
def CountStoryPoints(Issue issue, Logger log) \{\
	CustomFieldManager myCustomFieldManager = ComponentAccessor.getCustomFieldManager()\
	SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager();\
    \
    //logger.info("Story " + issue.getKey() );\
    \
    if (issue.getStatus().getName() == "Discarded")\
    	return\
\
    //Add up the Total number of Stories.\
    countOfStories = countOfStories + 1;\
    storyPoints = 0;\
    CustomField cfStoryPoints = myCustomFieldManager.getCustomFieldObjects(issue).find \{it.name == "Story Points"\}\
    if ( cfStoryPoints ) \{\
        if ( issue.getCustomFieldValue(cfStoryPoints) != null ) \{\
             storyPoints = (int)issue.getCustomFieldValue(cfStoryPoints);\
        \}\
                \
        totalStoryPoints = totalStoryPoints + storyPoints;\
\
        //Depending on the Status of this Story, we decide whether it's: Not Started, In Progress, or Done\
        //StatusCategory issueStatusCategory = story.getStatus().getStatusCategory().getName();\
        String issueStatusCategory = issue.getStatus().getStatusCategory().getName();\
        //logger.info("category=" + issueStatusCategory)\
        if ( issueStatusCategory == "New" ) \{\
            countOfToDoStories += 1;\
            countOfToDoStoryPoints += storyPoints;\
        \}\
        else if ( issueStatusCategory == "In Progress" ) \{\
            countOfInProgressStories += 1;\
            countOfInProgressStoryPoints += storyPoints;\
        \}\
        else if ( issueStatusCategory == "Complete" )\{\
            countOfDoneStories += 1;\
            countOfDoneStoryPoints += storyPoints;\
        \}   \
    \}  \
\}\
\
//logger.info("Feature Progress started")\
\
if (issue.getProjectObject().getName() != "SAFe Program")  \{\
  return "Not SAFe Program"\
\}\
\
//This will not take into account any Story Point Estimate on the Feature itself.\
\
//For the Feature...Loop through all it's Children.\
issueLinkManager.getOutwardLinks(issue.id).each \{storyLink ->\
    \
    if ( storyLink.issueLinkType.getName() == "Parent/Child") \{\
        story = storyLink.destinationObject\
        CountStoryPoints( story, log )\
    \}       //end each iterating through Stories & other Issues associated with a Feature.\
\}\
\
def percentage = 0;\
if( totalStoryPoints > 0 && countOfDoneStoryPoints > 0) \{\
    percentage =  (countOfDoneStoryPoints / totalStoryPoints ) * 100    \
\}\
                                           \
String title = "Story Points Total:" + totalStoryPoints + "; Todo: " + countOfToDoStoryPoints + "; In Progess: " + countOfInProgressStoryPoints + "; Complete: " + countOfDoneStoryPoints \
return "<progress style=\\"color:green\\" value=\\"" + countOfDoneStoryPoints + "\\" max=\\"" + totalStoryPoints + "\\" title='" + title + "'></progress>"\
\
}