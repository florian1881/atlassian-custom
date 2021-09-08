
//import com.atlassian.jira.issue.comments.MutableComment
//import com.atlassian.jira.issue.comments.Comment
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager

import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.bc.issue.search.SearchService 
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.bc.issue.IssueService.IssueValidationResult
import com.atlassian.jira.bc.issue.IssueService.UpdateValidationResult
import com.atlassian.jira.bc.issue.IssueService.IssueResult
def logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
// user replacements
// **********************************************
// key : value
def users = [
"yasin.t@domain1eu.com":"yasin.t@domain1.com",
"wenni.chong@domain1eu.com":"wenni.chong@domain1.com",
"wahmiin.wong@domain1eu.com":"wahmiin.wong@domain1.com",
"w.rzepka@domain1eu.com":"w.rzepka@domain1.com",
"vikas.bh@domain1eu.com":"vikas.bh@domain1.com",
"vi.silva@domain1eu.com":"vi.silva@domain1.com",
"v.pokhvaleny@domain1eu.com":"v.pokhvaleny@domain1.com",
"tariq.i@domain1eu.com":"tariq.i@domain1.com",
"t.choroba@domain1eu.com":"t.choroba@domain1.com",
"serdar.o@domain1eu.com":"serdar.o@domain1.com",
"sebastian.z@domain1eu.com":"sebastian.z@domain1.com",
"saurabh.kulkarni@domain1eu.com":"saurabh.kulkarni@domain1.com",
"sarah.z@domain1eu.com":"sarah.z@domain1.com",
"sarah.sousa@domain1eu.com":"sarah.sousa@domain1.com",
"robert.ong@domain1eu.com":"robert.ong@domain1.com",
"richard.cho@domain1eu.com":"richard.cho@domain1.com",
"rhea.iyer@domain1eu.com":"rhea.iyer@domain1.com",
"renato.me@domain1eu.com":"renato.me@domain1.com",
"ramsey.k@domain1eu.com":"ramsey.k@domain1.com",
"r.diaz@domain1eu.com":"r.diaz@domain1.com",
"r.chygryn@domain1eu.com":"r.chygryn@domain1.com",
"polaris@domain1eu.com":"polaris@domain1.com",
"pauline.wong@domain1eu.com":"pauline.wong@domain1.com",
"paul.moncada@domain1eu.com":"paul.moncada@domain1.com",
"p.szadkowski@domain1eu.com":"p.szadkowski@domain1.com",
"p.noubissie@domain1eu.com":"p.noubissie@domain1.com",
"p.dzienny@domain1eu.com":"p.dzienny@domain1.com",
"o.kobierecki@domain1eu.com":"o.kobierecki@domain1.com",
"nowicki.p@domain1eu.com":"nowicki.p@domain1.com",
"navin.c@domain2.com":"navin.c@domain1.com",
"n.bondarenko@domain1eu.com":"n.bondarenko@domain1.com",
"monica.abreu@domain1eu.com":"monica.abreu@domain1.com",
"moazzam.k@domain1eu.com":"moazzam.k@domain1.com",
"matias.tridico@domain1eu.com":"m.tridico@domain1.com",
"maria.hill@domain1eu.com":"maria.hill2@domain1.com",
"malavika.n@domain1eu.com":"malavika.n@domain1.com",
"m.zylinkski@domain1eu.com":"m.zylinski@domain1.com",
"m.magdziak@ad.client.domain1eu.com":"m.magdziak@domain1.com",
"m.bankowski@domain1eu.com":"m.bankowski@domain1.com",
"luisa.m@domain1eu.com":"luisa.m@domain1.com",
"lucas.rittes@domain1eu.com":"lucas.rittes@domain1.com",
"luana.l@domain1eu.com":"luana.l@domain1.com",
"lilian.reis@domain1eu.com":"lilian.reis@domain1.com",
"l.gruzewski@domain1eu.com":"l.gruzewski@domain1.com",
"kathleen.collins@domain1eu.com":"kathleen.collins@domain1.com",
"k.jarosinska@domain1eu.com":"k.jarosinska@domain1.com",
"k.jakubowski@domain1eu.com":"k.jakubowski@domain1.com",
"juliana.j@domain1eu.com":"juliana.j@domain1.com",
"juan.wilches@domain1eu.com":"juan.wilches@domain1.com",
"joshua.rosas@domain1eu.com":"joshua.rosas@domain1.com",
"jose.miselem@domain1eu.com":"jose.miselem@domain1.com",
"jinu.isabel@domain1eu.com":"jinu.isabel@domain1.com",
"jimmy.c@domain1eu.com":"jimmy.c@domain1.com",
"jessica.wh@domain1eu.com":"jessica.wh@domain1.com",
"j.vergara@domain1eu.com":"j.vergara@domain1.com",
"j.gomez@domain1eu.com":"j.gomez@domain1.com",
"ivy.h@domain1eu.com":"ivy.h@domain1.com",
"isabel.l@domain1eu.com":"isabel.l@domain1.com",
"heejae.c@domain1eu.com":"heejae.c@domain1.com",
"hassan.a@domain1eu.com":"hassan.a@domain1.com",
"hasan.c@domain1eu.com":"hasan.c@domain1.com",
"gustavo.s@domain1eu.com":"gustavo.s@domain1.com",
"gurinder.s@domain1eu.com":"gurinder.s@domain1.com",
"fouzia.s@domain1eu.com":"fouzia.s@domain1.com",
"felipe.sf@domain1eu.com":"felipe.sf@domain1.com",
"felipe.leon@domain1eu.com":"felipe.leon@domain1.com",
"fatema.z@domain1eu.com":"fatema.z@domain1.com",
"fakhri.a@domain1eu.com":"fakhri.a@domain1.com",
"fabio.murazawa@domain1eu.com":"fabio.murazawa@domain1.com",
"f.cavaco@domain1eu.com":"f.cavaco@domain1.com",
"eugene.p@domain1eu.com":"eugene.p@domain1.com",
"eduardo.ml@domain1eu.com":"eduardo.ml@domain1.com",
"e.rodriguez@domain1eu.com":"e.rodriguez@domain1.com",
"e.paulo@domain2.com":"e.paulo@domain1.com",
"e.malinowski@domain1eu.com":"e.malinowski@domain1.com",
"dkwan.kim@domain1eu.com":"dkwan.kim@domain1.com",
"dilan.ramos@domain1eu.com":"dilan.ramos@domain1.com",
"dia-bi@domain1eu.com":"ses.bounce@domain1.com",
"dhirendra.p@domain1eu.com":"dhirendra.p@domain1.com",
"d.beringer@domain1eu.com":"d.beringer@domain1.com",
"clara.h@domain1eu.com":"clara.h@domain1.com",
"cdm.oper18@domain1eu.com":"cdm.oper18@domain1.com",
"cdm.oper14@domain1eu.com":"cdm.oper14@domain1.com",
"bruno.rosa@domain1eu.com":"bruno.rosa@domain1.com",
"bohyun.kim1@domain1eu.com":"bohyun.kim1@domain1.com",
"alexandra.f@domain1eu.com":"alexandra.f@domain1.com",
"akash.l@domain1eu.com":"akash.l@domain1.com",
"abdiel.g@domain1eu.com":"abdiel.g@domain1.com",
"a.tomczak@domain1eu.com":"a.tomczak@domain1.com",
"a.shurbaji@domain1eu.com":"a.shurbaji@domain1.com",
"a.pielach@domain1eu.com":"a.pielach@domain1.com",
"a.lisek@domain1eu.com":"a.lisek@domain1.com",
"a.hernandez@domain1eu.com":"a.hernandez@domain1.com",
"a.filipiak@domain1eu.com":"a.filipiak@domain1.com"
]
String oldUser
String newUser
String assigneeclause1 = 'assignee in ( "' 
String reporterclause1 = 'reporter in ( "' 
String clause2 = '")'

