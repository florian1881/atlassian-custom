import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.plugin.PluginAccessor;
import com.valiantys.software.elements.api.content.*;
import com.valiantys.software.elements.api.model.*;
//import com.valiantys.software.elements.api.content.ReadOptions;


PluginAccessor pluginAccessor = ComponentAccessor.getPluginAccessor();
Class panelContentServiceClass = pluginAccessor.getClassLoader().findClass("com.valiantys.software.elements.api.content.PanelContentService");
Class panelRefClass = pluginAccessor.getClassLoader().findClass("com.valiantys.software.elements.api.model.PanelRef");
Class attributeRefClass = pluginAccessor.getClassLoader().findClass("com.valiantys.software.elements.api.model.AttributeRef");
 
def issueManager = ComponentAccessor.getIssueManager();
def panelContentService = ComponentAccessor.getOSGiComponentInstanceOfType(panelContentServiceClass);
 
def readOptionClass = pluginAccessor.getClassLoader().findClass("com.valiantys.software.elements.api.content.ReadOptions")
def readOptions = ComponentAccessor.getOSGiComponentInstanceOfType(readOptionClass);
// Define Elements panel
def panelContent = panelContentService.getPanel(issue, panelRefClass.byName("eDocs"),readOptionClass.withEntities());
 
// Get Elements panel items
def tab = ""
for (def panelItem : panelContent.getPanelItems()) {
    tab += panelItem .getAttributeContent(attributeRefClass.byName("Document Type"))?.getValue() + " - ";
    tab += panelItem .getAttributeContent(attributeRefClass.byName("Attachment"))?.getValue() + " - ";
}
return tab;