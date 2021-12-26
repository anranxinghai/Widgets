package com.pinguo.camera360.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class MainFragmentFactory(val mainFragmentRepository:MainFragmentRepository): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader,className)
        return when(fragmentClass){
            MainFragment::class.java->{
                MainFragment(/*mainFragmentRepository.constructorString*/)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}