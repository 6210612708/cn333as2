package com.example.mynotes.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.R
import com.example.mynotes.databinding.MainFragmentBinding
import com.example.mynotes.models.TaskList
import org.intellij.lang.annotations.JdkConstants

//layout: main_activity.xml
//binding class : MainActivityBinding
//layout: main_activity.xml
//binding class : MainFragmentBinding
class MainFragment() :
    Fragment(), ListSelectionRecycleViewAdapter.ListSelectionRecycleViewClickListener {

    private lateinit var binding: MainFragmentBinding
    lateinit var clickListener: MainFragmentInteractionListener

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
    }


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        //id: lists_recycleview => listsRecycleview
        binding.listsRecycleview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val recycleViewAdapter = ListSelectionRecycleViewAdapter(viewModel.lists, this)
        binding.listsRecycleview.adapter = recycleViewAdapter
        viewModel.onListAdded = {
            recycleViewAdapter.listsUpdated()
        }
    }

    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }

}