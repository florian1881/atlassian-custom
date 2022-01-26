// https://kb.botronsoft.com/x/gYBk
import com.atlassian.greenhopper.model.rapid.RapidView
import com.atlassian.greenhopper.manager.rapidview.RapidViewManager
import com.onresolve.scriptrunner.runner.customisers.JiraAgileBean
import com.atlassian.jira.issue.search.SearchRequestManager
import com.atlassian.jira.component.ComponentAccessor

def fix = false

public class NoCheck implements RapidViewManager.RapidViewPermissionCheck {
    public boolean check(RapidView view) {
        return true
    }
}

@JiraAgileBean RapidViewManager rapidViewManager
SearchRequestManager srm = ComponentAccessor.getComponent(SearchRequestManager)

def removed = []
rapidViewManager.getAll(new NoCheck()).value.each { b ->
    if (srm.getSearchRequestById(b.savedFilterId) == null) {
        if (fix) {
            def res = rapidViewManager.delete(b)
        }
        removed.add("${b.id},${b.name},${b.owner}")
    }
}
def result = removed.join("\n")
return result
