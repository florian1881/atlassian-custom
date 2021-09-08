import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager

def issueManager = ComponentAccessor.getIssueManager()
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.AttachmentManager
import com.atlassian.jira.issue.comments.Comment
import com.atlassian.jira.issue.attachment.Attachment

//def issue = issueManager.getIssueObject("NGP-417")
def systemUser = ComponentAccessor.userManager.getUserByName("system")
Issue parentIssue = issue.getParentObject()

log.warn(parentIssue.key)
AttachmentManager attachmentManager = ComponentAccessor.getAttachmentManager();

Collection<Attachment> currentIssueAttachments = issue.getAttachments()

def currentIssueAttachmentsFilenames = []
log.warn("Below are current attachments:")
for (Attachment attachment : currentIssueAttachments) {
	currentIssueAttachmentsFilenames.add(attachment.getFilename())
    log.warn(attachment.getFilename())
}


Collection<Attachment> parentIssueAttachments = parentIssue.getAttachments()
log.warn("Below are parent attachments:")
parentIssueAttachments.each{
    if (!(it.getFilename() in currentIssueAttachmentsFilenames)){
    	attachmentManager.copyAttachment(it, systemUser, issue.getKey())
        log.warn(it.getFilename() + " not in current issue and will be copyed")
    } else 
        log.warn(it.getFilename() + " in current")
