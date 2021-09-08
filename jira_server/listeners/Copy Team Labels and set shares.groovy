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
import com.atlassian.jira.issue.label.LabelManager\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Team label.Update.Listener")\
logger.setLevel(Level.INFO)\
\
logger.info("Starting Team Label Update Listener.")\
\
IssueManager issueManager = ComponentAccessor.getIssueManager();\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()\
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()\
def labelMgr = ComponentAccessor.getComponent(LabelManager)\
\
Issue issue = event.issue\
\
if (issue.getIssueType().getName() == "Feature" || issue.getIssueType().getName() == "Enabler" || issue.getIssueType().getName() == "Capability" || \
    issue.getIssueType().getName() == "Spike" || issue.getIssueType().getName() == "Objective" || issue.getIssueType().getName() == "SAFe Risk" || \
    issue.getIssueType().getName() == "Dependency") \{\
    \
    logger.info("Found feature, capability etc")\
    \
    CustomField agileTeamCf = customFieldManager.getCustomFieldObject(12302) // "Agile Team(s)"\
    CustomField teamsCf = customFieldManager.getCustomFieldObject(12806) // "Teams"\
    CustomField sharedCf = customFieldManager.getCustomFieldObject(13402) // "Shared Teams"\
    \
    String destLabels = "";\
    String currLabels = "";\
    \
    int shares=0\
    int teams=0\
	    \
    if (agileTeamCf && teamsCf && sharedCf) \{\
   \
    	Set sourceLabels = (Set)agileTeamCf.getValue(issue); \
        currLabels = teamsCf.getValue(issue)\
        if (sharedCf.getValue(issue)) \{\
	        shares = (int)sharedCf.getValue(issue);\
        \}\
        \
        if (sourceLabels) \{\
   \
            for (String label : sourceLabels) \{\
\
                if (label.contains("Team_"))\{\
                    teams++\
                    destLabels = destLabels + label.substring(5) + " "\
                \}\
            \}\
        \}\
        \
        if (!currLabels) \{\
            currLabels = ""\
        \}\
        \
        logger.info("Destination Labels: "+destLabels)\
        logger.info("Current Labels: "+currLabels)\
        logger.info("Agiles Team found "+teams)\
        logger.info("Current shares found "+shares)\
        \
        if (currLabels.equals(destLabels) && teams.equals(shares)) \{\
            logger.info("No update required")\
        \}\
        else \{\
            \
            logger.info("Updating issue "+issue.key)\
            \
        	//JIRA requires a mutable issue to update, so we need to create one, referenced by the issue we are viewing.\
        	MutableIssue mutableIssue = issueManager.getIssueObject(issue.id) as MutableIssue\
\
        	mutableIssue.setCustomFieldValue(teamsCf, destLabels)\
            //mutableIssue.setCustomFieldValue(sharedCf,(Double)teams)  \
\
            def authContext = ComponentAccessor.jiraAuthenticationContext\
            def user = authContext.getLoggedInUser()\
            \
            UpdateIssueRequest updateIssueRequest = UpdateIssueRequest.builder().eventDispatchOption(EventDispatchOption.ISSUE_UPDATED).sendMail(false).build();\
            Issue finalIssue = ComponentAccessor.getIssueManager().updateIssue(user, mutableIssue, updateIssueRequest);\
        \}\
    \}\
\
\}}