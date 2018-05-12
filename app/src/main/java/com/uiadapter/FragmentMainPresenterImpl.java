package com.uiadapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.duanqu.Idea.fragment.DisplayFragment;
import com.duanqu.Idea.fragment.FriendsFragment;
import com.duanqu.Idea.fragment.MessageFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Me on 2017/8/5.
 */

public class FragmentMainPresenterImpl implements FragmentMainPresenter {
    private FragmentMainIView iView;

    public FragmentMainPresenterImpl(FragmentMainIView iView) {
        this.iView = iView;
    }

    @Override
    public void DoInitTabLayout(String[] titles) {
        iView.OnInitTabLayout(titles);
    }

    @Override
    public void DoInitFragmentLayout() {
        List<Fragment> fragments = new ArrayList<>();
        //这里初始化需要载入的view
        Fragment fragmentMovie = new DisplayFragment();
        Fragment fragmentType = new MessageFragment();
        Fragment fragmentLove = new FriendsFragment();
        fragments.add(fragmentMovie);
        fragments.add(fragmentType);
        fragments.add(fragmentLove);
        iView.OnInitFragmentLayout(fragments);
    }

    @Override
    public void DoBindTab2ViewPager(final ViewPager viewPager, final TabLayout tabLayout) {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
