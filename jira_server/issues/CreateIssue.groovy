// Скрипт в постфункции, создает задачу в проекте и связывает ее с родительской
// иак же копирует некоторые поля из родительской
//
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.issue.fields.config.FieldConfig
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.customfields.option.LazyLoadedOption
import com.atlassian.jira.issue.customfields.option.OptionsImpl
import java.text.SimpleDateFormat
import java.util.Date
import org.joda.time.DateTime
import org.apache.log4j.Level
import org.apache.log4j.Logger
log = Logger.getLogger("com.tv.CreateIssue")
log.setLevel(Level.DEBUG)

def locale = ComponentAccessor.applicationProperties.defaultLocale
def simpleDateFormat = new SimpleDateFormat("dd/MMM/yy HH:mm", locale)
def newSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")

def linkMgr = ComponentAccessor.getIssueLinkManager()
def issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey("PP1-18842")
def customField = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_11601")
def dataVihodaEfira = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_11504") //Дата выхода рубрики в эфир
def dataVihodaEfiraValueOld = issue.getCustomFieldValue(dataVihodaEfira)
def dataVihodaEfiraValueNew = simpleDateFormat.format(dataVihodaEfira.getValue(issue))
def Deadline = simpleDateFormat.format(new DateTime(dataVihodaEfiraValueOld).minusDays(1).toDate()) // Заполняем поле Deadline
log.debug("Дата выхода рубрики в эфир в задаче Промо: " + dataVihodaEfiraValueOld)
log.debug("Дата выхода рубрики в эфир для задачи Оперативная графика: " + dataVihodaEfiraValueNew)
log.debug("Дата делайна для Оперативной графики: " + Deadline)


def rubrika = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_10501") //Рубрика
String rubrikaValueId = rubrika.getValue(issue)?.optionId
def rubrikaValue = rubrika.getValue(issue)
log.debug("Рубрика: " + rubrikaValue)

def znachimost = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_11508") //Значимость
String znachimostValueId = znachimost.getValue(issue)?.optionId
def znachimostValue = znachimost.getValue(issue)
log.debug("Значимость: " + znachimostValue)

def graficaDescription = ComponentAccessor.customFieldManager.getCustomFieldObject("customfield_11502") // Графика - описание идеи/расписание
def graficaDescriptionValue = graficaDescription.getValue(issue)
log.debug("Графика - описание идеи/расписание: " + graficaDescriptionValue)

def valueCF = customField.getValue(issue)*.value   
if ("Моушен" in valueCF) {

	def issueTypeName = "Оперативная графика"
	def issueService = ComponentAccessor.issueService
	def constantsManager = ComponentAccessor.constantsManager
	def loggedInUser = ComponentAccessor.jiraAuthenticationContext.loggedInUser
	def projectId = ComponentAccessor.projectManager.getProjectByCurrentKey("SDTEST")
	log.debug(projectId.getId())
	def issueType = constantsManager.allIssueTypeObjects.findByName(issueTypeName)
	log.debug(issueType.id)
	def reporter = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
	log.debug(reporter.getKey())
	def priority = constantsManager.priorities.findByName("Major") ?: constantsManager.defaultPriority
	log.debug(priority.id)
	def issueInputParameters = issueService.newIssueInputParameters().with {
    	setProjectId(projectId.getId())
    	setIssueTypeId(issueType.id)
    	setReporterId(reporter.getKey())
    	setSummary(issue.summary)
    	setPriorityId(priority.id)
        setDescription(graficaDescriptionValue)
    	addCustomFieldValue("customfield_11504", dataVihodaEfiraValueNew)
        addCustomFieldValue("customfield_10501", rubrikaValueId)
        addCustomFieldValue("customfield_11508", znachimostValueId)
        //addCustomFieldValue("customfield_13503", [12345, 12345]) // Шаблоны
        addCustomFieldValue("customfield_12500", Deadline)  // Deadline
        
	}
	def validationResult = issueService.validateCreate(loggedInUser, issueInputParameters)
    log.debug(validationResult.errorCollection)
	def result = issueService.create(loggedInUser, validationResult)
    log.debug(result.issue.id)
    linkMgr.createIssueLink(issue.id, result.issue.id, 10000, 1, loggedInUser)
 
}
