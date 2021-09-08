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
UserUtil userUtil = ComponentAccessor.userUtil
def logit = Logger.getLogger("com.domain1.eu.logging")

ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService)                        
SimpleErrorCollection errorCollection
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)

def groupManager = ComponentAccessor.groupManager

def groups = [
"split_WSC Team",
"split_WPC SEOUL",
"split_WPC PL Managers",
"split_WPC PL",
"split_WPC CN",
"split_WPC BR",
"split_WMC PMO",
"split_Wisewire QA",
"split_UserRoles",
"split_tse-lpm",
"split_tse-dmm",
"split_ssa-lpm",
"split_ssa-dmm",
"split_sme-lpm",
"split_sme-dmm",
"split_siel-lpm",
"split_siel-dmm",
"split_sge-lpm",
"split_sge-dmm",
"split_seuk-lpm",
"split_seuk-dmm",
//"split_SEUK Samsung",
//"split_SEUK Cheil",
"split_seuc-lpm",
"split_seuc-dmm",
"split_setk-lpm",
"split_setk-dmm",
"split_set-lpm",
"split_set-dmm",
"split_sesg-lpm",
"split_sesg-dmm",
"split_sesa-dmm",
//"split_SESA Cheil",
"split_serom-lpm",
"split_serom-dmm",
"split_serc-lpm",
"split_serc-dmm",
"split_sepr-lpm",
"split_sepr-dmm",
"split_sepol-lpm",
"split_sepol-dmm",
//"split_SEPOL Samsung",
//"split_SEPOL Cheil",
"split_sepco-lpm",
"split_sepco-dmm",
"split_sepak-lpm",
"split_sepak-dmm",
"split_seo-lpm",
"split_seo-dmm",
"split_senz-lpm",
"split_senz-dmm",
"split_sena-lpm",
"split_sena-dmm",
"split_semag-lpm",
"split_semag-dmm",
"split_sem-lpm",
"split_sem-dmm",
"split_selv-lpm",
"split_selv-dmm",
"split_sela-lpm",
"split_sela-dmm",
"split_sein-lpm",
"split_sein-dmm",
"split_seil-lpm",
"split_seil-dmm",
"split_sei-lpm",
"split_sei-dmm",
//"split_SEI Cheil",
"split_sehk-lpm",
"split_sehk-dmm",
"split_seh-lpm",
"split_seh-dmm",
"split_segr-lpm",
"split_segr-dmm",
"split_seg-lpm",
"split_seg-dmm",
"split_SEG Cheil",
"split_sef-lpm",
"split_sef-dmm",
"split_seeg-lpm",
"split_seeg-dmm",
"split_seda-lpm",
"split_seda-dmm",
"split_secz-lpm",
"split_secz-dmm",
"split_sech-lpm",
"split_sech-dmm",
"split_sece-lpm",
"split_sece-dmm",
"split_seca-lpm",
"split_seca-dmm",
"split_sebn-lpm",
//"split_SEBN Cheil",
"split_seb-lpm",
"split_seau-lpm",
"split_seau-dmm",
"split_seasa-lpm",
"split_seasa-dmm",
"split_seas-dmm",
"split_seag-lpm",
"split_sead-lpm",
"split_sead-dmm",
"split_sea-dmm",
"split_SDSA",
"split_SDS",
"split_scic-lpm",
"split_scic-dmm",
"split_savina-lpm",
"split_savina-dmm",
"split_sapl-lpm",
"split_sapl-dmm",
"split_SAMSUNG HQ",
"split_samcol-lpm",
"split_samcol-dmm",
"split_RSC",
"split_P6 OPTS",
"split_ksa-lpm",
"split_ksa-dmm",
//"split_jira-administrators",
"split_iran-lpm",
"split_iran-dmm",
"split_HQ Support Team",
"split_GMC Data Science Team",
"split_GMC",
"split_global-wpl",
"split_global-wb",
"split_global-dmm",
"split_CHEIL HQ",
"split_Artience",
"split_africa-lpm",
"split_africa-dmm",
]
 
Group inactive = groupManager.getGroup("jira-inactive-users")
Group license = groupManager.getGroup("jira-software-users")

//String[] userGroups
groups.each() {
	Group groupIn = groupManager.getGroup(it)
    String originalGroup = groupIn.getName().replaceFirst("split_","")
    Group original = groupManager.getGroup(originalGroup)
    logit.info("original " + originalGroup  + " " + original)

	if (groupIn) {
	//logit.info("source : " + groupIn.name)
		Collection<ApplicationUser> inUsers = groupManager.getUsersInGroup(groupIn)
    	inUsers.each(){ApplicationUser user ->
            errorCollection = new SimpleErrorCollection()
            //projectRoleService.
            Collection<String> userGrps = groupManager.getGroupNamesForUser(user)
            Collection<String> userGroups = userGrps.findAll()//new ArrayList(userGrps)
            userGrps.each() {
                if (it != groupIn.name && it.contains("split_")) {
                	String originalG = it.replaceFirst("split_","")
                    userGroups.remove(originalG)
                }
            }
            
            boolean deactivate = true
            for (String grpName : userGroups) {
           // userGroups.each() {String grpName ->
                if (!grpName.contains(originalGroup) && !grpName.contains("COMP_") && !grpName.contains("ORG_") && !grpName.contains("FnSec_")  && !grpName.contains("split") && !grpName.contains("jira-software-users") && !grpName.contains("jira-inactive-users")) {
                    //logit.info("ignore user " + user +" " + grpName + " "+ userGroups)
                    deactivate = false
                } else {
                    //logit.info("deactivate "  + user +" " + grpName + " "+ userGroups)
                    deactivate = true
                }
                if (!deactivate) {
                   // logit.info("break")
                    break 
                }
                
            }
            if (deactivate) {
        		groupManager.addUserToGroup(user, inactive)
            	userUtil.removeUserFromGroup(license, user)
            	logit.info("**deactivate," + deactivate + "," + user + "," + userGroups)
                if (original) {
                    logit.info("remove " + user + " from original " + original.name)
            		//userUtil.removeUserFromGroup(original, user)
                }
            }
 
    	}
	}
}
