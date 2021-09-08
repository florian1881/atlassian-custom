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
import com.atlassian.jira.issue.customfields.manager.OptionsManager\
import com.atlassian.jira.issue.customfields.option.Option\
import com.atlassian.jira.issue.customfields.option.Options\
\
String objConfidence\
\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();\
\
CustomField cfSprint1Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 1 Confidence' )\
CustomField cfSprint2Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 2 Confidence' )\
CustomField cfSprint3Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 3 Confidence' )\
CustomField cfSprint4Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 4 Confidence' )\
CustomField cfSprint5Confidence = customFieldManager.getCustomFieldObjectByName( 'Sprint 5 Confidence' )\
\
if ( cfSprint5Confidence ) \{\
    objConfidence = issue.getCustomFieldValue( cfSprint5Confidence )\
    if (objConfidence != null) \{\
    	return "5 - " +objConfidence  \
    \}\
\}  \
\
if ( cfSprint4Confidence ) \{\
    objConfidence = issue.getCustomFieldValue( cfSprint4Confidence )\
    if (objConfidence != null) \{\
    	return "4 - " + objConfidence  \
    \}   \
\} \
   \
if ( cfSprint3Confidence ) \{\
    objConfidence = issue.getCustomFieldValue( cfSprint3Confidence )\
    if (objConfidence != null) \{\
    	return "3 - " + objConfidence  \
    \}   \
\} \
\
if ( cfSprint2Confidence ) \{\
    objConfidence = issue.getCustomFieldValue( cfSprint2Confidence )\
    if (objConfidence != null) \{\
    	return "2 - " + objConfidence  \
    \}  \
\} \
\
if ( cfSprint1Confidence ) \{\
    objConfidence = issue.getCustomFieldValue( cfSprint1Confidence )\
    if (objConfidence != null) \{\
    	return "1 - " + objConfidence  \
    \}\
\} \
    \
return null}