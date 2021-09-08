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
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
import com.atlassian.jira.issue.customfields.manager.OptionsManager\
import com.atlassian.jira.issue.customfields.option.Option\
import com.atlassian.jira.issue.customfields.option.Options\
\
// Hazard Class_Current\
String hazardClass\
String severity\
String probability\
\
def hazardMatrix = [\
    Critical : [\
        Improbable: "Hello",\
        Significant: "Critical",\
        Moderate: "High",\
        Minor: "Medium"\
    ],\
    High     : [\
        Extensive: "Critical",\
        Significant: "High",\
        Moderate: "Medium",\
        Minor: "Medium"\
    ],\
    Medium   : [\
        Extensive: "High",\
        Significant: "Medium",\
        Moderate: "Medium",\
        Minor: "Low"\
    ],\
    Low      : [\
        Extensive: "Medium",\
        Significant: "Medium",\
        Moderate: "Low",\
        Minor: "Low"\
    ],\
    Marginal: [\
       	Improbable: "Hello",\
        Significant: "Critical",\
        Moderate: "High",\
        Minor: "Medium"  \
    ]\
]\
\
def log = Logger.getLogger("Hazard Class")\
log.setLevel(Level.DEBUG)\
\
if (issue.getProjectObject().getName() != "Design Safety Review")  \{\
  return "Not Design Safety Review"\
\}\
\
log.info("Hazard Class has started")\
\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();\
\
CustomField severityFieldObj = customFieldManager.getCustomFieldObjectByName( 'Severity_current' )\
CustomField probabilityFieldObj = customFieldManager.getCustomFieldObjectByName( 'Probability_current' )\
\
if ( severityFieldObj ) \{\
    severity = issue.getCustomFieldValue( severityFieldObj )\
    if (severity != null) \{\
    	log.info("Severity is "+severity)  \
    \} \
    else \
        return "Severity is null"\
\}\
\
if ( probabilityFieldObj ) \{\
    probability = issue.getCustomFieldValue( probabilityFieldObj )\
    if (probability != null) \{\
    	log.info("Probability is "+probability)  \
    \}\
    else\
        return "Probability is null"\
\}\
\
log.info("Severity is "+severity)\
log.info("Probability is "+probability)\
\
hazardClass = hazardMatrix[severity][probability]\
\
if (hazardClass) \{\
    log.info("Hazard is "+hazardClass) \
	return hazardClass\
\}\
\
log.info("Not found")  \
\
return "Not Found"}