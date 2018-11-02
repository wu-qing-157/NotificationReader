package personal.wuqing.notificationreader

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_main.*
import personal.wuqing.notificationreader.io.readAvailableAppList
import personal.wuqing.notificationreader.status.addStatus
import personal.wuqing.notificationreader.io.saveAvailableAppList
import java.text.Collator
import java.util.function.Function

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val (packageNameList, errList) = readAvailableAppList(this)
        updateAvailableAppList(packageNameList)
        for (err in errList) addStatus(this, layout_status, err)
        button_choose_apps.setOnClickListener {
            val (lambdaPackageNameList, lambdaErrList) = readAvailableAppList(this)
            for (lambdaErr in lambdaErrList) addStatus(this, layout_status, lambdaErr)
            val lambdaPackageNameSet = HashSet<String>(lambdaPackageNameList)
            val checkBoxList = mutableListOf<CheckBox>()
            val applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (info in applicationInfoList) {
                val checkBox = CheckBox(this)
                checkBox.text = info.loadLabel(packageManager)
                checkBox.isChecked = lambdaPackageNameSet.contains(info.packageName)
                checkBox.tag = info.packageName
                checkBoxList += checkBox
            }
            checkBoxList.sortWith(Comparator.comparing( Function { checkBox -> checkBox.text.toString() }, Collator.getInstance() ))
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            for (checkBox in checkBoxList)
                layout.addView(checkBox)
            val scrollView = ScrollView(this)
            scrollView.addView(layout)
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.choose_app)
            builder.setView(scrollView)
            builder.setPositiveButton(R.string.confirm) { _, _ -> run {
                val newPackageNameList = mutableListOf<String>()
                for (checkBox in checkBoxList)
                    if (checkBox.isChecked)
                        newPackageNameList += checkBox.tag as String
                saveAvailableAppList(this, newPackageNameList)
                updateAvailableAppList(newPackageNameList)
            }}
            builder.setNegativeButton(R.string.cancel, null)
            builder.show()
        }
    }

    private fun updateAvailableAppList(list: MutableList<String>) {
        val nameList = mutableListOf<String>()
        for (packageName in list) {
            try {
                val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                nameList.add(applicationInfo.loadLabel(packageManager).toString())
            } catch (e: PackageManager.NameNotFoundException) {
                addStatus(this, layout_status, "Cannot find package \"$packageName\"")
            }
        }
        textView_available_apps.text = nameList.joinToString("\n", "", "", -1, "...")
    }
}
