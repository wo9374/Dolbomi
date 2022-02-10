package com.example.project;

import androidx.lifecycle.MutableLiveData;

public class TSLiveData<T> extends MutableLiveData<T> {
    public TSLiveData() {

    }

    public TSLiveData(T value) {
        setValue(value);
    }
}
