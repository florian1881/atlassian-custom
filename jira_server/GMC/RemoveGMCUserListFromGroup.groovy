import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
 
def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
String[] userList = [
"yusil.choi@domain2.com",
"yuna7.kim@domain2.com",
"yujung.seo@domain1eu.com",
"yoonsuk.lee@domain1eu.com",
"yoonmi.bae@domain2.com",
"william.cho@domain2.com",
"sem@domain1eu.com",
"nuri.seo@domain1eu.com",
"mhk.kang@domain2.com",
"j10916.lee@domain2.com",
"hoyamono@domain1eu.com",
"hk.maeng@domain1eu.com",
"dongjoo.jang@domain1eu.com",
"chunsik.choi@domain2.com",
"cholong.mo@domain1eu.com",
"boyeon.jeon@partner.domain2.com",
"bosung.kim@domain1eu.com",
"bomin12.kim@partner.domain2.com",
"yr82.lee@domain2.com",
"yongil.oh@domain2.com",
"sy0529.an@domain2.com",
"swjeon@domain1eu.com",
"seyoung.yu@domain2.com",
"jiemi.shin@domain2.com",
"jh1338.park@domain2.com",
"hyejinn.lee@domain2.com",
"h25.lee@domain2.com",
"dylan.lee@domain2.com",
"daheeee.jung@domain2.com",
"boeun15.song@domain2.com",
]
// **********************************************

String[] groups = ["CHEIL HQ"]
ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 
UserManager userManager = ComponentAccessor.getUserManager()
GroupManager groupManager = ComponentAccessor.getGroupManager()


// for each user to process
userList.each() {
    user = userManager.getUserByName((String)it)
	
    //logit.info("" + user )//+ "/" + (user.active?"active":"inactive"))
    String[] usergroups = userUtil.getGroupNamesForUser(user.getName())
    groups.each() {
        //log.info(it)
        if (user.active) {
            Group removeFromGroup = groupManager.getGroup((String)it)
            userUtil.removeUserFromGroup(removeFromGroup, user)
    		logit.info(user.getName() + "," + user.emailAddress + "," + user.active + "," + usergroups + ", Removed " + user.displayName + " from  " + removeFromGroup.getName())
        }
    }
}
