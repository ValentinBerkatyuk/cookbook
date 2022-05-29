package com.example.cookbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.cookbook.R
import com.example.cookbook.models.Result
import com.example.cookbook.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_instructions.view.*


class InstructionsFragment : Fragment(R.layout.fragment_instructions) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        view.instruction_webview.webViewClient = object : WebViewClient() {}
        val website: String = myBundle!!.sourceUrl
        view.instruction_webview.loadUrl(website)
    }

}