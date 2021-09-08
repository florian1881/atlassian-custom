{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 // Initialiser\
\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.MutableIssue\
import com.onresolve.jira.groovy.user.FieldBehaviours \
import groovy.transform.BaseScript \
\
@BaseScript FieldBehaviours fieldBehaviours\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
def optionsManager = ComponentAccessor.getOptionsManager()\
def issue = MutableIssue\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def expMon = getFieldById("customfield_12921") // mapped from 13406\
def expDol = getFieldById("customfield_12901") // mapped from 13404\
def expFTE = getFieldById("customfield_12904") // mapped from 13407\
def techMar = getFieldById("customfield_12913") // mapped from 13403\
def schRec = getFieldById("customfield_12903") // mapped from 13405\
def labCost = getFieldById("customfield_12940") // mapped from 14200\
def customField = customFieldManager.getCustomFieldObjectByName("Is Parent Risk")\
def parentField = getFieldByName("Is Parent Risk")\
def parentConfig = customField.getRelevantConfig(getIssueContext())\
def parentOption = optionsManager.getOptions(parentConfig)?.find \{it.value == "True"\}\
\
if (sumPWEValue != null)\{\
    expMon.setFormValue(null).setHidden(true).setRequired(false)\
    expDol.setFormValue(0).setHidden(true).setRequired(false)\
    expFTE.setFormValue(null).setHidden(true).setRequired(false)\
    techMar.setHidden(true).setRequired(false)\
    schRec.setHidden(true).setRequired(false)\
    labCost.setFormValue(null).setHidden(true)\
    parentField.setFormValue(parentOption.optionId).setReadOnly(true)\
\}\
else \{\
    expMon.setRequired(true)\
    expDol.setRequired(true)\
    expFTE.setRequired(true)\
    techMar.setRequired(true)\
    schRec.setRequired(true)\
\
\}\
\
\
\
// Is Parent Risk\
\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.MutableIssue\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def optionsManager = ComponentAccessor.getOptionsManager()\
def issueManager = ComponentAccessor.getIssueManager()\
def issue = MutableIssue\
\
\
def expMon = getFieldById("customfield_12921") // mapped from 13406\
def expDol = getFieldById("customfield_12901") // mapped from 13404\
def expFTE = getFieldById("customfield_12904") // mapped from 13407\
def techMar = getFieldById("customfield_12913") // mapped from 13403\
def schRec = getFieldById("customfield_12903") // mapped from 13405\
def labCost = getFieldById("customfield_12940") // mapped from 14200\
def childSel = getFieldById("customfield_13200") // Child Risk Selector\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
\
\
\
//Hide PWE fields based on parent risk field\
if(sumPWEValue == null)\{\
    def parent = getFieldById(getFieldChanged())\
 def parentValue = parent.getValue().toString()\
    if (parentValue == "True") \{\
     expMon.setFormValue(null).setHidden(true).setRequired(false)\
     expDol.setFormValue(null).setHidden(true).setRequired(false)\
     expFTE.setFormValue(null).setHidden(true).setRequired(false)\
     techMar.setHidden(true).setRequired(false)\
     schRec.setHidden(true).setRequired(false)\
     labCost.setFormValue(null).setHidden(true)\
     childSel.setHidden(false).setRequired(false)\
 \}\
 else \{\
     expMon.setRequired(true).setHidden(false)\
     expDol.setRequired(true).setHidden(false)\
     expFTE.setRequired(true).setHidden(false)\
     techMar.setRequired(true).setHidden(false)\
     schRec.setRequired(true).setHidden(false)\
     childSel.setHidden(true).setRequired(false)\
 \}\
\}\
\
// Custom Expected Labour Cost\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def labCost = getFieldById("customfield_12940") //mapped from 14200\
\
if (sumPWEValue != null)\{\
    labCost.setFormValue(null).setHidden(true)\
\}\
\
// Schedule Recoverable\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def schRec = getFieldById("customfield_12903") //mapped from 13405\
\
if (sumPWEValue != null)\{\
    schRec.setHidden(true).setRequired(false)\
\}\
\
// Technical Margin\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def techMar = getFieldById("customfield_12913") //mapped from 13403\
\
if (sumPWEValue != null)\{\
    techMar.setHidden(true).setRequired(false)\
\}\
\
// FTEs Required\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def expFTE = getFieldById("customfield_12904") // mapped from 13407\
\
if (sumPWEValue != null)\{\
    expFTE.setFormValue(null).setHidden(true).setRequired(false)\
\}\
\
// Euros Expected\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def expDol = getFieldById("customfield_12901") // mapped from 13404\
\
if (sumPWEValue != null)\{\
    expDol.setFormValue(0).setHidden(true).setRequired(false)\
\}\
\
//Expected Months\
\
import com.atlassian.jira.component.ComponentAccessor\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
def sumPWEField = customFieldManager.getCustomFieldObjectByName("PWE Sum in Linked Risks")\
def sumPWEValue = underlyingIssue?.getCustomFieldValue(sumPWEField)\
def expMon = getFieldById("customfield_12921") // mapped from 13406\
\
if (sumPWEValue != null)\{\
    expMon.setFormValue(null).setHidden(true).setRequired(false)\
\}}