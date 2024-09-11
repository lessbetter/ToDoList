package com.example.todolist.settings


import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.todolist.R
import com.example.todolist.viewmodel.TodoViewModel

class MySettingsFragment: PreferenceFragmentCompat() {

    private val viewModel: TodoViewModel by activityViewModels()


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        var categoryList: Array<CharSequence> = arrayOf()

        categoryList += "All"


        viewModel.categoryList.value?.forEach {
            categoryList += it
        }




        val categoryPreference: ListPreference? = findPreference("category")

        if(categoryPreference!=null){
            categoryPreference.entries = categoryList
            categoryPreference.entryValues = categoryList
//            categoryPreference.summary = "debug"
//            categoryList.forEach {
//                Log.d(TAG, it.toString())
//            }
        }



    }



}