import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.attachment.Attachment
import com.atlassian.jira.issue.AttachmentManager
import org.apache.commons.io.FilenameUtils
import com.atlassian.jira.config.util.AttachmentPathManager
import com.atlassian.jira.util.io.InputStreamConsumer
import org.springframework.util.StreamUtils
import java.io.InputStream
import java.io.IOException
import org.apache.log4j.Logger

Logger logit = Logger.getLogger("com.domain1.eu.logging")
def issueManager = ComponentAccessor.getIssueManager()
AttachmentManager attachmentManager = ComponentAccessor.getAttachmentManager()
AttachmentPathManager apm = ComponentAccessor.getAttachmentPathManager()

def issue = issueManager.getIssueObject("CSD-19040"); // e.g. SP-1

List<Attachment> attachments = attachmentManager.getAttachments(issue)

int rowCount, pKeyCol, siteCol, dueDateCol, summCol, compCol, descCol, repCol, assgnCol = 0

attachments.each{ Attachment attachment ->
	def fileName=FilenameUtils.getBaseName(attachment.getFilename())
	def fileExtension=FilenameUtils.getExtension(attachment.getFilename())
//log.warn (fileExtension) 
    if (fileExtension.equalsIgnoreCase("csv")) {
    
		def attachmentFile = attachmentManager.streamAttachmentContent(attachment, 
                           new FileInputStreamConsumer(attachment.getFilename()))

// logit.info(fileName + "." + fileExtension)
// List<String> attachmentFileLines = attachmentFile.text.split('\n')
		attachmentFile.each() { singleLine->
// attachmentFile.each() { singleLine->
			logit.info(singleLine)
            rowCount++
                switch(rowCount) {
                    case 1:
                    logit.info("header")
                    break
                    default :
                    logit.info("row " + rowCount)
                        
                }
		}
    } 

}
/*****/

class FileInputStreamConsumer implements InputStreamConsumer{

private final String filename;
public FileInputStreamConsumer(String filename) {
this.filename = filename;
}
@Override
public File withInputStream(InputStream is) throws IOException {
final File f = File.createTempFile(FilenameUtils.getBaseName(filename), FilenameUtils.getExtension(filename));
StreamUtils.copy(is, new FileOutputStream(f));
return f;
}
}
