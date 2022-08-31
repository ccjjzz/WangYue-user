package com.jiuyue.user.ui.technician;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.R;
import com.jiuyue.user.adapter.DynamicProfileAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.loading.LoadingInterface;
import com.jiuyue.user.databinding.ActivityTechnicianProfileBinding;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.TechnicianDynamicEntity;
import com.jiuyue.user.entity.TechnicianEntity;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.mvp.contract.TechnicianContract;
import com.jiuyue.user.mvp.model.DynamicModel;
import com.jiuyue.user.mvp.presenter.TechnicianPresenter;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;
import com.jiuyue.user.utils.FastClickHelper;
import com.jiuyue.user.utils.ScrollHelper;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.utils.glide.GlideLoader;
import com.jiuyue.user.widget.XRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.List;

import io.reactivex.disposables.Disposable;


/**
 * 技师简介
 */
public class TechnicianProfileActivity extends BaseActivity<TechnicianPresenter, ActivityTechnicianProfileBinding> implements TechnicianContract.IView {
    private XRefreshLayout refreshLayout;
    private RecyclerView rvDynamic;
    private DynamicProfileAdapter mAdapter;
    private int pageCount = 1;
    private boolean isRefresh = true;

    private int techId;

    @Override
    protected ActivityTechnicianProfileBinding getViewBinding() {
        return ActivityTechnicianProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected TechnicianPresenter createPresenter() {
        return new TechnicianPresenter(this);
    }

    @Override
    public View getLoadingTargetView() {
        return binding.srlProfile;
    }

    @Override
    public LoadingInterface.OnClickListener initLoadingControllerRetryListener() {
        return () -> {
            requestData(true);
        };
    }

    @Override
    public void initStatusBar() {
        UltimateBarX.statusBarOnly(this)
                .fitWindow(false)
                .colorRes(R.color.transparent)
                .light(false)
                .lvlColorRes(R.color.white)
                .apply();
    }

    @Override
    protected void init() {
        if (getIntent().getExtras() == null) {
            ToastUtil.show("参数错误");
            finish();
            return;
        }
        techId = getIntent().getIntExtra(IntentKey.TECH_ID, -1);
        //设置title滑动渐变
        ScrollHelper.INSTANCE.setTitleBarChange(
                "技师简介",
                this,
                binding.appBarLayout,
                binding.toolbar,
                binding.ivProfileBack,
                binding.tvProfileTitle
        );

        binding.ivProfileBack.setOnClickListener(v -> {
            finish();
        });

        //初始化刷新控件
        refreshLayout = binding.srlProfile;
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData(true);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(false);
            }
        });

        //初始化rv
        rvDynamic = binding.rvProfile;
        rvDynamic.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DynamicProfileAdapter(this);
        rvDynamic.setAdapter(mAdapter);
        rvDynamic.setNestedScrollingEnabled(true);
        rvDynamic.setHasFixedSize(true);
        mAdapter.addChildClickViewIds(R.id.tv_item_dynamic_collect, R.id.tv_item_dynamic_like);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_item_dynamic_collect) {
                //关注
                TextView tvCollect = view.findViewById(view.getId());
                DynamicEntity data = mAdapter.getData().get(position);
                if (tvCollect.isSelected()) {
                    data.setIsCollect(0);
                    data.setCollectNum(data.getCollectNum() - 1);
                    mAdapter.notifyItemChanged(position);
                    collectDynamic(data.getTechId(), data.getId(), 1);
                } else {
                    data.setIsCollect(1);
                    data.setCollectNum(data.getCollectNum() + 1);
                    mAdapter.notifyItemChanged(position);
                    collectDynamic(data.getTechId(), data.getId(), 0);
                }
            } else if (view.getId() == R.id.tv_item_dynamic_like) {
                //点赞
                TextView tvLike = view.findViewById(view.getId());
                DynamicEntity data = mAdapter.getData().get(position);
                if (tvLike.isSelected()) {
                    data.setIsLike(0);
                    data.setLikeNum(data.getLikeNum() - 1);
                    mAdapter.notifyItemChanged(position);
                    likeDynamic(data.getTechId(), data.getId(), 1);
                } else {
                    data.setIsLike(1);
                    data.setLikeNum(data.getLikeNum() + 1);
                    mAdapter.notifyItemChanged(position);
                    likeDynamic(data.getTechId(), data.getId(), 0);
                }
            }
        });

        //拉取数据
        mPresenter.technicianInfo(techId);
        showLoading();
        requestData(true);
    }

    /**
     * 请求数据
     *
     * @param refresh 是否刷新
     */
    private void requestData(boolean refresh) {
        if (refresh) {
            pageCount = 1;
            isRefresh = true;
        } else {
            pageCount++;
            isRefresh = false;
        }
        mPresenter.technicianDynamicList(techId, pageCount);
    }

    @Override
    public void onTechnicianListSuccess(ListBean<TechnicianEntity> data) {
    }

    @Override
    public void onTechnicianListError(String msg, int code) {
    }

    @Override
    public void onTechnicianInfoSuccess(TechnicianEntity data) {
        //设置技师信息
        binding.tvProfileName.setText(data.getCertName());
        GlideLoader.display(data.getAvator(), binding.ivProfileHead, R.drawable.default_user_icon);
        binding.tvProfileMark.setText(data.getScore() + ".0");
        binding.tvProfileFans.setText(String.valueOf(data.getFansNum()));
        binding.tvProfileLike.setText(String.valueOf(data.getLikeNum()));
        //0=未关注 1=已关注
        if (data.getFollowStatus() == 0) {
            binding.tvProfileFollow.setText("关注");
            binding.tvProfileFollow.setSelected(false);
        } else {
            binding.tvProfileFollow.setText("已关注");
            binding.tvProfileFollow.setSelected(true);
        }
        binding.tvProfileFollow.setOnClickListener(v -> {
            if (!FastClickHelper.isFastClick()) {
                if (binding.tvProfileFollow.isSelected()) { //取消关注
                    binding.tvProfileFollow.setText("关注");
                    binding.tvProfileFollow.setSelected(false);
                    //粉丝-1
                    binding.tvProfileFans.setText(String.valueOf(data.getFansNum() - 1));
                    mPresenter.followTechnician(techId, 1);
                } else {
                    binding.tvProfileFollow.setText("已关注");
                    binding.tvProfileFollow.setSelected(true);
                    //粉丝+1
                    binding.tvProfileFans.setText(String.valueOf(data.getFansNum() + 1));
                    mPresenter.followTechnician(techId, 0);
                }
            }
        });
    }

    @Override
    public void onTechnicianInfoError(String msg, int code) {
        ToastUtil.show(msg);
        showError(msg, code);
    }

    @Override
    public void onFollowTechnicianSuccess(Object data) {
        if (binding.tvProfileFollow.isSelected()) {
            ToastUtil.show("已关注");
            LiveEventBus.get(EventKey.UPDATE_FOLLOW_STATUS, Boolean.class).post(true);
        } else {
            ToastUtil.show("取消关注");
            LiveEventBus.get(EventKey.UPDATE_FOLLOW_STATUS, Boolean.class).post(false);
        }
    }

    @Override
    public void onFollowTechnicianError(String msg, int code) {
        ToastUtil.show(msg);
    }

    @Override
    public void onTechnicianDynamicListSuccess(TechnicianDynamicEntity.ListDTO data) {
        hideLoading();
        //动态列表
        List<DynamicEntity> dataBeans = data.getList();
        if (dataBeans.size() > 0) {
            if (isRefresh) {
                mAdapter.setList(dataBeans);
                refreshLayout.finishRefresh();
            } else {
                mAdapter.addData(dataBeans);
                refreshLayout.finishLoadMore();
            }
        } else {
            if (pageCount == 1) {
                refreshLayout.finishRefreshWithNoMoreData();
                refreshLayout.finishLoadMoreWithNoMoreData();
                mAdapter.setList(null);
                showEmpty();
            } else {
                if (isRefresh) {
                    refreshLayout.finishRefreshWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        }
    }

    @Override
    public void onTechnicianDynamicListError(String msg, int code) {
        hideLoading();
        ToastUtil.show(msg);
    }

    /**
     * 点赞/取消点赞动态
     *
     * @param techId    技师id
     * @param dynamicId 动态id
     * @param type      0=点赞 1=取消
     */
    private void likeDynamic(int techId, int dynamicId, int type) {
        new DynamicModel().likeDynamic(techId, dynamicId, type, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                if (type == 0) {
                    ToastUtil.show("已点赞");
                } else {
                    ToastUtil.show("取消点赞");
                }
            }

            @Override
            public void onError(String msg, int code) {
                ToastUtil.show(msg);
            }

            @Override
            public void complete() {
            }

            @Override
            public void onSubscribe(Disposable d) {
            }
        });
    }

    /**
     * 收藏/取消收藏动态
     *
     * @param techId    技师id
     * @param dynamicId 动态id
     * @param type      0=收藏 1=取消
     */
    private void collectDynamic(int techId, int dynamicId, int type) {
        new DynamicModel().collectDynamic(techId, dynamicId, type, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                if (type == 0) {
                    ToastUtil.show("已收藏");
                } else {
                    ToastUtil.show("取消收藏");
                }
            }

            @Override
            public void onError(String msg, int code) {
                ToastUtil.show(msg);
            }

            @Override
            public void complete() {
            }

            @Override
            public void onSubscribe(Disposable d) {
            }
        });
    }
}
