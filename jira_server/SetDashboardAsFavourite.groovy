import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.favourites.FavouritesManager
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.favourites.FavouritesManager
import com.atlassian.jira.portal.PortalPage
import com.atlassian.jira.portal.PortalPageManager
import com.atlassian.jira.user.ApplicationUser
// Set the filter ID and group to share with here
Long dashboardId = 10803
String group1 = "jira-software-users"
Collection<String> shareWith = new ArrayList()
shareWith.add(group1)
ApplicationUser user

FavouritesManager favouritesManager = ComponentAccessor.getComponentOfType(FavouritesManager.class);

GroupManager groupManager = ComponentAccessor.getGroupManager()
PortalPageManager portalPageManager = ComponentAccessor.getComponentOfType(PortalPageManager.class);
PortalPage portalPage = portalPageManager.getPortalPageById(dashboardId)

UserManager userManager = ComponentAccessor.getUserManager()
for (String groupName in shareWith) {
    log.info(groupName)
    Group group = groupManager.getGroup(groupName)
	for (String userId in groupManager.getUserNamesInGroup(group)) {
        log.info("id: " + userId)
        user = userManager.getUserByKey(userId)
        log.info("user: "+ user)
        if (user != null) {
            log.info("Add " + portalPage.getName() + " to user favourites for " + user.getDisplayName())
    		favouritesManager.addFavourite(user, portalPage)
        }
	}
}