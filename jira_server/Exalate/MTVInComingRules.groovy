//  maps remote projects to local projects 
// remove GDS on golive

def projectMap = [
"GPV2SEUK":"SEUK",
"GPV2SEBN":"SEBN",
"GPV2SEF":"SEF",
"GPV2SEI":"SEI",
"GPV2SEP":"SEP",
"GPV2SESA":"SESA",
"GPV2SENA":"SENA",
"GPV2SEG":"SEG",
"GPV2SEAG":"SEAG",
"GPV2SESG":"SESG",
"GPV2SEPOL":"SEPOL",
"GPV2SEGR":"SEGR",
"GPV2SECZ":"SECZ",
"GPV2SEB":"SEB",
"GPV2SEROM":"SEROM",
"GPV2SEH":"SEH",
"GPV2SEAD":"SEAD",
"GDS":"DPR" 
]

def issueTypeMap = [
"TASK":"Task",
"REQUEST":"Request",
"STORY":"Story",
"EPIC":"Epic",
"ISSUE":"Issue",
"PRODUCT REGISTRATION":"Product Registration",
"DOTCOM - PRODUCT REGISTRATION":"Product Registration",
"DOTCOM - CONTENT PRODUCTION":"Issue",
"DOTCOM - IT SUPPORT":"Request",
"DOTCOM - GENERAL INQUIRY":"Request",
"GP - ENHANCEMENT":"Issue",
"GP - SUPPORT":"Request",
"GP INTERNAL/PD":"Issue"

]
// determine the Issue Type for later decisions
// for new issues use issue.typeName
// for existing issues use issue.type.name
String issueTypeNameValue
log.info("+++ issue type" + replica.type.name + " map " + replica.type.name.toUpperCase() + " result " + issueTypeMap.get(replica.type.name.toUpperCase()))

if(firstSync){
	/* Set Project
	** If the remote projct is null or the project is not found in the map,
	** add a comment to the issue such that the configuration can be adapted.
	*/ 
	if (replica.project  == null ||
	    replica.project.key  == null ||
	    projectMap.get(replica.project.key.toUpperCase()) == null) {
		throw new Exception("Remote project [" + replica.project.key + "] is unknown or cannot be mapped - cannot process")
	} else {
  		issue.projectKey = projectMap.get(replica.project.key.toUpperCase())
	}
	
	if (replica.type.name  == null ||  
	    issueTypeMap.get(replica.type.name.toUpperCase()) == null) {
		throw new Exception("Remote issueType [" + replica.type + "/" + replica.type.name + "] is unknown or cannot be mapped - cannot process")
	    //issue.comments = commentHelper.addComment(replica.project.key  + " Remote project is unknown or cannot be mapped - can't handle it", issue.comments)
	} else {
  		//issue.typeName = issueTypeMap.get(replica.type.name.toUpperCase())
        issue.typeName = issueTypeMap.get(replica.type.name.toUpperCase())
		issueTypeNameValue = issue.typeName
	}
   //issue.typeName = "Issue"
} else {
	// existing issue
	issueTypeNameValue = issue.type.name
}

log.info("+++++ " + issueTypeNameValue)

issue.summary      = replica.summary
issue.description  = replica.description
issue.comments     = commentHelper.mergeComments(issue, replica)
issue.attachments  = attachmentHelper.mergeAttachments(issue, replica)
issue.due = replica.due
issue.labels = replica.labels

// custom
issue.customFields."GPV2 Template URL" = replica.customFields."Template URL"
issue.customFields."Disclaimer" = replica.customFields."Disclaimer"
issue.customFields."GPV2 Approver" = replica.customFields."GPV2 Approver"


//if (issueTypeNameValue.equalsIgnoreCase("Product Registration") ) {
//	issue.customFields."Disclaimer" = replica.customFields."Disclaimer"
//	log.info("Disclaimer " + replica.customFields."Disclaimer")
//}

