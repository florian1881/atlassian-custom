{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.fields.config.FieldConfig\
import com.atlassian.jira.issue.CustomFieldManager\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
\
CustomField cfPwce = customFieldManager.getCustomFieldObject(12906) // mapped from 14809 (Probability Weighted Exposure (Current K$)\
CustomField cfDateModel = customFieldManager.getCustomFieldObject(12915) // mapped from 13107 Model for contingency obligation date(s)\
CustomField cfIssueDate = customFieldManager.getCustomFieldObject(12933) // mapped from 13108 Obligation Date\
\
def pwce = 0.0 as Double\
if (cfPwce) \{\
    // Following line seems to cause problems !!!\
    pwce = issue.getCustomFieldValue(cfPwce) as Double\
\}\
\
def dateModel = "" as String\
if (cfDateModel) \{\
    dateModel = issue.getCustomFieldValue(cfDateModel) as String\
\}\
\
def issueDate = issue.getCustomFieldValue(cfIssueDate) as Date\
\
def logger = Logger.getLogger("Probability Weighted Exposure Then-Year K Dollar Field 12911")\
logger.setLevel(Level.DEBUG)\
\
logger.info("pwce "+pwce)\
logger.info("dateModel "+dateModel)\
logger.info("issueDate "+issueDate)\
\
def inflationMultiplier = 1.03 as Float\
def years = 0.0 as Float\
\
def todayDate = new Date() as Date\
\
if (pwce && dateModel && issueDate) \{\
    if (dateModel == "Trigger date") \{\
        years = issueDate[Calendar.YEAR] - todayDate[Calendar.YEAR]\
    \} else if (dateModel == "Random occurrence(s)") \{\
        years = 0\
    \} else if (dateModel == "Distributed occurrence") \{\
        years = issueDate[Calendar.YEAR] - todayDate[Calendar.YEAR]\
    \} else \{\
        years = 0\
    \}\
    \
    logger.info("Years "+ years)\
    logger.info("PWCE "+ pwce)\
\
	return (pwce * Math.pow(inflationMultiplier,years) as Float).round(1)\
\} else if (pwce) \{\
    logger.info("PWCE "+ pwce)\
    return pwce\
\} else \{\
    logger.info("returning null")\
    return null\
\}}