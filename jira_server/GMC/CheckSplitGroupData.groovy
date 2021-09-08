import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.bc.projectroles.ProjectRoleService  
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.user.util.UserUtil

def logit = Logger.getLogger("com.domain1.eu.logging")

ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
SimpleErrorCollection errorCollection
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
UserUtil userUtil = ComponentAccessor.userUtil
def groupManager = ComponentAccessor.groupManager

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
"split_jira-administrators",
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
"split_SEBN Cheil",
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
"split_SEG Cheil",
"split_seg-dmm",
"split_seg-lpm",
"split_segr-dmm",
"split_segr-lpm",
"split_seh-dmm",
"split_seh-lpm",
"split_sehk-dmm",
"split_sehk-lpm",
"split_SEI Cheil",
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
"split_sesg-lpm"
]
 
Group inactive = groupManager.getGroup("jira-inactive-users")
Group license = groupManager.getGroup("jira-software-users")

String[] userGroups
groups.each(){
	Group groupIn = groupManager.getGroup(it)

	if (groupIn) {
	//logit.info("source : " + groupIn.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){ApplicationUser user ->
            errorCollection = new SimpleErrorCollection()
            //projectRoleService.
            userGroups = groupManager.getGroupNamesForUser(user)
            boolean deactivate = true 
            if (groupManager.isUserInGroup(user, inactive)) {
                def grps = groupManager.getGroupsForUser(user)
                String list = ""
                grps.each() {
                    list = list + " " + it.name + ","
                }
                logit.info(""  + user.username + " " + inactive.name + " " + list)
            }

 
    	}
	}
}
