{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww18840\viewh13900\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Risk mgt: Risk Probability Number")\
logger.setLevel(Level.DEBUG)\
\
logger.debug("Risk Probability Number for "+ issue.key)\
\
def result = 0\
\
def customFieldManager = ComponentAccessor.customFieldManager\
\
if (issue.getProjectObject().getName() == "SKA Risk Register")  \{ \
\
    CustomField cfProbability = customFieldManager.getCustomFieldObject(12926) // Current Probability of Occurrence\
\
    def probability = "" as String\
    if (cfProbability) \{\
        probability = issue.getCustomFieldValue(cfProbability) as String\
\
        if (probability == null)\
            probability = "undefined"\
    \}\
\
    logger.debug("Probability "+ probability)\
\
    if (probability == "2%" || probability == "5%")\
        result = 1\
    else if (probability == "10%")\
        result = 2\
    else if (probability == "25%")\
        result = 3\
    else if (probability == "50%")\
        result = 4\
    else if (probability == "80%")\
        result = 5\
    else\
        result = 0\
\
    return result as Integer ?: 0 \
\} else if (issue.getProjectObject().getName() == "TIMS_new") \{\
    \
    CustomField cfProbability = customFieldManager.getCustomFieldObject(11507)\
    \
    def probability = "" as String\
    if (cfProbability) \{\
        probability = issue.getCustomFieldValue(cfProbability) as String\
\
        if (probability == null)\
            probability = "undefined"\
    \}\
\
    logger.debug("Probability "+ probability)\
    \
    if (probability == "Very unlikely" )\
        result = 1\
    else if (probability == "Unlikely")\
        result = 2\
    else if (probability == "Possible")\
        result = 3\
    else if (probability == "Likely")\
        result = 4\
    else if (probability == "Certain")\
        result = 5\
    else\
        result = 0\
\
    return result as Integer ?: 0 \
    \
\}\
\
return 0}