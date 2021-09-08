//https://scriptrunner.adaptavist.com/latest/jira/resources.html
import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.jira.groovy.user.FormField
import com.onresolve.scriptrunner.db.DatabaseUtil
import groovy.sql.Sql

FormField lotTovara = getFieldByName("Лот товара")
def valueLottovara = lotTovara.getValue()
FormField summary = getFieldById('summary')
def rows = []
def value = DatabaseUtil.withSql('Test') {
    it.call '{call ksas_exp.pr\$get_lot_photo(?,?,?)}', [valueLottovara, Sql.VARCHAR, Sql.VARCHAR], {name, path ->       
        rows.add(name)
        rows.add(path)
    }
}
def valueSummary = summary.getValue()
summary.setFormValue("" + rows[0] + "_" + valueSummary)
