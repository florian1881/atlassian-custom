import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.ApplicationUser
import com.onresolve.scriptrunner.parameters.annotation.*
import org.apache.log4j.Level
import org.apache.log4j.Logger


def userList = ComponentAccessor.getUserUtil().getAllUsersInGroupNames(['jira-users'])
def logger = Logger.getLogger(getClass())
def result=""
logger.setLevel(Level.DEBUG)

userList.each { 
    result +="""${it.getEmailAddress()};<br>"""
}
return result