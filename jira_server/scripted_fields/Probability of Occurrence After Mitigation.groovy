{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww18120\viewh17060\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def log = Logger.getLogger("Risk mgt: Probability of Occurrence After Mitigation")\
log.setLevel(Level.DEBUG)\
\
log.info("Probability of Occurrence After Mitigation")\
\
//Check that current issue is an RM-Risk\
if(issue.issueType.name == "RM-Risk")\
\{\
    log.info("RM-Risk")\
    \
    //Initialize appropriate managers\
    def linkManager = ComponentAccessor.issueLinkManager\
    def cfm = ComponentAccessor.customFieldManager\
\
    //Get the risk handling date custom field and initialize a list to hold the date values\
    CustomField cfMitigationDateField = cfm.getCustomFieldObject(12900) // mapped from 13114 Anticipated completion date\
    CustomField cfMitigationProbField = cfm.getCustomFieldObject(12928) // mapped from 14504 Probability After Mitigation\
    \
    if (cfMitigationDateField == null || cfMitigationProbField == null) \{\
        log.info("cfMitigationDateField is null or cfMitigationProbField is null")\
        return null\
    \}\
    \
    def compDates = [] as List<Date>\
    def mitProbs = [] as List\
    //Loop through all of risk mitigations and collect their date objects\
    linkManager.getInwardLinks(issue.id).each\{\
        if(it.issueLinkType.name == "Risk Mitigation")\
        \{\
            def mitigationIssue = it.getSourceObject()\
            log.info("Mitigation found "+mitigationIssue.key)\
            \
            if(mitigationIssue.getStatus().getName() == "Planned" || mitigationIssue.getStatus().getName() == "DONE")\{\
                if(mitigationIssue.getCustomFieldValue(cfMitigationDateField))\
                \{\
                    mitProbs.add(mitigationIssue.getCustomFieldValue(cfMitigationProbField) as String)\
            		compDates.add(mitigationIssue.getCustomFieldValue(cfMitigationDateField) as Date)\
                \}\
            \}\
        \}\
    \}\
\
    //Initialize last date, probability\
    def lastDate = compDates[0] as Date\
    def lastMitProb = mitProbs[0] as String\
    //Find actual last Dates\
    for(int i = 1; i < compDates.size(); i++)\
    \{\
        def currentDate = compDates[i]\
        def currentProb = mitProbs[i]\
        if(currentDate.after(lastDate))\
        \{\
            lastDate = currentDate\
            lastMitProb = currentProb\
        \}\
    \}\
\
    //You now have the last of all of the risk handling custom Date, and Probability fields\
    CustomField cfProbability = cfm.getCustomFieldObject(12926) //Risk issue expected probability mapped from 13200\
    \
    def probability = issue.getCustomFieldValue(cfProbability) as String\
    \
    log.info("Expected Probability "+ probability)\
    log.info("Last Mit Prob "+ lastMitProb)\
    \
    //Set probability to risk handling value if populated\
    if(lastMitProb)\
    \{\
        probability = lastMitProb as String\
    \}\
    return probability as String\
\}\
else //Return null if issue is not an RM-Risk\
\{\
    log.info("Not an RM-Risk")\
    return null\
\}}