// set true to add key (lefthand value), set false to add value (righthand value)
boolean adduser = true
def jqlSearch 
List<Issue> issues = null
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
UserManager userMgr = ComponentAccessor.getUserManager()
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
IssueManager issueManager = ComponentAccessor.getIssueManager()

IssueService issueService = ComponentAccessor.issueService

def searchResult

users.each() {
    if (adduser) {
        oldUser = it.key
        newUser = it.value
    } else {
        oldUser = it.value
        newUser = it.key
    }
    // ASSIGNEE
    jqlSearch = assigneeclause1 + oldUser + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
    	searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
        StringBuffer sBuf1 = new StringBuffer()
    	searchResult.getResults().each { issue ->
           sBuf1.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setAssigneeId(newUser)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
    			IssueResult updateResult = issueService.update(user, updateValidationResult);
    			if (!updateResult.isValid())
    			{
        			sBuf1.append("Y, ")
    			}
			}
	    }
        logit.info(sBuf1.toString())
        } else {
    		logit.error("Invalid JQL: " + jqlSearch);
		}
   
    // REPORTER
    jqlSearch = reporterclause1 + oldUser + clause2
    logit.info( "" + jqlSearch)
    SearchService.ParseResult parseResult2 =  searchService.parseQuery(user, jqlSearch)
    if (parseResult.isValid()) {
   	searchResult = searchService.search(user, parseResult2.getQuery(), PagerFilter.getUnlimitedFilter())
        //logit.info(searchResult.getResults().size())
        StringBuffer sBuf2 = new StringBuffer()
    	searchResult.getResults().each { issue ->
        sBuf2.append(issue.getKey() + ", ")
           def issueInputParameters = issueService.newIssueInputParameters()
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setReporterId(newUser)
            UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.id, issueInputParameters);
			if (updateValidationResult.isValid())
			{
    			IssueResult updateResult = issueService.update(user, updateValidationResult);
    			if (!updateResult.isValid())
    			{
        			sBuf2.append("Y, ")
    			}
			}
	    }
        logit.info(sBuf2.toString())
        } else {
    		log.error("Invalid JQL: " + jqlSearch);
		}
        
    }

