{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.issue.Issue;\
import com.atlassian.jira.project.version.Version;\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.fields.config.FieldConfig\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.customfields.manager.OptionsManager\
import com.atlassian.jira.issue.customfields.option.Option\
import com.atlassian.jira.issue.customfields.option.Options\
\
Date p0 = Date.parse( 'yyyyMMdd', '20181219' ) // start of PI 1 + 7 leap days added in PI9\
def p1 = new Date() // current Date */\
\
Integer incr = (p1-p0)/(13*7)+1 as Integer\
Integer sprint=(p1-p0)%(13*7)/14+1 as Integer\
\
if (sprint>6) \{\
    sprint=6\
\}\
String currentSprint = "Sprint "+sprint\
\
String dueSprint\
Integer fixVersion = 0\
String status = ""\
\
def curr_issue = issue as Issue\
\
if (curr_issue) \{\
    status = curr_issue.getStatus().name.toUpperCase()\
    def issueVersions = curr_issue.getFixVersions()\
    for (version in issueVersions) \{\
        Integer pi = Integer.parseInt(version.name.substring(2))\
        if (pi > fixVersion) \{\
            	fixVersion = pi\
        \}\
    \}   \
\}\
\
if (status == "READY FOR ACCEPTANCE" || status == "NOT ACHIEVED" || status == "ACHIEVED" || status == "RELEASING" || status == "DISCARDED" || status == "DONE" || status == "RESOLVED") \{\
    return ""\
\}\
\
if (fixVersion == 0) \{\
    return ""\
\}\
\
if (fixVersion < incr) \{\
    return "Overdue"\
\}\
\
\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();\
CustomField cfDueSprint = customFieldManager.getCustomFieldObjectByName( 'Due Sprint' )\
\
if ( cfDueSprint ) \{\
    dueSprint = curr_issue.getCustomFieldValue( cfDueSprint )\
    if (dueSprint != null) \{\
        if (currentSprint > dueSprint  && fixVersion==incr)\
    	return "Overdue"\
    \}\
\}  \
return ""}