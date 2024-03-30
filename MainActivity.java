import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.loadUrl("file:///android_asset/index.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Notify JavaScript that the page is fully loaded
                webView.loadUrl("javascript:onPageLoaded()");
            }
        });
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void sendEmail(String email, String message) {
            // Implement email sending functionality here
            // Example:
            // Toast.makeText(MainActivity.this, "Email sent to " + email + ": " + message, Toast.LENGTH_SHORT).show();
            System.out.println("Email sent to " + email + ": " + message);
        }

        @JavascriptInterface
        public void screenOffDetected() {
            // Implement screen off detection functionality here
            // Example:
            // Toast.makeText(MainActivity.this, "Screen off detected", Toast.LENGTH_SHORT).show();
            System.out.println("Screen off detected");
        }
    }
}
