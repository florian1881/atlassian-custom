{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Shared Teams")\
logger.setLevel(Level.DEBUG)\
\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()\
\
CustomField agileTeamCf = customFieldManager.getCustomFieldObject(12302) // "Agile Team(s)"\
    \
String destLabels = "";\
String currLabels = "";\
    \
int shares=0\
int teams=0\
	    \
if (agileTeamCf) \{\
   \
 	Set sourceLabels = (Set)agileTeamCf.getValue(issue); \
        \
    if (sourceLabels) \{\
\
        for (String label : sourceLabels) \{\
\
            if (label.contains("Team_"))\{\
                teams++\
            \}\
        \}\
        \
        logger.info("Agiles Team found "+teams)      \
    \}\
\}\
\
return teams}