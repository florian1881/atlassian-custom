import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.ApplicationUser
import com.onresolve.scriptrunner.parameters.annotation.*
import org.apache.log4j.Level
import org.apache.log4j.Logger

@ShortTextInput(description = "Project requested", label = "Project KEY")
String projectRequested
@ShortTextInput(description = "", label = "Requester")
String userRequester
@ShortTextInput(description = "", label = "Ticket ID")
String ticketID

def projectleadmail = ComponentAccessor.projectManager.getProjectByCurrentKey(projectRequested)?.getProjectLead()?.getEmailAddress();
def mailto ="<a href='mailto:" + projectleadmail +"?subject=Access request to project " + projectRequested + "&cc=me@mymail.com&body=Hi%2C%0D%0A%0D%20support%20team%20received%20an%20access%20request%20to%20your%20project%20"+projectRequested+"%20from%20ticket%20"+ticketID+":%0D%0A%0D%0A"+userRequester+"%0D%0A%0D%0ACan%20you%20please%20VALIDATE%20or%20REJECT%20by%20answer%20of%20this%20mail%20the%20request%20and%20the%20level%20of%20rights%20%3A%0D%0A-Administrator%0D%0A-Developer%0D%0A-User%0D%0A%0D%0AThanks%20in%20advance%2C%0D%0Support%20(JIRA%20-%20Confluence)'>Send mail</a>"
def errorMsg = """<div class="aui-message aui-message-error">
    <p class="title">
        <strong>Clef innexistante</strong>
    </p>
    <p>Aucun project lead n'a été trouvé pour cette clé</p>
</div>"""
if(projectleadmail){
    return mailto
} else {
   errorMsg 
}
