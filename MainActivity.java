import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private BroadcastReceiver screenOffReceiver;
    private MediaPlayer mediaPlayer;

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

        // Register screen off receiver
        registerScreenOffReceiver();

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.warning_sound);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister screen off receiver
        unregisterScreenOffReceiver();
    }

    private void registerScreenOffReceiver() {
        screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Screen off detected, notify JavaScript
                webView.loadUrl("javascript:detectScreenOff()");
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, filter);
    }

    private void unregisterScreenOffReceiver() {
        if (screenOffReceiver != null) {
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
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
        public void warnUser() {
            // Play warning sound
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            // Implement any other warning mechanisms here
            // Example:
            // Toast.makeText(MainActivity.this, "Unauthorized access detected!", Toast.LENGTH_SHORT).show();
            System.out.println("Unauthorized access detected!");
        }
    }
}
