{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 package com.onresolve.jira.groovy.test.scriptfields.scripts\
\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.Issue\
import com.atlassian.jira.issue.link.IssueLink\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Risk mgt: PWE Sum in Linked Risks")\
logger.setLevel(Level.DEBUG)\
\
log.info("PWE Sum in Linked Risks Field 12944")\
\
def issueLinkManager = ComponentAccessor.getIssueLinkManager()\
\
Issue linkedIssue;\
\
def rollup = 0\
def n = 0\
CustomField cf = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(12906) // mapped from 14809 Probability Weighted Exposure Current K$\
\
issueLinkManager.getOutwardLinks(issue.id).each \{issueLink ->\
    if (issueLink.issueLinkType.name == "Parent/Child") \{\
        \
        linkedIssue = issueLink.getDestinationObject()\
        \
        logger.info("Linked Issue "+linkedIssue.key)\
        \
        // Add up Subordinated children only \
        if (linkedIssue.getStatus().getName().toUpperCase() == "SUBORDINATED") \{\
        	rollup += (Double)linkedIssue.getCustomFieldValue(cf) ?: 0D\
        	n += 1\
        \}\
    \}\
\}\
\
logger.info("Rollup "+rollup)\
\
if (rollup) \{\
    // What does this do ????\
	rollup += issue.getEstimate() ?: 0\
\}\
\
if (n == 0)\
	return null //("no contained risks")\
else\
	//return "\\$" + (rollup as Double)\
    return rollup as Double\
\
}