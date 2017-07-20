package com.felixyan.wifiproxy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.util.ViewUtil;
import com.felixyan.wifiproxy.WifiItemData;
import com.felixyan.wifiproxy.WifiItemView;

import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiRecyclerViewAdapter extends BaseRecyclerViewAdapter<WifiItemData> {
    private static final int VIEW_TYPE_GAP = 0;
    private static final int VIEW_TYPE_HEADER_SAVED_WIFI = 1;
    private static final int VIEW_TYPE_HEADER_NEARBY_WIFI = 2;
    private static final int VIEW_TYPE_SAVED_DATA = 3;
    private static final int VIEW_TYPE_NEARBY_DATA = 4;

    private List<WifiItemData> mSavedWifiList;
    private List<WifiItemData> mNearbyWifiList;

    public WifiRecyclerViewAdapter(Context context, List<WifiItemData> savedList, List<WifiItemData> nearbyList) {
        super(context, null);
        mSavedWifiList = savedList;
        mNearbyWifiList = nearbyList;
    }

    private int getSavedWifiCount() {
        return mSavedWifiList != null ? mSavedWifiList.size() : 0;
    }

    private int getNearbyWifiCount() {
        return mNearbyWifiList != null ? mNearbyWifiList.size() : 0;
    }

    @Override
    public int getItemCount() {
        int savedCount = getSavedWifiCount();
        int nearbyWifiCount = getNearbyWifiCount();

        // 附近没有WIFI
        if(savedCount + nearbyWifiCount == 0) {
            return 0;
        }
        // 没有已保存的WIFI，则显示附近的WIFI + “选取附近的WIFI”的header + 1个分隔线
        if(savedCount == 0) {
            return nearbyWifiCount + 2;
        }
        // 显示已保存的WIFI + 附近的WIFI + 2个header + 2个分隔线
        return savedCount + nearbyWifiCount + 4;
    }

    @Override
    public WifiItemData getItem(int position) {
        int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPE_SAVED_DATA || viewType == VIEW_TYPE_NEARBY_DATA) {
            int dataPosition = getDataPosition(position);
            if(viewType == VIEW_TYPE_SAVED_DATA) {
                return mSavedWifiList.get(dataPosition);
            } else {
                return mNearbyWifiList.get(dataPosition);
            }
        }

        return null;
    }

    /**
     * 获取列表中某item绑定的数据在对应数据列表中的位置
     *
     * @param position
     * @return
     */
    @Override
    public int getDataPosition(int position) {
        int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPE_SAVED_DATA) {
            return position - 2;
        }
        if(viewType == VIEW_TYPE_NEARBY_DATA) {
            int savedCount = getSavedWifiCount();
            if(savedCount != 0) {
                return position - savedCount - 4;
            }
            return position - 2;
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        int savedCount = getSavedWifiCount();
        // 附近的WIFI + 1个header + 1个分隔线
        if(savedCount == 0) {
            if(position == 0) {
                return VIEW_TYPE_GAP;
            } else if (position == 1) {
                return VIEW_TYPE_HEADER_NEARBY_WIFI;
            }
            return VIEW_TYPE_NEARBY_DATA;
        }

        // 已保存的WIFI + 附近的WIFI + 2个header + 2个分隔线
        if(position == 0) {
            return VIEW_TYPE_GAP;
        } else if (position == 1) {
            return VIEW_TYPE_HEADER_SAVED_WIFI;
        } else if (position == savedCount + 2) {
            return VIEW_TYPE_GAP;
        } else if (position == savedCount + 3) {
            return VIEW_TYPE_HEADER_NEARBY_WIFI;
        } else if (position < savedCount + 2) {
            return VIEW_TYPE_SAVED_DATA;
        }
        return VIEW_TYPE_NEARBY_DATA;
    }

    @Override
    IViewWrapper newViewWrapper(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_GAP:
                View view = new View(getContext());
                view.setMinimumHeight(ViewUtil.dp2px(10));
                return new ViewWrapper(view);
            case VIEW_TYPE_HEADER_SAVED_WIFI:
                View view1 = getLayoutInflater().inflate(R.layout.layout_wifi_list_item_header, parent, false);
                TextView header1 = (TextView) view1.findViewById(R.id.tvHeader);
                header1.setText(getContext().getString(R.string.list_connected));
                return new ViewWrapper(view1);
            case VIEW_TYPE_HEADER_NEARBY_WIFI:
                View view2 = getLayoutInflater().inflate(R.layout.layout_wifi_list_item_header, parent, false);
                TextView header2 = (TextView) view2.findViewById(R.id.tvHeader);
                header2.setText(getContext().getString(R.string.list_available_networks));
                return new ViewWrapper(view2);
            case VIEW_TYPE_SAVED_DATA:
            case VIEW_TYPE_NEARBY_DATA:
                return new WifiItemView(getContext());
        }
        return null;
    }
}
