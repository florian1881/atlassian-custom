package com.onresolve.jira.groovy.jql
 
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.link.IssueLinkManager
import com.atlassian.jira.issue.link.IssueLink
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.util.MessageSet
import com.atlassian.jira.util.MessageSetImpl
 
import com.atlassian.jira.JiraDataType
import com.atlassian.jira.JiraDataTypes
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.jql.operand.QueryLiteral
import com.atlassian.jira.jql.query.QueryCreationContext
import com.atlassian.jira.permission.ProjectPermissions
import com.atlassian.jira.project.version.VersionManager
import com.atlassian.query.clause.TerminalClause
import com.atlassian.query.operand.FunctionOperand
import com.atlassian.jira.issue.link.IssueLink
 
class issueinEpic extends AbstractScriptedJqlFunction implements JqlFunction {
 
    @Override
    String getDescription() {
        "Get issue in Epic and subtask in issue "
    }
   
 
    @Override
    List<Map> getArguments() {
        [
            [
                description: "Epic Key",
                optional: false,
            ]
        ]
    }
   
 
    @Override
    String getFunctionName() {
        "issueinEpic"
    }
   
 
    @Override
    JiraDataType getDataType() {
        JiraDataTypes.ISSUE
    }
	
    @Override
    MessageSet validate(ApplicationUser user, FunctionOperand operand, TerminalClause terminalClause) {
        Issue issue = ComponentAccessor.getIssueManager().getIssueObject(operand.args.first())
		MessageSet messages = new MessageSetImpl()
        if (issue == null) {
            messages.addErrorMessage ("Issue not found")
        }
        return messages
    }
   
 
    @Override
    List<QueryLiteral> getValues( QueryCreationContext queryCreationContext, FunctionOperand operand, TerminalClause terminalClause) {
 
        List<QueryLiteral> out = []
        Issue issue = ComponentAccessor.getIssueManager().getIssueObject(operand.args.first().toUpperCase())
		//def issues = issueLinkManager.getLinkCollection(issue, user).getAllIssues()
        if (issue == null) {
            retrun out
        }
 
        def issueLinkManager = ComponentAccessor.getIssueLinkManager()
         
        // Получаем связанные задачи у линка
        //def inwardLinks = issueLinkManager.getInwardLinks(issue.getId())	// Входящая связь
        def outwardLinks = issueLinkManager.getOutwardLinks(issue.getId()) // Исходящая связь
        // для исходящих
        for (IssueLink link: outwardLinks) {
			//if (link.getIssueLinkType().getName() == "Blocks" || link.getIssueLinkType().getName() == "Duplicate") { // Фильтруем линки по типу
			if (link.getIssueLinkType().getName() == "Epic-Story Link") {
			        out.add(new QueryLiteral(operand, link.getDestinationObject().getKey()))
					def subtasks = link.getDestinationObject().getSubTaskObjects()
						for (Issue SubTask: subtasks) {
							out.add(new QueryLiteral(operand, SubTask.getKey()))
						}
            }
        }
        // для входящих
        //for (IssueLink link: inwardLinks) {
        //  if (link.getIssueLinkType().getName() == "Blocks" || link.getIssueLinkType().getName() == "Duplicate") {
        //      out.add(new QueryLiteral(operand, link.getSourceObject().getKey()))
        //  }
        //}
 
 
    return out
    }
}