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
"split_Athena Team",
"split_BAU Solution Team",
"split_BMP Cheil Poland",
"split_BMP Team",
"split_CCAT Accounts Team",
"split_CCAT General Team",
"split_CE & Support Solution Team",
"split_Cheil Canada Optimisation Team",
"split_Cheil DE MFF",
"split_Cheil FR SAMFY",
"split_Cheil IT DE",
"split_Cheil IT UK",
"split_Cheil PL Digital",
"split_Cheil Retainer UK",
"split_Cheil SMNA",
"split_Cheil Ukraine Campaign - Admin",
"split_Cheil Ukraine Campaign - Team",
"split_CIP Administrators",
"split_CIP BBH",
"split_CIP Cheil",
"split_CIP Jarmany",
"split_CIP Samsung",
"split_COMP_YDB1",
"split_CRM Team",
"split_CSSO Team",
"split_DataDrivenUX Team",
"split_DIA Team",
"split_DMM Cheil",
"split_DMM Cheil Agents",
"split_EO Cheil Central",
"split_EO Cheil Production Centre",
"split_EO Cheil Studio",
"split_EO Samsung",
"split_EO Samsung Production Centre",
"split_EU Cheil PMO",
"split_FEIDE Team",
"split_FFM Administrators",
"split_FFM Team",
"split_IM Solution Team",
"split_ITC Analytics",
"split_ITC SDSE",
"split_jira-administrators",
"split_LH Administrators",
"split_LH Team",
"split_Merchandising Solution Team",
"split_Operations Admins",
"split_Operations Team",
"split_Optimisation Solution Team",
"split_POR Team",
"split_POS Team",
"split_RXPBN Cheil",
"split_RXPCZ Cheil",
"split_RXPDE Cheil",
"split_RXPES Cheil",
"split_RXPFR Cheil",
"split_RXPIT Cheil",
"split_RXPNA Cheil",
"split_RXPPOL Cheil",
"split_RXPROM Cheil",
"split_RXPUK Cheil",
"split_Sales Force",
"split_Samsung Members",
"split_Samsung SMNA",
"split_SEAD Cheil",
"split_SEAD Samsung",
"split_sead-dmm",
"split_SEAG Cheil",
"split_SEAG Samsung",
"split_seas-dmm",
"split_SEB Cheil",
"split_SEB Samsung",
"split_SEBN Cheil",
"split_SEBN Samsung",
"split_SECZ Cheil",
"split_SECZ Samsung",
"split_SEF Cheil",
"split_SEF Marketing Retail",
"split_SEF Samsung",
"split_SEG Cheil",
"split_SEG Samsung",
"split_SEGR Cheil",
"split_SEGR Samsung",
"split_SEH Cheil",
"split_SEH Samsung",
"split_SEI Cheil",
"split_SEI Samsung",
"split_SENA Cheil",
"split_SENA Samsung",
"split_SEO Poland Team",
"split_SEO Team",
"split_SEP Cheil",
"split_SEP Samsung",
"split_SEPOL Cheil",
"split_SEPOL Samsung",
"split_SEPOL WG",
"split_SEPOL WG Administrators",
"split_SEROM Cheil",
"split_SEROM Samsung",
"split_SESA Cheil",
"split_SESA Samsung",
"split_SESG Cheil",
"split_SESG Samsung",
"split_SEUK Cheil",
"split_SEUK Cheil Retainer",
"split_SEUK Samsung",
"split_SFOT Cheil",
"split_Shopitect Team",
"split_SMP Clients",
"split_SMP Team",
"split_SSO2 All Access",
"split_SSO2 EO Cheil",
"split_SSO2 SEBN Cheil",
"split_SSO2 SECZ Cheil",
"split_SSO2 SEFR Cheil",
"split_SSO2 SEG Cheil",
"split_SSO2 SEI Cheil",
"split_SSO2 SENA Cheil",
"split_SSO2 SEROM Cheil",
"split_SSO2 SESA Cheil",
"split_SSO2 SEUK Cheil",
"split_Steer Group",
"split_Tech Centre Scrum Poland DEV",
"split_Tech Centre Scrum Poland PM",
"split_Tech Centre Scrum Poland UX",
"split_Test And Learn Team",
"split_TX1 Team",
"split_UserRoles",
"split_Welkin & Meraki Team"
//"split_WSC Team"
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
            	logit.info("deactivate:" + deactivate + " " + user +" " +  " "+ userGroups)
            if (deactivate) {
        		groupManager.addUserToGroup(user, inactive)
            	userUtil.removeUserFromGroup(license, user)
                if (original) {
                    logit.info("remove " + user + " from original " + original.name)
            		//userUtil.removeUserFromGroup(original, user)
                }
            }
 
    	}
	}
}