if (replica.assignee) {
	issue.customFields."GPV2 Assignee".value = replica.assignee.email
} else {
	// handle unassigned
	issue.customFields."GPV2 Assignee".value = "Unassigned"
}

// multiple choice
/*
** Collect all values of the remote checkbox and find the corresponding value
** Ignore in case the value is not found
*/
if (replica.customFields."Cheil B2B/B2C") {
def checkboxCollection = replica.customFields."Cheil B2B/B2C".
                                value?.
                                collect{
                                        a->
                                        nodeHelper.getOption (issue, "B2B/B2C", a.value)
                                        }
issue.customFields."B2B/B2C".value = checkboxCollection
}

if (replica.customFields."Cheil Missing Contents") {
def checkboxCollection = replica.customFields."Cheil Missing Contents".
                                value?.
                                collect{
                                        a->
                                        nodeHelper.getOption (issue, "Missing Contents", a.value)
                                        }
issue.customFields."Missing Contents".value = checkboxCollection
}
if (replica.customFields."Cheil Sites List") {
def checkboxCollection = replica.customFields."Cheil Sites List".
                                value?.
                                collect{
                                        a->
                                        nodeHelper.getOption (issue, "Sites", a.value)
                                        }
issue.customFields."Sites".value = checkboxCollection
}

if (replica.customFields."Cheil Project Labels") {
	issue.customFields."Project Labels".value = nodeHelper.getOption(issue, "Project Labels", replica.customFields."Cheil Project Labels"?.value?.value)
}
if (replica.customFields."Cheil Product Category") {
	issue.customFields."Product Category".value = nodeHelper.getOption(issue, "Product Category", replica.customFields."Cheil Product Category"?.value?.value)
}
// issue.customFields."Sites".value = replica.customFields."Cheileu Sites"?.value?.collect { opt -> opt?.value }
if (replica.customFields."AEM task ID") {
	issue.customFields."GPV2 AEM task ID".value = replica.customFields."AEM task ID".value
}

if (replica.customFields."Billing Type") {
	issue.customFields."GPV2 Billing Type".value = replica.customFields."Billing Type"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."GPV2 Client Approver") {
	issue.customFields."GPV2 Client Approver".value = replica.customFields."GPV2 Client Approver".value
}
// if (replica.customFields."GPV2 Approver") {
// 	issue.customFields."GPV2 Approver".value = replica.customFields."GPV2 Approver".value
// }
if (replica.customFields."AEM Epic Link") {
	issue.customFields."GPV2 Epic Link".value = replica.customFields."AEM Epic Link".value.value
}
// if (replica.customFields."Missing Contents") {
// 	issue.customFields."GPV2 Missing Contents".value = replica.customFields."Missing Contents"?.value?.collect{it.value}?.join(",")
// }
if (replica.customFields."Module") {
	issue.customFields."GPV2 Module".value = replica.customFields."Module"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."Product Category") {
	issue.customFields."GPV2 Product Category".value = replica.customFields."Product Category"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."Product SKU") {
	issue.customFields."GPV2 Product SKU".value = replica.customFields."Product SKU".value.value
}
if (replica.customFields."Quantity") {
	issue.customFields."GPV2 Quantity".value = replica.customFields."Quantity"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."Translation") {
	issue.customFields."GPV2 Translation".value = replica.customFields."Translation"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."Work Type detail 1") {
	issue.customFields."GPV2 Work Type detail 1".value = replica.customFields."Work Type detail 1"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."Work Type detail 2") {
	issue.customFields."GPV2 Work Type detail 2".value = replica.customFields."Work Type detail 2"?.value?.collect{it.value}?.join(",")
}
if (replica.customFields."WPC") {
	issue.customFields."GPV2 WPC".value = replica.customFields."WPC"?.value?.collect{it.value}?.join(",")
}

def issueWflowMap = [
"TASK":"SSO1",
"REQUEST":"SSO1",
"STORY":"SSO1",
"EPIC":"SSO1",
"ISSUE":"SSO1",
"PRODUCT REGISTRATION":"PROD_REG"
]

