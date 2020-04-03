package com.hacks.societyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authentication {
    @SerializedName("success")
    @Expose
    private String mSuccessMsg;

    @SerializedName("error")
    @Expose
    private String mErrorCode;

    public String getSuccessMsg() {
        return mSuccessMsg;
    }

    public void setSuccessMsg(String mSuccessMsg) {
        this.mSuccessMsg = mSuccessMsg;
    }

    public String getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
    }
}
