package com.example.mynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.MainActivityBinding
import com.example.mynotes.models.Noted
import com.example.mynotes.ui.detail.ContentFragment
import com.example.mynotes.ui.main.MainFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }

        binding.taskListAddButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            if (viewModel.findNoted(listTitleEditText.text.toString())){
                Toast.makeText(this,"The name has been already taken", Toast.LENGTH_LONG).show()
            }else {
                val noteList = Noted(listTitleEditText.text.toString(), "")
                viewModel.create(noteList)
                showNote(noteList)
            }
        }
        builder.create().show()
    }

    private fun showNote(list: Noted) {
        if (binding.mainFragmentContainer == null) {
            val noteIntent = Intent(this, ContentNote::class.java)
            noteIntent.putExtra(INTENT_LIST_KEY, list)
            startActivity(noteIntent)
        }else{
            val bundle = bundleOf(INTENT_LIST_KEY to list)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.subnote_fragment, ContentFragment::class.java,bundle,null)
            }
        }
    }

    private fun showRemoveDialog(list: Noted) {
        val dialogTitle = "Do you want to removing note ${list.name}!"
        val positiveButtonTitle = "Yes"
        val negativeButtonTitle = "No"

        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            viewModel.removeNoted(list)

            viewModel = ViewModelProvider(this,
                MainViewModelFactory(android.preference.PreferenceManager.getDefaultSharedPreferences(this))
            )
                .get(MainViewModel::class.java)
            binding = MainActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }


            binding.taskListAddButton.setOnClickListener {
                showCreateListDialog()
            }

        }
        builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun listItemTapped(list: Noted) {
        showNote(list)
    }

    override fun listItemHold(list: Noted) {
        showRemoveDialog(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.updateNoted(data.getParcelableExtra(INTENT_LIST_KEY)!!)
            }
        }
    }

    override fun onBackPressed() {
        val listNoteFragment = supportFragmentManager.findFragmentById(R.id.subnote_fragment)
        if (listNoteFragment == null) {
            super.onBackPressed()

        } else {
            title = resources.getString(R.string.app_name)
            val editNoteText: EditText = findViewById(R.id.editTextTextMultiLine)
            viewModel.saveNoted(Noted(viewModel.list.name,editNoteText.text.toString()))
            editNoteText.setText("")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listNoteFragment)
            }
            binding.taskListAddButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
}