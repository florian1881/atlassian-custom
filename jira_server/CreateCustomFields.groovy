// Cheil EU Jira Tools Tom Lister June 2019

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option
import com.atlassian.jira.issue.customfields.option.Options
import com.atlassian.jira.issue.context.GlobalIssueContext;
import com.atlassian.jira.issue.context.JiraContextNode;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig
import com.atlassian.jira.issue.fields.config.FieldConfigScheme
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.fields.screen.FieldScreenManager;
import com.atlassian.jira.issue.fields.screen.FieldScreenTab;
import com.atlassian.jira.issue.issuetype.IssueType;
import java.util.ArrayList;
import java.util.List;
import org.ofbiz.core.entity.GenericEntityException;
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger

CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
FieldScreenManager fieldScreenManager = ComponentAccessor.getFieldScreenManager();
OptionsManager optionsManager = ComponentAccessor.getOptionsManager()

String TEXT_CF = "Trace Text";
String CHECKBOX = "com.atlassian.jira.plugin.system.customfieldtypes:multicheckboxes"
String CHECKBOX_SEARCHER = "com.atlassian.jira.plugin.system.customfieldtypes:multiselectsearcher"
String SELECT ="com.atlassian.jira.plugin.system.customfieldtypes:multiselect"
String SELECT_SEARCHER = "com.atlassian.jira.plugin.system.customfieldtypes:multiselectsearcher"
String TEXT = "com.atlassian.jira.plugin.system.customfieldtypes:textfield"
String TEXT_SEARCHER = "com.atlassian.jira.plugin.system.customfieldtypes:textsearcher"
String SINGLE_SELECT = "com.atlassian.jira.plugin.system.customfieldtypes:select"
String SINGLE_SELECT_SEARCHER = "com.atlassian.jira.plugin.system.customfieldtypes:multiselectsearcher"
	

		List<IssueType> issueTypes = new ArrayList<IssueType>();
		//issueTypes.add(null);

		// Create a list of project contexts for which the custom field needs to be
		// available
		List<JiraContextNode> contexts = new ArrayList<JiraContextNode>();
		JiraContextNode node
       // add B2B/B2C
        def fields = ["Retail Internal Brief Flag",
"Retail Planning and Design Flag",
"Retail Prototype Flag",
"Retail Production Flag",
"Retail Logistics Flag",
"Retail Installation Flag",
"Retail Snagging Flag",
"Retail Reporting Flag"]
fields.each() {String name ->
		String[] values = ["Y"]	
		def cField = createOptionField(name, "", issueTypes, 
                                       contexts, values, customFieldManager, fieldScreenManager, optionsManager, SINGLE_SELECT,SINGLE_SELECT_SEARCHER);

}

// end script

// utility methods
// TODO set default select option value

public CustomField createTextField(String name, String description, List<IssueType> issueTypes,
			List<JiraContextNode> contexts, CustomFieldManager customFieldManager, FieldScreenManager fieldScreenManager, String type, String searcher) throws GenericEntityException {
		CustomField cField = customFieldManager.getCustomFieldObject(name);
		if (!cField) {
			cField = customFieldManager.createCustomField(name, description,
					customFieldManager
							.getCustomFieldType(type),
					customFieldManager
							.getCustomFieldSearcher(searcher),

					contexts, issueTypes);
            		addToDefaultScreen(cField, fieldScreenManager);

		}
		return cField;
	}

	public CustomField createMultiCheckbox(String name, String description, List<IssueType> issueTypes,
			List<JiraContextNode> contexts, String[] options, CustomFieldManager customFieldManager, FieldScreenManager fieldScreenManager, OptionsManager optionsManager,
                                          String type, String searcher)
			throws GenericEntityException {
                createOptionField(name, description, issueTypes,
			contexts, options,  customFieldManager,  fieldScreenManager,  optionsManager, type, searcher)
            }

	public CustomField createMultiSelect(String name, String description, List<IssueType> issueTypes,
			List<JiraContextNode> contexts, String[] options, CustomFieldManager customFieldManager, FieldScreenManager fieldScreenManager, OptionsManager optionsManager,
                                        String type, String searcher)
			throws GenericEntityException {
                createOptionField(name, description, issueTypes,
			contexts, options,  customFieldManager,  fieldScreenManager,  optionsManager, type, searcher)
            }

	public  CustomField createOptionField(String name, String description, List<IssueType> issueTypes,
			List<JiraContextNode> contexts, String[] options, CustomFieldManager customFieldManager, FieldScreenManager fieldScreenManager, OptionsManager optionsManager,
                                         String type, String searcher)
			throws GenericEntityException {
		CustomField cField = customFieldManager.getCustomFieldObject(name);
		if (!cField) {
			cField = customFieldManager.createCustomField(name, description,
					customFieldManager
							.getCustomFieldType(type),
					customFieldManager.getCustomFieldSearcher(searcher),

					contexts, issueTypes);
            addToDefaultScreen(cField, fieldScreenManager);
            for(value in options) {
                addOptionToCustomField(cField, value, optionsManager)
            }
		}
		return cField;
	}

	public  void addToDefaultScreen(CustomField cField, FieldScreenManager fieldScreenManager) {
		// Add field to default Screen
		FieldScreen defaultScreen = fieldScreenManager.getFieldScreen(FieldScreen.DEFAULT_SCREEN_ID);
		if (!defaultScreen.containsField(cField.getId())) {

			FieldScreenTab firstTab = defaultScreen.getTab(0);
			firstTab.addFieldScreenLayoutItem(cField.getId());
		}
	}
	public Option addOptionToCustomField(CustomField customField, String value,OptionsManager optionsManager) {
		Option newOption = null;

		if (customField != null) {
			List<FieldConfigScheme> schemes = customField
					.getConfigurationSchemes();
			if (schemes != null && !schemes.isEmpty()) {
				FieldConfigScheme sc = schemes.get(0);
				Map configs = sc.getConfigsByConfig();
				if (configs != null && !configs.isEmpty()) {
					FieldConfig config = (FieldConfig) configs.keySet()
							.iterator().next();
                    
                   

					//OptionsManager optionsManager = getOptionsManager();
					//Options l = optionsManager.getOptions(config);
					//Option opt;

					int numberAdded = 100;
					newOption = optionsManager.createOption(config, null,
							new Long(numberAdded), // TODO What is this
							value);
                    
				}
			}
		}

		return newOption;
	}
