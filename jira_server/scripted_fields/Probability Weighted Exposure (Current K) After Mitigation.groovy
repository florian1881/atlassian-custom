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
def customFieldManager = ComponentAccessor.customFieldManager\
\
CustomField cfProbability = customFieldManager.getCustomFieldObject(12923) // "probability of occurrence after mitigation" mapped from 14807\
CustomField cfCost = customFieldManager.getCustomFieldObject(12917)  // "expected dollars K$ after mitigation" mapped from 14804\
CustomField cfLabor_cost = customFieldManager.getCustomFieldObject(12919) // "computed expected labor cost after mitigation" mapped from 14803\
CustomField cfSumChildPWE = customFieldManager.getCustomFieldObject(12955)  // "PWE sum of child risks after mitigation" mapped from 15200\
\
def probability = "" as String\
if (cfProbability) \{\
    probability = issue.getCustomFieldValue(cfProbability) as String\
    \
    if (probability == null)\
    	probability = "undefined"\
\}\
\
def cost = 0 as Integer\
if (cfCost) \{\
    cost = issue.getCustomFieldValue(cfCost) as Integer\
    \
    if (cost == null)\
        cost = 0 as Integer\
\}\
\
def labor_cost = 0.0 as Float\
if (cfLabor_cost) \{\
   labor_cost = issue.getCustomFieldValue(cfLabor_cost) as Float\
    \
    if (labor_cost == null)\
    	labor_cost = 0.0 as Float\
\}\
\
def sumChildPWE = 0.0 as Float\
if (cfSumChildPWE) \{\
   sumChildPWE = issue.getCustomFieldValue(cfSumChildPWE) as Float\
\}\
\
def log = Logger.getLogger("Risk mgt: Probability Weighted Exposure Current K Dollar After Mitigation")\
log.setLevel(Level.DEBUG)\
\
log.info("Probability Weighted Exposure Current K Dollar After Mitigation")\
\
log.info("probability "+probability)\
log.info("cost "+cost)\
log.info("labor_cost "+labor_cost)\
log.info("sumChildPWE "+sumChildPWE)\
\
def prob_mean = 0.0\
def pwe = 0.0\
\
if (probability) \{\
\
    if (probability == "2%")\
    //prob_mean = (0.02+0.02)/2\
    prob_mean = 0.02\
    else if (probability == "5%")\
        //prob_mean = (0.05+0.05)/2\
    prob_mean = 0.05\
    else if (probability == "10%")\
        //prob_mean = (0.1+0.1)/2\
    prob_mean = 0.1\
    else if (probability == "25%")\
        //prob_mean = (0.25+0.25)/2\
    prob_mean = 0.25\
    else if (probability == "50%")\
        //prob_mean = (0.5+0.5)/2\
    prob_mean = 0.5\
    else if (probability == "80%")\
        //prob_mean = (0.8+0.8)/2\
    prob_mean = 0.8\
    else\
        prob_mean = 0.0\
\}\
\
log.info("prob_mean "+prob_mean)\
\
//If this is a parent risk, use the sum of the child risks PWE\
if (sumChildPWE != null)\
	pwe = sumChildPWE.round(2)\
else\
//pwe = prob_mean * (cost * 1000 + labor_cost)\
pwe = (prob_mean * (cost + labor_cost) as Float).round(2)}