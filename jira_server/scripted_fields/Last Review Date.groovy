{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 //import com.atlassian.jira.ComponentManager\
import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.IssueManager\
import com.atlassian.jira.issue.comments.Comment\
import com.atlassian.jira.issue.comments.CommentManager\
\
def customFieldManager = ComponentAccessor.getCustomFieldManager();\
def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()\
def changeItems = changeHistoryManager.getAllChangeItems(issue)\
def revDate = issue.getUpdated() as Date\
\
//Get last comment\
def commentManager = ComponentAccessor.getCommentManager()\
def comment = commentManager.getLastComment (issue)\
\
if (changeItems.size() > 0) \{\
    def changeDate = changeItems.sort(false).last()["created"] as Date\
    def last_date = revDate\
    if (comment != null) \{\
        def commentDate = comment.getCreated() as Date\
        if (commentDate.after(changeDate)) \{\
        	last_date = commentDate\
		\} else\{\
    		last_date = changeDate\
		\}\
	\} else \{\
    	last_date = changeDate\
	\}\
    return new Date(last_date.time)\
\} else \{\
    if (revDate) return revDate\
    else return null\
\}}