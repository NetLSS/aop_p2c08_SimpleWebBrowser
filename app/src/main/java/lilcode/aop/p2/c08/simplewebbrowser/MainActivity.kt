package lilcode.aop.p2.c08.simplewebbrowser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/*
https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar?hl=en
로딩창 인데 나타났다 사라질 때 최소한의 시간동안 유지되고 사라짐
 */
class MainActivity : AppCompatActivity() {

    private val goHomeButton: ImageButton by lazy {
        findViewById(R.id.goHomeButton)
    }

    private val goBackButton: ImageButton by lazy {
        findViewById(R.id.goBackButton)
    }

    private val goForwardButton: ImageButton by lazy {
        findViewById(R.id.goForwardButton)
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val addressBar: EditText by lazy {
        findViewById(R.id.addressBar)
    }

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    // 폰에서 뒤로가기 버튼 눌렀을 때
    override fun onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled") // 보안 관련 경고 무시
    private fun initViews() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true // 자바 스크립트 사용 가능 하도록
            loadUrl(HOME_URL)
        }
    }

    private fun bindViews() {
        // 주소 창
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = v.text.toString()

                if (URLUtil.isNetworkUrl(loadingUrl)){
                    webView.loadUrl(loadingUrl)
                }else{
                    webView.loadUrl("http://$loadingUrl")
                }
            }

            return@setOnEditorActionListener false // 키보드 닫기까지 동작 하도록
        }

        // 뒤로가기 버튼
        goBackButton.setOnClickListener {
            webView.goBack() // 뒤로 가기
        }

        // 앞으로 가기 버튼
        goForwardButton.setOnClickListener {
            webView.goForward()
        }

        // 홈 버튼
        goHomeButton.setOnClickListener {
            webView.loadUrl(HOME_URL)
        }

        // 리프레시 레이아웃
        refreshLayout.setOnRefreshListener {
            webView.reload() // 새로 고침
        }
    }

    // inner 를 붙여서 상위 클래스 접근 가능하도록 설정
    inner class WebViewClient: android.webkit.WebViewClient(){

        // 페이지가 다 로딩 되었을때
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false

            progressBar.hide()

            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()
            addressBar.setText(url)
        }


        // 시작 시
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }

    }

    inner class WebChromeClient: android.webkit.WebChromeClient(){

        // 페이지 로딩 정도 0~100
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress
        }
    }

    companion object {
        private const val HOME_URL = "http://www.google.com"
    }
}