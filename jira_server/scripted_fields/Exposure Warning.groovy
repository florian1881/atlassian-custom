{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 package com.onresolve.jira.groovy.test.scriptfields.scripts\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.Issue\
import groovy.xml.MarkupBuilder\
import com.atlassian.jira.config.properties.APKeys\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def log = Logger.getLogger("Risk mgt: Exposure Warning")\
log.setLevel(Level.DEBUG)\
\
log.info("Exposure Warning")\
\
def cfm = ComponentAccessor.customFieldManager\
\
CustomField cfPWE = cfm.getCustomFieldObject(12906)  // Probability Weighted Exposure (Current K$)\
CustomField cfPWEam = cfm.getCustomFieldObject(12912)  // Probability Weighted Exposure (Current K$) After Mitigation\
\
 if (cfPWE == null || cfPWEam == null) \{\
        log.info("cfPWE is null or cfPWEam is null")\
        return null\
\}\
\
log.info("Prepare to retrieve values...")\
\
def PWE = 0.0\
def PWEam = 0.0\
\
// !!! THIS CODE DOES NOT WORK....CANNOT REFERENCE A SCRIPTED FIELD FROM ANOTHER ONE !!!!\
PWE = issue.getCustomFieldValue(cfPWE) as Float\
PWEam = issue.getCustomFieldValue(cfPWEam) as Float\
\
log.info("PWE "+PWE)\
log.info("PWEam "+PWEam)\
\
if (PWE == null || PWEam == null)\{\
    return null\
\}\
\
if (PWEam > PWE) \{\
    StringWriter writer = new StringWriter()\
    MarkupBuilder builder = new MarkupBuilder(writer)\
\
    builder.div(class: "aui-message error shadowed") \{\
        p(class: "title") \{\
            span(class: "aui-icon icon-error", "")\
            strong("This risk's PWE after mitigation is greater than the unmitigated PWE!")\
        \}\
    \}\
    return writer\
\} else \{\
    return null\
\}}