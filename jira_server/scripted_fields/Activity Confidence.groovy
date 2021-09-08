{\rtf1\ansi\ansicpg1252\cocoartf2577
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww15820\viewh19420\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import com.atlassian.jira.component.ComponentAccessor\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.fields.CustomField\
import com.atlassian.jira.issue.fields.config.FieldConfig\
import com.atlassian.jira.issue.CustomFieldManager\
import com.atlassian.jira.issue.customfields.manager.OptionsManager\
import com.atlassian.jira.issue.customfields.option.Option\
import com.atlassian.jira.issue.customfields.option.Options\
import org.apache.log4j.Logger\
import org.apache.log4j.Level\
import groovy.json.JsonSlurper\
\
def log = Logger.getLogger("TDT Activity Confidence")\
log.setLevel(Level.INFO)\
\
if (issue.getProjectObject().getName() != "TDT Activities and Tasks")  \{\
  log.info("Not TDT Activities and Tasks Project "+ issue.getProjectObject().getName())\
  return null\
\}\
\
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();\
CustomField cfPDTConfidence = customFieldManager.getCustomFieldObjectByName( 'PDT Confidence' )\
//row in bundledfield\
Integer row = 0 \
//field column in bundledfield\
List pdtNames = ['PDT_SW', 'AIV','PDT_NWC', 'PD', 'DST', 'PDT_DSH','PDT_MD', 'PDT_MI','OPS', 'PDT_LIFN', 'PDT_LD', 'M_ASSUR']\
\
if ( cfPDTConfidence ) \{\
	String pdtConfidence = issue.getCustomFieldValue( cfPDTConfidence )\
    String aggConfidence = ""\
    if (pdtConfidence) \{\
\
		Object jsonValue = new JsonSlurper().parseText(pdtConfidence)\
   		\
    	////Retrieve a value of requested row and field name\
		List fields = getFieldsForRow(row,(Map) jsonValue)\
   		log.info("Fields: "+ fields)\
    \
    	pdtNames.each \{\
        	Map field = getFieldByName(it,fields)\
        	if (field) \{\
				def value = getFieldValue(field) \
            	aggConfidence += value\
				log.info("my subField $\{it\} value is $\{value\}")\
        	\}\
        \}\
    \
    	log.info("Agg Confidence: "+aggConfidence)\
    \
    	if (aggConfidence.contains("POOR")) \{\
    		return "POOR"  \
   		\} else if (aggConfidence.contains("MEDIUM")) \{\
       		return "MEDIUM"\
    	\} else if (aggConfidence.contains("GOOD")) \{\
        	return "GOOD"\
    	\} else if (aggConfidence.contains("EXCELLENT")) \{\
        	return "EXCELLENT"\
    	\} else\
        	return "UNKNOWN"\
    \}\
\}\
\
def String getFieldValue(Map field)\{\
    def type = field.type\
    if(type == 'select' || type == 'checkbox')\{\
        return getOptionValue(field)\
    \}\
    return field.value\
\}\
\
def String getOptionValue(Map field) \{\
    List<Map> allOptions = (List<Map>) field.options\
    String val = field.value\
    if (val==null)\
    	return null\
    String[] selectedIds = val.split(',')\
    List<Map> selectedOptions = allOptions.findAll \{ it.id in selectedIds \}\
    return selectedOptions.name.join(', ')\
\}\
\
def Map getFieldByName(fieldName, List<Map> fields)\{\
    return fields.find\{it.name == fieldName\}\
\}\
\
def List getFieldsForRow(row,Map json)\{\
    List fields = null\
    int i=0\
    json.each\{k,v -> \
        if(i == row)\{\
            fields = ((Map) v).get("fields")\
        \}\
        i++\
    \}\
    return fields\
\} \
\
return null}