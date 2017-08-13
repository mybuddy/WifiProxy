package com.felixyan.wifiproxy.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher实现类，用于简化EditText的输入监听，不用实现所有方法
 *
 * Created by yanfei on 2017/08/13.
 */

public class TextAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
