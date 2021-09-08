{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
\
def issueManager = ComponentAccessor.getIssueManager()\
\
if ( getBehaviourContextId() == "Create-AT1Story" || \
     getBehaviourContextId() == "Create-AT2Story" ||\
     getBehaviourContextId() == "Create-ArchStory" || \
     getBehaviourContextId() == "Create-STStory" || \
     getBehaviourContextId() == "Create-SkanetStory" ) \{\
    \
    getFieldById("project-field").setReadOnly(true)\
\
    getFieldById("issuetype-field").setReadOnly(true)\
\
    def contextIssue = issueManager.getIssueObject(getContextIssueId())\
\
    getFieldById("issuelinks-linktype").setFormValue("Child Of").setReadOnly(true)\
    getFieldById("issuelinks-issues").setFormValue(contextIssue.key).setReadOnly(true)\
    \
    //Copy the FixVersion from parent to child.\
    //def versionManager = ComponentAccessor.getVersionManager()\
    //def versions = contextIssue.getFixVersions()\
    //if (versions) \{\
    //    getFieldById("Fix Version/s").setFormValue(versions) \
    //\}\
\}\
}