String transitionWflow
if (issueTypeNameValue  == null ||	    
	    issueTypeMap.get(issueTypeNameValue.toUpperCase()) == null) {
		throw new Exception(issueTypeNameValue + " Remote issueType is unknown or cannot be mapped - can't handle it")
	    //issue.comments = commentHelper.addComment(replica.project.key  + " Remote project is unknown or cannot be mapped - can't handle it", issue.comments)
	} else {
  		transitionWflow = issueWflowMap.get(issueTypeNameValue.toUpperCase())
		log.info("**** " + transitionWflow)
	}


// the transitionmap maps remote statuses to transitions which needs to be activated locally
// Standard SSO 
// Product Registration

//def issueTransitionMap = [:]
def sso1Map =
	[
	"RQMT-LOCAL REVIEW":"RQMT-Local Review",
	"RQMT-WITH CLIENT":"RQMT-With Client",
	"RQMT-WITH EHQ*":"RQMT-With EHQ*",
	"RQMT-WITH CSSO":"RQMT- WITH CSSO",
	"RQMT-WITH HQ":"RQMT-With HQ",
	"REVIEW*":"REVIEW*",
	"RQMT-THIRD PARTY":"RQMT-Third party",
	"UX-REVIEW*":"UX-Review*",
	"UX-WITH CLIENT*":"UX-With Client*",
	"BUILD-WITH LOCAL":"BUILD-with Local",
	"BUILD-With ITC":"Build-With ITC",
	"BUILD-With CSSO":"Build-with CSSO",
	"BUILD-WITH STUDIO":"Build-With Studio",
	"QA-INTERNAL QA":"QA-Internal QA",
	"QA-CLIENT QA":"QA-Client QA",
	"BUILD-QUEUE":"BUILD-QUEUE",
	"SCHEDULED":"Scheduled",
	"PUBLISHED":"Published",
	"AUTO-CANCELLED":"Auto-cancelled",
	"CLOSED":"Closed",
	"ITC TO DO":"ITC To Do",
	"ITC IN PROGRESS":"ITC In Progress",
	"ITC PENDING":"Pending",
	"ITC WITH HQ":"ITC With HQ"
	]
	
def prodRegMap = 
	[
	"BUILD-QUEUE":"BUILD-Queue",
	"RQMT-THIRD PARTY":"Translation/Copy Review",
	"RQMT-WITH CLIENT":"Translation/Copy Approval",
	"BUILD-WITH LOCAL":"Build With Local",
	"QA-INTERNAL QA":"QA-Internal QA",
	"QA-CLIENT QA":"QA-Client QA",
	"RQMT-WITH HQ":"RQMT-With HQ",
	"ITC TO DO":"ITC To Do",
	"ITC IN PROGRESS":"ITC In Progress",
	"PENDING":"ITC - Pending",
	"ITC WITH HQ":"ITC With HQ",
	"CLOSED":"Closed",
	"PUBLISHED":"Published",
	"SCHEDULED":"Scheduled",
	"RQMT-WITH CSSO":"RQMT With CSSO"
	]

 
/* 
** transition the issue to the corresponding status. 
** If the remote status is null or the status is not found in the map,
** add a comment to the issue such that the configuration can be adapted.
*/
 
log.info("*** " + transitionWflow)
  if (transitionWflow.equalsIgnoreCase("PROD_REG") )
  {
	  transitionMap = prodRegMap
  } else {
	  transitionMap = sso1Map
  }
if (replica.status == null ||
    replica.status.name == null ||
    transitionMap.get(replica.status.name.toUpperCase()) == null) 
	{
    issue.comments = commentHelper.addComment(replica.status.name + ": Sync Remote status is unknown or cannot be mapped - cannot transition issue", issue.comments)
} else {

  log.info("*** trans " + transitionMap.get(replica.status.name.toUpperCase()))
  workflowHelper.transition(issue, transitionMap.get(replica.status.name.toUpperCase()))
}
