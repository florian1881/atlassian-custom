import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue

// this comes to us in the binding... so this line is unnecessary other than to give my IDE "type" information
Issue issue = issue

// get some components we need
def watcherManager = ComponentAccessor.getWatcherManager()
def userManager = ComponentAccessor.getUserManager()

// would be better to use componentLead rather than lead, but not available until 6.3
def list = issue.getComponents()
for (c in list) {
    def applicationUser = userManager.getUserByKey(c.getLead())
    watcherManager.startWatching(applicationUser, issue)
}
