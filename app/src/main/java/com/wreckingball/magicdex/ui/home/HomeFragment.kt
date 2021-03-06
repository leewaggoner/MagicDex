package com.wreckingball.magicdex.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wreckingball.magicdex.R
import com.wreckingball.magicdex.adapters.MenuAdapter
import com.wreckingball.magicdex.adapters.NewsAdapter
import com.wreckingball.magicdex.network.ERROR
import com.wreckingball.magicdex.network.LOADING
import com.wreckingball.magicdex.network.SUCCESS
import com.wreckingball.magicdex.utils.MagicUtil
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val model : HomeViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model.getNewsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return super.onCreateView(layoutInflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerViewNews = view.recyclerViewNews
        val recyclerViewMenu = view.recyclerViewMenu

        recyclerViewMenu.layoutManager = GridLayoutManager(context, 2)
        model.getMenuList(requireContext()).observe(viewLifecycleOwner, Observer { menuList ->
            recyclerViewMenu.adapter = MenuAdapter(menuList, view.context)
        })

        recyclerViewNews.layoutManager = GridLayoutManager(context, 1)
        recyclerViewNews.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        model.newsList.observe(viewLifecycleOwner, Observer { newsList ->
            when (newsList.status) {
                LOADING -> {
                    progressBarHome.visibility = View.VISIBLE
                }
                SUCCESS -> {
                    progressBarHome.visibility = View.INVISIBLE
                    recyclerViewNews.adapter = NewsAdapter(newsList)
                }
                ERROR -> {
                    progressBarHome.visibility = View.INVISIBLE
                    Snackbar.make(view, newsList.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        newsViewAll.setOnClickListener {
            model.viewAllNews(requireActivity())
        }

        imageViewSearch.setOnClickListener {
            val searchText = editTextSearch.text.toString()
            model.searchByName(requireView(), searchText)
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchText = editTextSearch.text.toString()
                    model.searchByName(requireView(), searchText)
                    true
                } else -> false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MagicUtil.closeKeyboard(requireContext(), requireView())
    }
}
