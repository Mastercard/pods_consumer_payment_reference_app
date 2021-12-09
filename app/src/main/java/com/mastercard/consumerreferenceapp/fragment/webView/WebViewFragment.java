package com.mastercard.consumerreferenceapp.fragment.webView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mastercard.consumerreferenceapp.BuildConfig;
import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.databinding.FragmentWebViewBinding;
import com.mastercard.consumerreferenceapp.model.WebViewData;

import java.util.HashMap;

import timber.log.Timber;

public class WebViewFragment extends BottomSheetDialogFragment {
    private FragmentWebViewBinding binding;
    // Make sure javascript interface name is Android
    private String javascriptInterfaceName = "Android";
    private MutableLiveData<Boolean> isDialogShown;
    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        });
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = Double.valueOf(windowHeight * 0.8).intValue();
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setPeekHeight(layoutParams.height);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
        behavior.setHideable(false);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWebViewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());
        WebViewData webViewData = WebViewFragmentArgs.fromBundle(getArguments()).getWebViewData();
        isDialogShown = webViewData.getIsLightBoxShown();
        setup(webViewData);
        setupWebView(webViewData);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isDialogShown.postValue(true);
    }

    private void setup(WebViewData webViewData) {
        binding.imgClose .setOnClickListener(v -> dismiss());
        binding.lblTitle.setText(webViewData.getWebViewType().getValue());
        binding.otpTextLayout.setVisibility(View.GONE);
    }

    private void setupWebView(WebViewData webViewData) {
        binding.webView.clearCache(false);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        // Please set this javascript interface name to "Android" to receive the status call back from our payment web view
        binding.webView.addJavascriptInterface(new WebAppInterfaceImpl(this), javascriptInterfaceName);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Timber.i("Request Url: " + request.getUrl().toString());
                Timber.i("Header: " + request.getRequestHeaders());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.loadingProgressBar.setVisibility(View.GONE);
            }
        });
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Timber.i("ConsoleMessage: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.loadingProgressBar.setProgress(newProgress);
            }
        });
        WebSettings webSettings = binding.webView.getSettings();
        String userAgent = String.format("%s [%s/%s]", webSettings.getUserAgentString(), BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        webSettings.setUserAgentString(userAgent);
        WebView.setWebContentsDebuggingEnabled(true);
        setUpCookies(webViewData);
        binding.webView.loadUrl(webViewData.getUrl());
        binding.backButton.setOnClickListener(v -> {
            if(binding.webView.canGoBack()) {
                binding.webView.goBack();
            }
        });
    }

    private void setUpCookies(WebViewData webViewData) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        /* String cookie = "jwt=" + webViewData.getAccessToken();
        cookieManager.setCookie(removeQueryParams(webViewData.getUrl()), cookie, isSuccess -> {
            if (!isSuccess) {
                Timber.e("Unable to set cookie for %s!", webViewData.getUrl());
            }
        }); */
        String cookie1 = "accessToken=" + webViewData.getAccessToken();
        cookieManager.setCookie(removeQueryParams(webViewData.getUrl()), cookie1, isSuccess -> {
            if (!isSuccess) {
                Timber.e("Unable to set accessToken cookie for %s!", webViewData.getUrl());
            }
        });
        String cookie2 = "tokenType=" + webViewData.getTokenType();
        cookieManager.setCookie(removeQueryParams(webViewData.getUrl()), cookie2, isSuccess -> {
            if (!isSuccess) {
                Timber.e("Unable to set tokenType cookie for %s!", webViewData.getUrl());
            }
        });
        String cookie3 = "expiresIn=" + webViewData.getExpiresIn();
        cookieManager.setCookie(removeQueryParams(webViewData.getUrl()), cookie3, isSuccess -> {
            if (!isSuccess) {
                Timber.e("Unable to set expiresIn cookie for %s!", webViewData.getUrl());
            }
        });
    }

    private String removeQueryParams(String url) {
        if (url.contains("?")) {
            return url.split("\\?")[0];
        }
        return url;
    }

    @Override
    public void onDestroyView() {
        CookieManager.getInstance().removeAllCookies(null);
        isDialogShown.postValue(false);
        super.onDestroyView();
    }
}
