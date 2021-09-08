if(cfValues['直接移交给开发人员'].getValue() == "是") 
    return (cfValues['测试人'] != null)
else return 1;

if(cfValues['直接移交给开发人员'].getValue() == '是') 
    return (issue.getComponents().size() != 0)
else return 1;

if(cfValues['直接移交给开发人员'].getValue() == '是') 
    return (cfValues['Sprint'] != null)
else return 1;

issue.getSummary().contains("测试用例")

["Story","Bug"].contains(issue.parentObject?.issueType.name)

import com.atlassian.jira.component.ComponentAccessor

def groupManager = ComponentAccessor.getGroupManager()
groupManager.isUserInGroup(cfValues['验收人'], 'jira-administrators') || cfValues['验收人'] == null || (cfValues['验收人'] == issue.getProjectObject().getProjectLead())||(cfValues['验收人'] == cfValues['设计人'])||(cfValues['验收人'] == cfValues['测试人'])

if(issue.getSummary().contains("测试用例") || issue.getSummary().contains("完成需求设计宣讲"))
	return 1
else
	return issue.fixVersions.size()
