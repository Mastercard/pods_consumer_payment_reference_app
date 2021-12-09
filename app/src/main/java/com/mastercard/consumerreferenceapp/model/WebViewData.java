package com.mastercard.consumerreferenceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class WebViewData implements Parcelable {
    private final WebViewType webViewType;
    private final String url;
    private final String accessToken;
    private MutableLiveData<Boolean> isLightBoxShown;
    private final String tokenType;
    private final String expiresIn;

    protected WebViewData(Parcel in) {
        webViewType = WebViewType.valueOf(in.readString());
        url = in.readString();
        accessToken = in.readString();
        tokenType = in.readString();
        expiresIn = in.readString();
    }

    public static final Creator<WebViewData> CREATOR = new Creator<WebViewData>() {
        @Override
        public WebViewData createFromParcel(Parcel in) {
            return new WebViewData(in);
        }

        @Override
        public WebViewData[] newArray(int size) {
            return new WebViewData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(accessToken);
        dest.writeString(webViewType.getValue());
        dest.writeString(tokenType);
        dest.writeString(expiresIn);

    }
}
