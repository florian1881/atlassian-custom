{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww21600\viewh15920\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def log = Logger.getLogger("Risk mgt: FTE's required After Mitigation Field 12914")\
log.setLevel(Level.DEBUG)\
\
log.info("FTE's required After Mitigation")\
\
//Check that current issue is an RM-Risk\
if(issue.issueType.name == "RM-Risk")\
\{ \
    log.info("RM-Risk")\
    \
    //Initialize appropriate managers\
    def linkManager = ComponentAccessor.issueLinkManager\
    def cfm = ComponentAccessor.customFieldManager\
\
    //Get the risk handling date custom field and initialize a list to hold the date values\
    CustomField cfMitigationDateField = cfm.getCustomFieldObject(12900) // mapped from 13114\
    CustomField cfMitigationFTEField = cfm.getCustomFieldObject(12904) // mapped from 13407\
    def compDates = [] as List<Date>\
    def mitFTEs = [] as List    \
    //Loop through all of risk mitigations and collect their date objects\
    linkManager.getInwardLinks(issue.id).each\{\
        if(it.issueLinkType.name == "Risk Mitigation")\
        \{\
            def mitigationIssue = it.getSourceObject()\
            log.info("Mitigation issue found "+mitigationIssue.key)\
            \
            if(mitigationIssue.getStatus().getName() == "Planned" || mitigationIssue.getStatus().getName() == "DONE")\{\
                if(mitigationIssue.getCustomFieldValue(cfMitigationDateField))\
                \{\
                    mitFTEs.add(mitigationIssue.getCustomFieldValue(cfMitigationFTEField) as Double)\
            		compDates.add(mitigationIssue.getCustomFieldValue(cfMitigationDateField) as Date)\
                \}\
            \}\
        \}\
    \}\
\
    //Initialize last date, FTEs\
    def lastDate = compDates[0] as Date\
    def lastMitFTE = mitFTEs[0] as Double\
    //Find actual last Dates\
    for(int i = 1; i < compDates.size(); i++)\
    \{\
        def currentDate = compDates[i]\
        def currentFTE = mitFTEs[i]\
        if(currentDate.after(lastDate))\
        \{\
            lastDate = currentDate\
            lastMitFTE = currentFTE\
        \}\
    \}\
\
    //You now have the last of all of the risk handling custom Date, Month, and FTE fields\
    CustomField cfFtes = cfm.getCustomFieldObject(12904) //Risk issue FTE's mapped from 13407\
    \
    def ftes = issue.getCustomFieldValue(cfFtes) as Double\
    \
    log.info("Expected FTEs "+ ftes)\
    log.info("Last Mit FTE "+ lastMitFTE)\
\
    //Set FTEs to risk handling value if populated\
    if(lastMitFTE != null)\
    \{\
        ftes = lastMitFTE as Double\
    \}\
    return ftes\
\}\
else //Return null if issue is not an RM-Risk\
\{\
    return null\
\}}