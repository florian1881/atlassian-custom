import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.transform.BaseScript
import groovy.json.JsonSlurper
import groovy.json.StreamingJsonBuilder
import com.atlassian.jira.ComponentManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import org.apache.commons.codec.binary.Base64
import com.atlassian.jira.user.DelegatingApplicationUser
import com.atlassian.jira.event.issue.IssueEvent

@BaseScript CustomEndpointDelegate delegate

//def issue = ComponentAccessor.getIssueManager().getIssueObject('TII-562')

def value_summary = issue.getSummary()
def value_description = issue.getDescription()
log.error ("Summary :"  + value_summary )


def user = "username"
def password = "password"

def appUser = event.getApplicationUser().getUsername()
def  users  = "user"


def baseURL = "http://redmine.com/issues.json"
URL url
url = new URL(baseURL)
log.error ("Куда отправляем пост: " + url )



def body = [
        issue: [
                project_id: 83,
                tracker_id: 5,
                //subject: "WWWУУУУ"
                subject: value_summary,
                description: value_description
        ]
]


log.error ("Тело заявки :"  + body )
String authStr = user + ":" + password
byte[] bytesEncoded = Base64.encodeBase64(authStr.getBytes())
String authEncoded = new String(bytesEncoded)
if (users == appUser){
    URLConnection connection = url.openConnection()
    connection.setRequestProperty("Authorization", "Basic "+authEncoded)
    connection.requestMethod = "POST"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
    connection.outputStream.withWriter("UTF-8") { new StreamingJsonBuilder(it, body) }
    connection.connect()
    log.error ("Content:" + connection.getContent())
    log.error ("ResponseCode:" + connection.getResponseCode())
    log.error ("getResponseMessage:" + connection.getResponseMessage())
    log.error (appUser)
}