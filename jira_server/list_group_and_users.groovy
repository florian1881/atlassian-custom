import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.crowd.embedded.api.User
import com.atlassian.crowd.embedded.api.Group;
Issue issue = issue;
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
def groupManager = ComponentAccessor.getGroupManager()
def userName = ""
CustomField releasersName = customFieldManager.getCustomFieldObjectByName( "Releasers Name" );
CustomField releaser = customFieldManager.getCustomFieldObjectByName( "Releaser" );
def userManager = ComponentAccessor.getUserManager()
//def group = userManager.getGroup((String) issue.getCustomFieldValue(releaser))
def cfgvalue =(List<Group>) issue.getCustomFieldValue(releaser);
for(Group group:cfgvalue){
    Collection <ApplicationUser> usersInGroup = groupManager.getUsersInGroup(group)
    for (User user : usersInGroup){
        log.warn(user.getDisplayName())
        if(userName == "") {
            userName = user.getDisplayName()
        } else {
        userName = userName + "," +user.getDisplayName()
        }
    }
}
//log.warn(userName)
//issue.setCustomFieldValue(releasersName,userName)
releasersName.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(releasersName), userName),changeHolder);
