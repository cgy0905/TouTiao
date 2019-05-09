package com.cgy.news.module.home.channel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgy.news.R;
import com.cgy.news.constants.Constant;
import com.cgy.news.listener.ItemDragHelperCallback;
import com.cgy.news.listener.OnChannelDragListener;
import com.cgy.news.listener.OnChannelListener;
import com.cgy.news.model.entity.Channel;
import com.cgy.news.module.home.channel.adapter.ChannelAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.socks.library.KLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.cgy.news.model.entity.Channel.TYPE_MY_CHANNEL;
import static com.cgy.news.model.entity.Channel.TYPE_OTHER;
import static com.cgy.news.model.entity.Channel.TYPE_OTHER_CHANNEL;

/**
 * @author cgy
 * @description
 * @date 2019/5/9 13:50
 */
public class ChannelDialogFragment extends DialogFragment implements OnChannelDragListener {

    private List<Channel> mDatas = new ArrayList<>();
    private ChannelAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Unbinder unbinder;
    private ItemTouchHelper mHelper;

    private OnChannelListener mOnChannelListener;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        mOnChannelListener = onChannelListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //添加动画
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        return inflater.inflate(R.layout.fragment_channel, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        processLogic();
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas) {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA_SELECTED, (Serializable) selectedDatas);
        bundle.putSerializable(Constant.DATA_UNSELECTED, (Serializable)unselectedDatas);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    private void setDataType(List<Channel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemType(type);

        }
    }

    private void processLogic() {
        mDatas.add(new Channel(Channel.TYPE_MY, "我的频道", ""));
        Bundle bundle = new Bundle();
        List<Channel> selectedDatas = (List<Channel>) bundle.getSerializable(Constant.DATA_SELECTED);
        List<Channel> unselectedDatas = (List<Channel>) bundle.getSerializable(Constant.DATA_UNSELECTED);
        setDataType(selectedDatas, TYPE_MY_CHANNEL);
        setDataType(unselectedDatas, TYPE_OTHER_CHANNEL);

        mDatas.addAll(selectedDatas);
        mDatas.add(new Channel(TYPE_OTHER, "频道推荐", ""));
        mDatas.addAll(unselectedDatas);

        mAdapter = new ChannelAdapter(mDatas);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == TYPE_MY_CHANNEL || itemViewType == TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
        ItemDragHelperCallback callback = new ItemDragHelperCallback(this);
        mHelper = new ItemTouchHelper(callback);
        mAdapter.setOnChannelDragListener(this);
        //attachRecyclerView
        mHelper.attachToRecyclerView(mRecyclerView);
    }

    @OnClick(R.id.icon_collapse)
    public void onClick(View view) {
        dismiss();
    }

    private DialogInterface.OnDismissListener mOnDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onStartDrag(BaseViewHolder baseViewHolder) {
        //开始拖动
        KLog.i("开始拖动");
        mHelper.startDrag(baseViewHolder);
    }

    @Override
    public void onItemMove(int startPos, int endPos) {
        //我的频道之间移动
        if (mOnChannelListener != null)
            mOnChannelListener.onItemMove(startPos - 1, endPos -1);//去除标题所占的一个index
        onMove(startPos, endPos);
    }

    private void onMove(int startPos, int endPos) {
        Channel startChannel = mDatas.get(startPos);
        //先删除之前的位置
        mDatas.remove(startPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(startPos, endPos);
    }

    @Override
    public void onMoveToMyChannel(int startPos, int endPos) {
        //移动到我的频道
        onMove(startPos, endPos);

        if (mOnChannelListener != null) {
            mOnChannelListener.onMoveToMyChannel(startPos - 1 - mAdapter.getMyChannelSize(), endPos - 1);
        }
    }

    @Override
    public void onMoveToOtherChannel(int startPos, int endPos) {
        //移动到推荐频道
        onMove(startPos, endPos);
        if (mOnChannelListener != null)
            mOnChannelListener.onMoveToOtherChannel(startPos - 1, endPos - 2 - mAdapter.getMyChannelSize());
    }
}
