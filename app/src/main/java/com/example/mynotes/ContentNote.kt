package com.example.mynotes

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.ContentActivityBinding
import com.example.mynotes.models.Noted
import com.example.mynotes.ui.detail.ContentFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory

class ContentNote() : AppCompatActivity() {
    private lateinit var binding: ContentActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)
        binding = ContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        title = viewModel.list.name

        val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editNoteText: EditText = findViewById(R.id.editTextTextMultiLine)
        val contented = sharedPreferences.getString(title as String,"Not found")
        if (contented != null) {
            Log.d(TAG, contented)
            editNoteText.setText(contented)
        }else{
            Log.d(TAG, "BROKE")
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()

                .replace(R.id.note_fragment_container, ContentFragment.newInstance())
                .commitNow()
        }
    }
    override fun onBackPressed() {
        val editNoteText: EditText = findViewById(R.id.editTextTextMultiLine)

        viewModel.saveNoted(Noted(viewModel.list.name,editNoteText.text.toString()))

        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY,viewModel.list)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }
}