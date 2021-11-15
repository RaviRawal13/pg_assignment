package com.ravirawal.pg_assignment.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(@LayoutRes layout: Int) : Fragment(layout) {
    protected lateinit var binding: VB
}
