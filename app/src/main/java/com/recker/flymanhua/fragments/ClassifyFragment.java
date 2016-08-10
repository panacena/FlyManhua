package com.recker.flymanhua.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.recker.flymanhua.R;
import com.recker.flymanhua.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by recker on 16/7/9.
 *
 * 漫画分类
 */
public class ClassifyFragment extends BaseFragment {

    @Bind(R.id.tablayout) TabLayout mTabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;


    private List<Fragment> mFragments;
    private ClassifyContentFragment mHotFragment;//热血漫画
    private ClassifyContentFragment mInFragment;//国产漫画
    private String[] mTitles = {"热血漫画", "国产漫画"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected void init() {
        setupViewPager();
    }

    private void setupViewPager() {
        addFragments();

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void addFragments() {
        mFragments = new ArrayList<>();

        mHotFragment = new ClassifyContentFragment();
        mInFragment = new ClassifyContentFragment();

        mHotFragment.setType(4);
        mInFragment.setType(2);

        mFragments.add(mHotFragment);
        mFragments.add(mInFragment);
    }


}
