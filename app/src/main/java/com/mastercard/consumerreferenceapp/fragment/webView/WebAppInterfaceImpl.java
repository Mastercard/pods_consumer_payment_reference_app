package com.mastercard.consumerreferenceapp.fragment.webView;

import android.media.MediaPlayer;
import android.webkit.JavascriptInterface;

import androidx.navigation.fragment.NavHostFragment;

import com.mastercard.consumerreferenceapp.R;
import com.mastercard.consumerreferenceapp.model.PaymentStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebAppInterfaceImpl implements WebAppInterface {
    private final WebViewFragment fragment;

    /**
     * This method will be called by our payment web view to pass the following 2 parameters
     * when user finish his/her payment.
     * @param sessionId
     * @param isSuccessful
     */
    @JavascriptInterface
    public void dismissWebView(String sessionId, String isSuccessful) {
        if(isSuccessful != null) {
            if(Boolean.valueOf(isSuccessful)) {
                MediaPlayer mPlayer = MediaPlayer.create(fragment.getContext(), R.raw.mastercard_transaction_sound_1_5s);
                mPlayer.start();
            }
            NavHostFragment.findNavController(fragment).navigate(WebViewFragmentDirections.actionWebViewFragmentToConfirmationFragment().setPaymentSuccess(Boolean.valueOf(isSuccessful)));
        } else {
            fragment.dismiss();
        }
    }
}
