package nl.plaatsoft.kerkingouda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Arrays;

public class MainActivity extends Activity {
    public String mainUrl = "https://www.kerkingouda.nl/";
    public String[] preUrls = {mainUrl};
    public WebView webView;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(preUrls.length != 1){
                webView.loadUrl(preUrls[preUrls.length-2]);
                preUrls = Arrays.copyOf(preUrls, preUrls.length-1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(mainUrl);

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String a = url.substring(url.length() - 1);

                if(!a.equals("/")){
                    url = url + "/";
                }

                if (url.startsWith(mainUrl)) {
                    if(!url.equals(preUrls[preUrls.length - 1])) {
                        int n = preUrls.length;
                        String[] newPreUrls = new String[n + 1];

                        int i;
                        for (i = 0; i < n; i++) {
                            newPreUrls[i] = preUrls[i];
                        }

                        newPreUrls[n] = url;
                        preUrls = newPreUrls;
                    }
                    return false;

                }else if (url.startsWith("tel:")) {
                    Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(tel);
                    return true;

                } else if (url.contains("mailto:")) {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("/", ""))));
                    return true;

                } else if (url.endsWith(".pdf/")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url.replace(".pdf/", ".pdf")), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    return true;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }
        });
    }
}