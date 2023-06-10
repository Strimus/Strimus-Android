package com.sps

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.machinarium.sbs.model.stream.StreamItem
import com.sps.databinding.FragmentPagerBinding

class PagerFragment : BaseFragment<FragmentPagerBinding>() {
    override fun getLayoutRes() = R.layout.fragment_pager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.pager.setPageTransformer(ZoomOutPageTransformer())
        binding.pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.pager.offscreenPageLimit = 1

        arguments?.getParcelable<TransferItem>(KEY_ITEM)?.let { tItem ->
            val pagerAdapter = ScreenSlidePagerAdapter(this, tItem.list)
            binding.pager.adapter = pagerAdapter

            binding.pager.post {
                binding.pager.setCurrentItem(tItem.list.indexOfFirst { it.id == tItem.id }, false)
            }
        }
    }


    private inner class ScreenSlidePagerAdapter(
        fragment: Fragment,
        val list: ArrayList<StreamItem>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = list.size

        override fun createFragment(position: Int): Fragment {
            val url = if (list[position].videos.isNullOrEmpty()) {
                list[position].url
            } else {
                list[position].videos?.get(0)?.url ?: ""
            }
            return LiveFragment.newInstance(url, position)
        }

    }

    companion object {
        const val KEY_ITEM = "keyItem"
    }
}