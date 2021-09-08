{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import java.sql.Timestamp\
\
// Get the resolution date\
Timestamp resDate = issue.getResolutionDate()\
\
// Check that we have a valid resolution date\
if (!resDate || issue.getStatus().getStatusCategory().getName() != "Complete")\
	resDate = Timestamp.valueOf("2018-12-10 00:00:00") // Date before first PI\
\
Date p0 = Date.parse( 'yyyyMMdd', '20181212' ) // start of PI 1\
def p1 = new Date(resDate.getTime()) // current Date */\
\
Integer incr = (p1-p0)/(13*7)+1 as Integer // calculate the current incr...each incr is 13 weeks of 7 days\
Integer sprint=(p1-p0)%(13*7)/14+1 as Integer // calculate the current sprint...find the mod of the incr...and divid by length of each sprint i.e. 14 days\
\
if (resDate.after(Timestamp.valueOf("2021-01-06 00:00:00"))) \{ // PI9 sprint 3 old start date...add a leap week\
    p0 = Date.parse( 'yyyyMMdd', '20181219' ) // start of PI 1 + 1 leap week (7 days)\
    incr = (p1-p0)/(13*7)+1 as Integer \
    sprint=(p1-p0)%(13*7)/14+1 as Integer\
\}\
\
if (sprint>6) \{\
    sprint=6\
\}\
\
if (resDate.before(Timestamp.valueOf("2018-12-12 00:00:00"))) \{\
    incr = 0\
\}\
\
float piSprint = Integer.valueOf(incr)+Integer.valueOf(sprint)/10\
\
if (piSprint < 1)\
	return null\
else \
	return piSprint\
\
}