package com.example.mynotes.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.databinding.MainFragmentBinding
import com.example.mynotes.models.Noted

//layout: main_activity.xml
//binding class : MainActivityBinding
//layout: main_activity.xml
//binding class : MainFragmentBinding
class MainFragment() :
    Fragment(), ListSelectionRecycleViewAdapter.ListSelectionRecyclerViewClickListener {

    private lateinit var binding: MainFragmentBinding
    var clickListener: MainFragmentInteractionListener? = null
    var holdClickListener: MainFragmentInteractionListener? = null

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: Noted)
        fun listItemHold(list: Noted)
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
        binding.listsRecycleview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecycleViewAdapter(viewModel.lists, this,this)
        binding.listsRecycleview.adapter = recyclerViewAdapter
        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
        viewModel.onListRemoved ={
            recyclerViewAdapter.listsRemove(viewModel.whereRemoved)

        }
    }
    override fun listItemClicked(list: Noted) {
        clickListener?.listItemTapped(list)
    }

    override fun listItemHold(list: Noted) {
        holdClickListener?.listItemHold(list)
    }


}