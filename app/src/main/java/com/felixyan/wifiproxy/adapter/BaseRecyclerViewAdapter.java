package com.felixyan.wifiproxy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yanfei on 2017/6/2.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    public static final int CHOICE_MODE_NONE = -1;
    public static final int CHOICE_MODE_SINGLE = 0;
    public static final int CHOICE_MODE_MULTI = 1;
    private Context mContext;
    private List<T> mDataList;
    private int mChoiceMode = CHOICE_MODE_NONE;
    private int mLastSelectPosition = -1; // 单选模式下（CHOICE_MODE_SINGLE）记录上一次选中的位置
    private int[] mSelectedPositions; // 多选模式下，记录所有选中的位置
    private OnListItemClickListener<T> mOnListItemClickListener;
    private LayoutInflater mLayoutInflater;

    public BaseRecyclerViewAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    abstract IViewWrapper newViewWrapper(ViewGroup parent, int viewType);

    Context getContext() {
        return mContext;
    }

    LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public void setChoiceMode(int choiceMode) {
        mChoiceMode = choiceMode;
    }

    public void setSelectedPosition(int... positions) {
        if(mChoiceMode == CHOICE_MODE_NONE) {
            return;
        }

        if(positions == null || positions.length == 0) {
            if(mChoiceMode == CHOICE_MODE_SINGLE) {
                mLastSelectPosition = -1;
            } else if (mChoiceMode == CHOICE_MODE_MULTI) {
                mSelectedPositions = null;
            }
        } else {
            if(mChoiceMode == CHOICE_MODE_SINGLE) {
                mLastSelectPosition = positions[positions.length - 1];
            } else if (mChoiceMode == CHOICE_MODE_MULTI) {
                mSelectedPositions = positions;
            }
        }
        notifyDataSetChanged();
    }

    public void setOnListItemClickListener(OnListItemClickListener<T> onListItemClickListener) {
        mOnListItemClickListener = onListItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        IViewWrapper viewWrapper = newViewWrapper(parent, viewType);

        final ViewHolder viewHolder = new ViewHolder(viewWrapper);
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();

                if(mChoiceMode == CHOICE_MODE_SINGLE) {
                    // 当前View之前未处于选中状态
                    if(position != mLastSelectPosition) {
                        // 更新当前View为选中状态
                        v.setSelected(true);

                        // 更新上一次选中的位置
                        int lastSelectPosition = mLastSelectPosition;
                        mLastSelectPosition = position;

                        // 更新上一次选中的View，修改其选中状态
                        if(lastSelectPosition != -1) {
                            notifyItemChanged(lastSelectPosition);
                        }
                    }
                } else if (mChoiceMode == CHOICE_MODE_MULTI) {
                    // 修改当前View的状态
                    v.setSelected(!(v.isSelected()));
                }

                if (mOnListItemClickListener != null) {
                    mOnListItemClickListener.onItemClick(v, viewType, position, getItem(position));
                }
            }
        });

        return viewHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T data = getItem(position);

        if(data != null) {
            IViewWrapper viewWrapper = ((ViewHolder) holder).getViewWrapper();
            viewWrapper.setData(getDataPosition(position), data);
            if(mChoiceMode == CHOICE_MODE_SINGLE) {
                viewWrapper.getView().setSelected(mLastSelectPosition == position);
            } else if(mChoiceMode == CHOICE_MODE_MULTI){
                if(mSelectedPositions != null && mSelectedPositions.length != 0) {
                    boolean selected = false;
                    for(int pos : mSelectedPositions) {
                        if(pos == position) {
                            selected = true;
                            break;
                        }
                    }
                    viewWrapper.getView().setSelected(selected);
                } else {
                    viewWrapper.getView().setSelected(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public T getItem(int position) {
        return mDataList != null ? mDataList.get(position) : null;
    }

    /**
     * 获取列表中某item绑定的数据在对应数据列表中的位置
     *
     * @param position
     * @return
     */
    public int getDataPosition(int position) {
        return position;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private IViewWrapper mViewWrapper;

        ViewHolder(IViewWrapper viewWrapper) {
            super(viewWrapper.getView());
            mViewWrapper = viewWrapper;
        }

        IViewWrapper getViewWrapper() {
            return mViewWrapper;
        }

        void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
