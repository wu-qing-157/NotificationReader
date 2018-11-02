package personal.wuqing.notificationreader.io

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun saveAvailableAppList(context: Context, list: MutableList<String>) {
    val fileOutputStream = context.openFileOutput("available_app_list", Context.MODE_PRIVATE)
    val objectOutputStream = ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(list)
    objectOutputStream.close()
    fileOutputStream.close()
}

fun readAvailableAppList(context: Context): Pair<MutableList<String>, MutableList<String>> {
    try {
        val err = mutableListOf<String>()
        val fileInputStream = context.openFileInput("available_app_list")
        val objectInputStream = ObjectInputStream(fileInputStream)
        val list = objectInputStream.readObject() as MutableList<*>
        val ret = mutableListOf<String>()
        for (packageName in list) {
            if (packageName !is String)
                err += "Invalid config file \"available_app_list\""
            else
                ret.add(packageName)
        }
        return Pair(ret, err)
    } catch (e: FileNotFoundException) {
        return Pair(mutableListOf(), mutableListOf("Cannot find config file \"available_app_list\""))
    } catch (e: IOException) {
        return Pair(mutableListOf(), mutableListOf("Unknown IO Exception"))
    }
}
