package personal.wuqing.notificationreader.reader

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import personal.wuqing.notificationreader.R

class PreferenceFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.pref)
    }
}