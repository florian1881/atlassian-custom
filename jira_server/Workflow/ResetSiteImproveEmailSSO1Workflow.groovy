import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import org.apache.log4j.Level
  
Logger logit = Logger.getLogger("com.domain1.eu.logging")
//log.setLevel(Level.DEBUG)


UserManager mgr = ComponentAccessor.getUserManager()
ApplicationUser user = mgr.getUserByName("Site Improve Email") 
logit.info("User: " + user)
logit.info("reporterId: " + issue.reporterId)
if (issue.reporterId == "siteimprove" && user )
{
    try {
        logit.info("site issue : " + issue.key)
        logit.info(" set reporter " + user.getName())
 //       issue.reporter = user
 //   	issue.setReporter(user) 
    } catch (Exception e) {
        logit.info(e.getMessage())
    }
}
logit.info("final reporterId: " + issue.reporterId)



