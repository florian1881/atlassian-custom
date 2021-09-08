{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Risk mgt: Risk Cost Number")\
logger.setLevel(Level.DEBUG)\
\
logger.debug("Risk Cost Number for " + issue.key)\
\
def customFieldManager = ComponentAccessor.customFieldManager\
def result = 0\
\
if (issue.getProjectObject().getName() == "SKA Risk Register")  \{ \
\
    CustomField cfCostImpact = customFieldManager.getCustomFieldObject(12901) //mapped from 13404 Expected Dollars K$\
    CustomField cfSchedCost = customFieldManager.getCustomFieldObject(12905) //mapped from 13606 Computed Expected Labor Cost K$\
\
    def costImpact = 0.0 as Float \
    if (cfCostImpact) \{\
        costImpact = issue.getCustomFieldValue(cfCostImpact) as Float\
    \}\
\
    def schedCost = 0.0 as Float \
    if (cfSchedCost) \{\
        // Not sure about this.\
        schedCost = issue.getCustomFieldValue(cfSchedCost) as Float\
    \}\
\
    if(costImpact == null)\
    costImpact = 0.0\
\
    def cost = costImpact + schedCost\
    logger.debug("cost "+ cost)\
\
    //if(costImpact != 0.0)\
    if (cost <= 500)\
    result = 1\
    else if (cost > 500 && cost <= 1000)\
        result = 2\
    else if (cost > 1000 && cost <= 3000)\
        result = 3\
    else if (cost > 3000 && cost <= 8000)\
        result = 4\
    else if (cost > 8000)\
        result = 5\
    else\
        result = 0\
    //else\
    //    result = 0\
\
    return result as Integer ?: 0\
\} else if (issue.getProjectObject().getName() == "TIMS_new") \{\
    \
	CustomField cfImpact = customFieldManager.getCustomFieldObject(11506) //impact\
    \
    def impact = "" as String\
    if (cfImpact) \{\
        impact = issue.getCustomFieldValue(cfImpact) as String\
\
        if (impact == null)\
            impact = "undefined"\
    \}\
\
    logger.debug("Impact "+ impact)\
\
    if (impact == "Insignificant")\
        result = 1\
    else if (impact == "Minor")\
        result = 2\
    else if (impact == "Moderate")\
        result = 3\
    else if (impact == "Major")\
        result = 4\
    else if (impact == "Catastrophic")\
        result = 5\
    else\
        result = 0\
\
    return result as Integer ?: 0 \
\}\
\
return 0}