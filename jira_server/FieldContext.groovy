import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.ConfigurableField;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.config.manager.FieldConfigSchemeManager;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.context.ProjectContext;
import com.atlassian.jira.issue.context.IssueContextImpl;
import com.atlassian.jira.issue.context.JiraContextNode;
import java.util.ArrayList;
    CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
    FieldConfigSchemeManager fieldConfigSchemeManager = ComponentAccessor.getFieldConfigSchemeManager();
    FieldManager fieldManager = ComponentAccessor.getFieldManager();
    
    ArrayList<CustomField> customFields = new ArrayList<CustomField>();
    
    customFields.add(customFieldManager.getCustomFieldObject(14147L));
    
	Long pid = 13040;
    ProjectContext newProjectContext = new ProjectContext(pid);
	IssueContextImpl newIssueContext = new IssueContextImpl(pid,"Test SR Project");
    
    for (customField in customFields) {
        println customField.getId();
        List<FieldConfigScheme> fieldConfigSchemes = customField.getConfigurationSchemes();
        
        FieldConfigScheme firstScheme = fieldConfigSchemes.get(0);
        
        List<JiraContextNode> existingSchemes = firstScheme.getContexts();
        JiraContextNode newContextNode = (JiraContextNode) newProjectContext;
        JiraContextNode newIssueContextNode = (JiraContextNode) newIssueContext;
        
        List<JiraContextNode> updatedSchemes = new ArrayList<JiraContextNode>();
        for (scheme in existingSchemes) {
            updatedSchemes.add(scheme);
        }
        //updatedSchemes.add(newContextNode);
        updatedSchemes.add(newIssueContextNode);
        
        fieldConfigSchemeManager.updateFieldConfigScheme(firstScheme, updatedSchemes, (ConfigurableField) customField);
        
    }
customFieldManager.refresh();