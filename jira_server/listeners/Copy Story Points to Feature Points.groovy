{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.IssueManager\
import com.atlassian.jira.issue.Issue\
import com.atlassian.jira.event.type.EventDispatchOption\
import com.atlassian.jira.issue.MutableIssue\
import com.atlassian.jira.issue.UpdateIssueRequest\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.user.ApplicationUser\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Story Point.Update.Listener")\
logger.setLevel(Level.DEBUG)\
\
logger.info("Starting Story Point Update Listener.")\
\
IssueManager issueManager = ComponentAccessor.getIssueManager();\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()\
\
Issue feature = event.issue\
\
if (feature.getIssueType().getName() == "Feature" || feature.getIssueType().getName() == "Enabler" || feature.getIssueType().getName() == "Capability" || feature.getIssueType().getName() == "Spike" || feature.getIssueType().getName() == "Activity") \{\
    \
	CustomField cfStoryPts = customFieldManager.getCustomFieldObject(10002) // "Story Points"\
    \
    def featurePts = feature.getCustomFieldValue(customFieldManager.getCustomFieldObject(11948)) // "Feature Points"\
	def storyPts = feature.getCustomFieldValue(customFieldManager.getCustomFieldObject(10002)) // "Story Points"\
    \
    if (featurePts.equals(storyPts)) \{\
        logger.info("Equal: No need to update")\
    \}\
    else \{\
                \
        logger.info("Not Equal: Need to update")\
    \
        //JIRA requires a mutable issue to update, so we need to create one, referenced by the story we are viewing.\
        MutableIssue mutableIssue = issueManager.getIssueObject(feature.id) as MutableIssue\
        mutableIssue.setCustomFieldValue(cfStoryPts, featurePts)\
\
        def authContext = ComponentAccessor.jiraAuthenticationContext\
        def user = authContext.getLoggedInUser()\
            \
        UpdateIssueRequest updateIssueRequest = UpdateIssueRequest.builder().eventDispatchOption(EventDispatchOption.ISSUE_UPDATED).sendMail(false).build();\
        Issue finalIssue = ComponentAccessor.getIssueManager().updateIssue(user, mutableIssue, updateIssueRequest);\
    \}\
\}}