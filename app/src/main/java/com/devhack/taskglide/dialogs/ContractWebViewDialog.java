package com.devhack.taskglide.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.devhack.taskglide.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class ContractWebViewDialog extends DialogFragment {

    private static final String LOG_TAG = ContractWebViewDialog.class.getSimpleName();

    private String loginUrl;
    private Unbinder unbinder;

    @BindView(R.id.dialog_contract_layout) WebView dialogWebView;

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialog_contract_view = inflater.inflate(R.layout.dialog_contract, container, false);
        unbinder = ButterKnife.bind(this, dialog_contract_view);

        // Sets the dialog background transparent.
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Encodes the redirect URL.
//        String uriRedirect = Uri.parse(someUrl)
//                .buildUpon()
//                .build().toString();
//
//        Uri loginUri = Uri.parse(someUrl.LOGIN_BASE_URL)
//                .buildUpon()
//                .appendPath("oauth")
//                .appendPath("authorize")
//                .appendQueryParameter("client_id", getString(R.string.someID))
//                .appendQueryParameter("scope", "all")
//                .appendQueryParameter("state", someUrl.state)
//                .appendQueryParameter("response_type", "token")
//                .appendQueryParameter("redirect_uri", uriRedirect)
//                .build();
//        loginUrl = loginUri.toString();

        initCookies();
        initWebView();

        return dialog_contract_view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /** WEBVIEW METHODS ________________________________________________________________________ **/

    private void initCookies() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieSyncManager.sync();
    }

    private void initWebView() {
        WebSettings settings = dialogWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        dialogWebView.setWebViewClient(new ContractWebClient());
        dialogWebView.loadUrl(loginUrl);
    }

    private void handleLoginResponse(String urlResponse) {
        dismiss();
    }

    private class ContractWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(LOG_TAG, "shouldOverrideUrlLoading(): New URL: " + url);
            handleLoginResponse(url);
            return false;
        }
    }
}