import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.CustomFieldManager;

CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
Collection<CustomField> customFields = customFieldManager.getCustomFieldObjects();
def out = "";
for (CustomField field: customFields) {
    if (field.description != null && field.description.startsWith("{SR}")) {
		// Uncomment this to delete
		//customFieldManager.removeCustomField(field);
        out+=field.name+ " deleted;";
	}
}

return out
