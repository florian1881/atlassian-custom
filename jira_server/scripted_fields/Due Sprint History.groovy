{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.history.ChangeItemBean\
import com.atlassian.jira.issue.CustomFieldManager\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
\
def logger = Logger.getLogger("Due Sprint History")\
logger.setLevel(Level.DEBUG)\
\
logger.info("Due Sprint History for "+issue.key)\
\
Date p0 = Date.parse( 'yyyyMMdd', '20181212' ) // start of PI 1\
\
def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()\
def changebeans = changeHistoryManager.getChangeItemsForField(issue,"Due Sprint")\
def changelist = null as String\
\
for (ChangeItemBean item: changebeans) \{\
    \
    if (item.getFromString() != null) \{\
        Date date = new Date(item.getCreated().getTime())\
        Integer incr = (date-p0)/(13*7)+1 as Integer // calculate the current incr...each incr is 13 weeks of 7 days\
        Integer sprint=(date-p0)%(13*7)/14+1 as Integer // calculate the current sprint...find the mod of the incr...and divid by length of each sprint i.e. 14 days\
        \
        if (sprint>6) \{\
    		sprint=6\
		\}\
        \
        float piSprint = Integer.valueOf(incr)+Integer.valueOf(sprint)/10\
        \
        if (changelist)\
	 	    changelist += "Modified "+date.format("yyyy-MM-dd")+" PI"+piSprint+" "+item.getFromString()+"->"+item.getToString() + "\\n"\
        else\
            changelist = "Modified "+date.format("yyyy-MM-dd")+" PI"+piSprint+" "+item.getFromString()+"->"+item.getToString() + "\\n"\
    \}	\
\}\
\
return changelist}