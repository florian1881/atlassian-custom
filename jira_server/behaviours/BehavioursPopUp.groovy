import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.jira.groovy.user.FormField
import com.onresolve.jira.groovy.user.FieldBehaviours
import java.text.SimpleDateFormat
import org.joda.time.DateTime
import java.util.Date

FormField foto = getFieldByName("Дата выхода рубрики в эфир")
FormField deadline = getFieldByName("Deadline")
FormField summary = getFieldById('summary')
def simpleDateFormat = new SimpleDateFormat("dd/MMM/yy")
def newSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
def dataVihodaEfiraValueNew = simpleDateFormat.format(foto.getValue())
def Deadline = simpleDateFormat.format(new DateTime(foto.getValue()).minusDays(1).toDate())
def today = new Date()
def today1 = simpleDateFormat.format(new DateTime(today).plusDays(3).toDate())
deadline.setFormValue(Deadline)
summary.setFormValue(today1)
if(Deadline < today1){
 deadline.setOverlay("Дата эфира должна должна быть больше текущей даты на 3 дня, иначе указываем во вкладе Прочее, выбрать в поле ЧС = Да  !!!")
//descriptionFoto.setFormValue(today)
}
