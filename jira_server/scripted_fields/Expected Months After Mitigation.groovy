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
def log = Logger.getLogger("Risk mgt: Expected Months After Mitigation Field 12946")\
log.setLevel(Level.DEBUG)\
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
    CustomField cfMitigationMonthField = cfm.getCustomFieldObject(12921) // mapped from 13406 Expected (months)\
    \
    if (cfMitigationDateField == null || cfMitigationMonthField == null) \{\
        log.info("cfMitigationDateField is null or cfMitigationMonthField is null")\
        return null\
    \}\
    \
    def compDates = [] as List<Date>\
    def mitMonths = [] as List\
    \
    //Loop through all of risk mitigations and collect their date objects\
    linkManager.getInwardLinks(issue.id).each\{\
        if(it.issueLinkType.name == "Risk Mitigation")\
        \{\
            \
            def mitigationIssue = it.getSourceObject()\
            \
            log.info("Found mitigation "+mitigationIssue.key + " status "+mitigationIssue.getStatus().getName())\
            \
            if(mitigationIssue.getStatus().getName() == "Planned" || mitigationIssue.getStatus().getName() == "DONE")\{\
                \
                if(mitigationIssue.getCustomFieldValue(cfMitigationDateField))\
                \{\
                	mitMonths.add(mitigationIssue.getCustomFieldValue(cfMitigationMonthField) as Double)\
            		compDates.add(mitigationIssue.getCustomFieldValue(cfMitigationDateField) as Date)\
                \}\
            \}\
        \}\
    \}\
\
    //Initialize last date, months\
    def lastDate = compDates[0] as Date\
    def lastMitMonth = mitMonths[0] as Double\
    //Find actual last Dates\
    for(int i = 1; i < compDates.size(); i++)\
    \{\
        def currentDate = compDates[i]\
        def currentMonth = mitMonths[i]\
        if(currentDate.after(lastDate))\
        \{\
            lastDate = currentDate\
            lastMitMonth = currentMonth\
        \}\
    \}\
\
    //You now have the last of all of the risk handling custom Date, and Month fields\
    CustomField cfMonths = cfm.getCustomFieldObject(12921) //Risk issue expected months mapped from 13406\
    def months = issue.getCustomFieldValue(cfMonths) as Double\
    \
    log.info("Expected Months "+ months)\
    log.info("Last Mit Month "+ lastMitMonth)\
    \
    //Set months to risk handling value if populated\
    if(lastMitMonth != null)\
    \{\
        months = lastMitMonth as Double\
    \}\
    return months\
\}\
else //Return null if issue is not an RM-Risk\
\{\
    log.info("Not an RM-Risk")\
    return null\
\}}