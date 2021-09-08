import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.issue.search.SearchRequest
import com.atlassian.jira.bc.JiraServiceContextImpl
import com.atlassian.jira.bc.JiraServiceContext
import com.atlassian.jira.bc.portal.PortalPageService
import com.atlassian.jira.portal.PortalPage
import com.atlassian.jira.portal.PortalPage.Builder

def logit = Logger.getLogger("com.domain1.eu.logging")

def groups = [
"split_africa-dmm",
"split_africa-lpm",
"split_Artience",
"split_CHEIL HQ",
"split_global-dmm",
"split_global-wb",
"split_global-wpl",
"split_GMC",
"split_HQ Support Team",
"split_iran-dmm",
"split_iran-lpm",
//"split_jira-administrators",
"split_ksa-dmm",
"split_ksa-lpm",
"split_P6 OPTS",
"split_RSC",
"split_samcol-dmm",
"split_samcol-lpm",
"split_SAMSUNG HQ",
"split_sapl-lpm",
"split_savina-dmm",
"split_savina-lpm",
"split_scic-dmm",
"split_scic-lpm",
"split_SDS",
"split_SDSA",
"split_sea-dmm",
"split_sead-dmm",
"split_sead-lpm",
"split_seag-lpm",
"split_seas-dmm",
"split_seasa-dmm",
"split_seasa-lpm",
"split_seau-dmm",
"split_seau-lpm",
"split_seb-lpm",
//"split_SEBN Cheil",
"split_sebn-lpm",
"split_seca-dmm",
"split_seca-lpm",
"split_sece-dmm",
"split_sece-lpm",
"split_sech-dmm",
"split_sech-lpm",
"split_secz-dmm",
"split_secz-lpm",
"split_seda-dmm",
"split_seda-lpm",
"split_seeg-dmm",
"split_seeg-lpm",
"split_sef-dmm",
"split_sef-lpm",
//"split_SEG Cheil",
"split_seg-dmm",
"split_seg-lpm",
"split_segr-dmm",
"split_segr-lpm",
"split_seh-dmm",
"split_seh-lpm",
"split_sehk-dmm",
"split_sehk-lpm",
//"split_SEI Cheil",
"split_sei-dmm",
"split_sei-lpm",
"split_seil-dmm",
"split_seil-lpm",
"split_sein-dmm",
"split_sein-lpm",
"split_sela-dmm",
"split_sela-lpm",
"split_selv-dmm",
"split_selv-lpm",
"split_sem-dmm",
"split_sem-lpm",
"split_semag-dmm",
"split_semag-lpm",
"split_sena-dmm",
"split_sena-lpm",
"split_senz-dmm",
"split_senz-lpm",
"split_seo-dmm",
"split_seo-lpm",
"split_sepak-dmm",
"split_sepak-lpm",
"split_sepco-dmm",
"split_sepco-lpm",
"split_sepol-dmm",
"split_sepol-lpm",
"split_sepr-dmm",
"split_sepr-lpm",
"split_serc-dmm",
"split_serc-lpm",
"split_serom-dmm",
"split_serom-lpm",
"split_sesa-dmm",
"split_sesg-dmm",
"split_sesg-lpm",
    "split_Wisewire QA"
]
def groupManager = ComponentAccessor.groupManager
def license = groupManager.getGroup("jira-software-users")

PortalPageService portalPageService = ComponentAccessor.getComponent(PortalPageService)

def luser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
JiraServiceContext sourceUserServiceCtx = new JiraServiceContextImpl(luser)

groups.each() {
	Group groupIn = groupManager.getGroup(it)
    String originalGroup = groupIn.getName().replaceFirst("split_","")

	if (groupIn) {
	//logit.info("source : " + groupIn.name)
        ComponentAccessor.getGroupManager().getUsersInGroup(groupIn).each { ApplicationUser user ->
            
        	Collection<PortalPage> dashboards = portalPageService.getOwnedPortalPages(user)
        	if (!groupManager.isUserInGroup(user, license)) {
        		dashboards.each() {
            		logit.info("" + user + " " + it.getName())
            		def builder = new PortalPage.Builder().portalPage(it).owner(luser).build()

                    // take ownership by force
            		portalPageService.updatePortalPageUnconditionally(sourceUserServiceCtx, luser, builder)

            		def result = portalPageService.validateForDelete(sourceUserServiceCtx, it.id)
            		if (result) {
            			portalPageService.deletePortalPage(sourceUserServiceCtx, it.id)
            		} else {
                		logit.info("can't delete " + it.getName())
            		}
        		}
     		}
    	}
   	}
}
    
