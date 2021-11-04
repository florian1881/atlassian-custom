import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.scriptrunner.canned.jira.admin.CopyProject

import org.apache.log4j.Logger
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.ProjectRoleActor
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.security.roles.ProjectRoleActors
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.bc.JiraServiceContextImpl
import com.atlassian.jira.util.SimpleErrorCollection
import com.domain1.jira.RemoveProjectRoles
import com.atlassian.jira.bc.project.component.ProjectComponent
import com.atlassian.jira.bc.project.component.ProjectComponentManager

def logit = Logger.getLogger("com.domain1.eu.logging")
def luser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def ctx = new JiraServiceContextImpl(luser)
ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
ProjectManager pMgr = ComponentAccessor.getProjectManager()
ProjectComponentManager pComMgr = ComponentAccessor.getProjectComponentManager()

def projList = [
"P620210001":"2021 P6 B2B AFRICA RHQ - AFRICA_EN / AFRICA_FR / AFRICA_PT",
"P620210002":"2021 P6 B2B IRAN - IRAN",
"P620210003":"2021 P6 B2B KSA - SA / SA_EN",
"P620210004":"2021 P6 B2B SAMCOL - CO",
"P620210005":"2021 P6 B2B SAPL - SG",
"P620210006":"2021 P6 B2B SAVINA - VN",
"P620210007":"2021 P6 B2B SCIC - CN",
"P620210008":"2021 P6 B2B SEA - US",
"P620210009":"2021 P6 B2B SEAD - HR / RS / SI / AL / MK / BA",
"P620210010":"2021 P6 B2B SEAG - AT",
"P620210011":"2021 P6 B2B SEASA - AR / PY / UY",
"P620210012":"2021 P6 B2B SEAU - AU",
"P620210013":"2021 P6 B2B SEB - EE / LV / LT",
"P620210014":"2021 P6 B2B SEBN - BE / BE_FR / NL",
"P620210015":"2021 P6 B2B SEC - SEC",
"P620210016":"2021 P6 B2B SECA - CA_FR / CA",
"P620210017":"2021 P6 B2B SECH - CL",
"P620210018":"2021 P6 B2B SECZ - CZ / SK",
"P620210019":"2021 P6 B2B SEDA - BR",
"P620210020":"2021 P6 B2B SEEG - EG",
"P620210021":"2021 P6 B2B SEF - FR",
"P620210022":"2021 P6 B2B SEG - DE",
"P620210023":"2021 P6 B2B SEGR - GR",
"P620210024":"2021 P6 B2B SEH - HU",
"P620210025":"2021 P6 B2B SEHK - HK / HK_EN",
"P620210026":"2021 P6 B2B SEI - IT",
"P620210027":"2021 P6 B2B SEIB - ES / PT",
"P620210028":"2021 P6 B2B SEIL - IL",
"P620210029":"2021 P6 B2B SEIN - ID",
"P620210030":"2021 P6 B2B SEKZ - KZ_RU",
"P620210031":"2021 P6 B2B SELA - LATIN / LATIN_EN",
"P620210032":"2021 P6 B2B SELV - LEVANT / LEVANT_AR",
"P620210033":"2021 P6 B2B SEM - MX",
"P620210034":"2021 P6 B2B SEMAG - N_AFRICA",
"P620210035":"2021 P6 B2B SENA - SE / DK / FI / NO",
"P620210036":"2021 P6 B2B SENZ - NZ",
"P620210037":"2021 P6 B2B SEPAK - PK",
"P620210038":"2021 P6 B2B SEPCO - PH",
"P620210039":"2021 P6 B2B SEPOL - PL",
"P620210040":"2021 P6 B2B SEPR - PE",
"P620210041":"2021 P6 B2B SERC - RU",
"P620210042":"2021 P6 B2B SEROM - RO / BG",
"P620210043":"2021 P6 B2B SESG - CH / CH_FR",
"P620210044":"2021 P6 B2B SET - TW",
"P620210045":"2021 P6 B2B SETK - TR",
"P620210046":"2021 P6 B2B SEUC - UA",
"P620210047":"2021 P6 B2B SEUK - UK / IE",
"P620210048":"2021 P6 B2B SGE - AE /AE_AR",
"P620210049":"2021 P6 B2B SIEL - IN",
"P620210050":"2021 P6 B2B SME - MY",
"P620210051":"2021 P6 B2B SSA - ZA",
"P620210052":"2021 P6 B2B TSE - TH"
]

def source = 'T7'

//def customFieldManager = ComponentAccessor.getCustomFieldManager();

//MutableIssue mutableIssue = (MutableIssue) issue;


CopyProject copyProject = new CopyProject()

projList.each() {

def inputs = [
(CopyProject.FIELD_SOURCE_PROJECT) : source,
(CopyProject.FIELD_TARGET_PROJECT) : it.key,
(CopyProject.FIELD_TARGET_PROJECT_NAME) : it.value,
(CopyProject.FIELD_COPY_VERSIONS) : false,
(CopyProject.FIELD_COPY_COMPONENTS) : true,
(CopyProject.FIELD_COPY_ISSUES) : false,
(CopyProject.FIELD_COPY_DASH_AND_FILTERS) : false
]


def errorCollection = copyProject.doValidate(inputs, false)
if (errorCollection.hasAnyErrors()) {
	logit.warn("Couldn't create project: $errorCollection")
}
else {

	copyProject.doScript(inputs)
    
   	Project proj = pMgr.getProjectByCurrentKey(it.key)
    logit.info(proj.getName())
    def beast = new RemoveProjectRoles(proj)
    beast.removeRoles()
    beast.addGroupToRole("jira-administrators","Administrators")
    beast.addGroupToRole("B2B Project Admin","Administrators")

	}
}
