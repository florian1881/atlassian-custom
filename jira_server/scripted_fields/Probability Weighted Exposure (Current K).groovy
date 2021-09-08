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
import com.atlassian.jira.issue.MutableIssue\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
import java.lang.Math\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager()\
def optionsManager = ComponentAccessor.getOptionsManager()\
def issueManager = ComponentAccessor.getIssueManager()\
\
CustomField cfProbability = customFieldManager.getCustomFieldObject(12926) // Current Probability of Occurrence\
CustomField cfCost = customFieldManager.getCustomFieldObject(12901) // Expected Dollars (K$)\
CustomField cfLaborCost = customFieldManager.getCustomFieldObject(12905) // Computed Expected Labor Cost (K$)\
CustomField cfSumChildPWE = customFieldManager.getCustomFieldObject(12944) // PWE Sum in Linked Risks\
\
def probability = "" as String\
if (cfProbability)\{\
    probability = (String)issue.getCustomFieldValue(cfProbability) // "Current probability of occurrence" mapped from 13200\
    \
    if (probability == null)\
    	probability = "undefined"\
\}\
\
def cost = 0 as Integer\
if (cfCost) \{\
    cost = (Integer)issue.getCustomFieldValue(cfCost) // "expected dollars K$" mapped from 13404\
     \
    if (cost == null)\
    	cost = 0\
\}\
    \
def labor_cost = 0.0 as Float\
if (cfLaborCost) \{\
	labor_cost = (Float)issue.getCustomFieldValue(cfLaborCost) // "computed expected labor risk cost" mapped from 13606\
\}\
\
def sumChildPWE = 0.0 as Float\
if (cfSumChildPWE) \{\
   sumChildPWE = (Float)issue.getCustomFieldValue(cfSumChildPWE) // "PWE sum of child risks" mapped from 13601\
\}\
\
def logger = Logger.getLogger("Risk mgt: Probability Weighted Exposure Current K Field 12906")\
log.setLevel(Level.DEBUG)\
\
log.info("Probability Weighted Exposure Current K")\
\
logger.info("probability "+probability)\
logger.info("cost "+cost)\
logger.info("labor_cost "+labor_cost)\
logger.info("sumChildPWE "+sumChildPWE)\
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
    \
    logger.info("Prob mean "+ prob_mean)\
\}\
//If this is a parent risk, use the sum of the child risks PWE\
if (sumChildPWE != null) \{\
	logger.info("sum Child PWE not null")\
    pwe = sumChildPWE.round(2)\
\} else \{\
    logger.info("cost and labor_cost "+ cost +" " + labor_cost)\
	//pwe = prob_mean * (cost * 1000 + labor_cost)\
	pwe = (prob_mean * (cost + labor_cost) as Float).round(2)\
\}\
return (float)pwe }