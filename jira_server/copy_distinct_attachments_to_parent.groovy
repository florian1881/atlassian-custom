import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager

def issueManager = ComponentAccessor.getIssueManager()
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.AttachmentManager
import com.atlassian.jira.issue.comments.Comment
import com.atlassian.jira.issue.attachment.Attachment

def issue = issueManager.getIssueObject("NGP-417")
def systemUser = ComponentAccessor.userManager.getUserByName("system")
//
def parentIssue = issue.getParentObject()

log.warn(parentIssue.key)
AttachmentManager attachmentManager = ComponentAccessor.getAttachmentManager();
//attachmentManager.copyAttachments(issue, systemUser, parentIssue.key);

Collection<Attachment> parentIssueAttachments = parentIssue.getAttachments()

def parentIssueAttachmentsFilenames = []
log.warn("Below are parent attachments:")
for (Attachment attachment : parentIssueAttachments) {
	parentIssueAttachmentsFilenames.add(attachment.getFilename())
    log.warn(attachment.getFilename())
}


Collection<Attachment> issueAttachments = issue.getAttachments()
log.warn("Below are sub-task attachments:")
issueAttachments.each{
    if (!(it.getFilename() in parentIssueAttachmentsFilenames)){
    	attachmentManager.copyAttachment(it, systemUser, parentIssue.getKey())
        log.warn(it.getFilename() + " not in parent and will be copyed")
    } else 
        log.warn(it.getFilename() + " in parent")
}




