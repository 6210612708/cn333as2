package com.example.mynotes.ui.detail

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.example.mynotes.models.Noted
import com.example.mynotes.ui.main.MainViewModel

class ContentFragment : Fragment() {

    companion object {
        fun newInstance() = ContentFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val note: Noted? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)

        note?.let{
            viewModel.list = note
            requireActivity().title = note.name
            val text: EditText = requireActivity().findViewById(R.id.editTextTextMultiLine)
            val sharedPreferences = viewModel.sharedPreferences
            val contented = sharedPreferences.getString(viewModel.list.name,"Not found")
            Log.d(ContentValues.TAG, note.content)
            text.setText(contented)
        }
    }

}