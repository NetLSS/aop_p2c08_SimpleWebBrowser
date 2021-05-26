package lilcode.aop.p2.c08.simplewebbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true // 자바 스크립트 사용 가능 하도록
        webView.loadUrl("http://www.google.com")
    }
}