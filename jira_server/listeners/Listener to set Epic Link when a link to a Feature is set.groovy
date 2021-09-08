{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.Issue\
import com.atlassian.jira.issue.IssueManager\
import com.atlassian.jira.issue.MutableIssue\
import com.atlassian.jira.issue.Issue\
import com.atlassian.jira.user.ApplicationUser\
import com.atlassian.jira.issue.UpdateIssueRequest\
import com.atlassian.jira.event.type.EventDispatchOption\
import com.atlassian.jira.event.issue.link.IssueLinkCreatedEvent\
import com.atlassian.jira.issue.CustomFieldManager\
\
\
def issueLinkManager = ComponentAccessor.getIssueLinkManager();\
IssueManager issueManager = ComponentAccessor.getIssueManager();\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()\
\
\
Issue feature\
Issue epic\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("linked.epic")\
logger.setLevel(Level.DEBUG)\
String epicName = ""\
\
Issue issue = event.getIssueLink().getDestinationObject()\
\
logger.info("linked.epic started:" + issue.getKey())\
\
def epicLink = customFieldManager.getCustomFieldObject(10006) // Epic Link\
                            \
if ( issue.getCustomFieldValue(epicLink ) != null ) \{\
	logger.info("Epic value was already set")\
    return\
\}\
\
String issueType = issue.getIssueType().getName()\
\
if ( issueType == "Story" || issueType == "Enabler" ) \{\
    issueLinkManager.getInwardLinks(issue.id).each \{ issueLink ->\
        if ( issueLink.getIssueLinkType().getName() == "Parent/Child" ) \{\
            feature = issueLink.sourceObject;\
            logger.info( "found feature:" + feature )        \
            \
            if ( feature.getIssueType().getName() == "Feature" || feature.getIssueType().getName() == "Enabler" || feature.getIssueType().getName() == "Activity" || feature.getIssueType().getName() == "Capability") \{\
                logger.info("Feature " + feature.getKey() );\
            \
            \
                //At the feature level, loop through issue links to find the Epic/Story link.\
                issueLinkManager.getInwardLinks(feature.id).each \{ featureLink ->\
                    if ( featureLink.getIssueLinkType().getName() == "Epic-Story Link" ) \{\
                        epic = featureLink.sourceObject;\
                        logger.info("Found Epic:" + epic.getKey())\
                        \
                        if ( epic.getIssueType().getName() == "Epic" ) \{    //just a double-check\
                                                    \
                            if ( issue.getCustomFieldValue(epicLink ) == null ) \{\
                                \
                                def epicLinkValue = epic.getCustomFieldValue(epicLink) \
                            \
                                //JIRA requires a mutable issue to update, so we need to create one, referenced by the story we are viewing.\
                                MutableIssue mutableIssue = issueManager.getIssueObject(issue.id) as MutableIssue\
                            \
                                //TODO mutableIssue.setFixVersions(resultVersions)\
                                mutableIssue.setCustomFieldValue(epicLink, epic)                               \
            \
             					def authContext = ComponentAccessor.jiraAuthenticationContext\
            					def user = authContext.getLoggedInUser()\
            \
            					UpdateIssueRequest updateIssueRequest = UpdateIssueRequest.builder().eventDispatchOption(EventDispatchOption.ISSUE_UPDATED).sendMail(false).build();\
            					Issue finalIssue = ComponentAccessor.getIssueManager().updateIssue(user, mutableIssue, updateIssueRequest);\
\
                            \}\
                            else \{\
                                logger.info("Epic value was already set")\
                            \}\
                        \} \
                    \}\
                \}\
            \}\
            else \{\
                logger.debug("not a feature")\
            \}\
        \}\
    \}\
\}\
}