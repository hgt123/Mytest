package com.example.voiceassistant;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.os.Handler;
import android.os.Looper;

/**
 * Lynx框架混合方案
 * 结合Lynx的跨平台能力和Android原生组件的优势
 */
public class LynxHybridSolution {
    
    private Context context;
    private WebView lynxWebView;
    private VoiceAssistantManager nativeManager;
    private Handler mainHandler;
    
    public LynxHybridSolution(Context context, ViewGroup parentContainer) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
        
        // 初始化Lynx WebView
        initLynxWebView(parentContainer);
        
        // 初始化原生管理器作为备用方案
        initNativeManager(parentContainer);
    }
    
    /**
     * 初始化Lynx WebView
     */
    private void initLynxWebView(ViewGroup parentContainer) {
        lynxWebView = new WebView(context);
        lynxWebView.getSettings().setJavaScriptEnabled(true);
        lynxWebView.getSettings().setDomStorageEnabled(true);
        lynxWebView.getSettings().setAllowFileAccess(true);
        lynxWebView.getSettings().setAllowContentAccess(true);
        
        // 设置WebView客户端
        lynxWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        
        lynxWebView.setWebChromeClient(new WebChromeClient());
        
        // 添加JavaScript接口
        lynxWebView.addJavascriptInterface(new LynxJSInterface(), "Android");
        
        // 加载Lynx框架
        loadLynxFramework();
        
        parentContainer.addView(lynxWebView);
    }
    
    /**
     * 初始化原生管理器
     */
    private void initNativeManager(ViewGroup parentContainer) {
        nativeManager = new VoiceAssistantManager(context, parentContainer);
    }
    
    /**
     * 加载Lynx框架
     */
    private void loadLynxFramework() {
        String lynxHTML = generateLynxHTML();
        lynxWebView.loadDataWithBaseURL("file:///android_asset/", lynxHTML, "text/html", "UTF-8", null);
    }
    
    /**
     * 生成Lynx HTML模板
     */
    private String generateLynxHTML() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Voice Assistant UI</title>
                <script src="https://unpkg.com/marked/marked.min.js"></script>
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                        margin: 0;
                        padding: 16px;
                        background-color: #f5f5f5;
                    }
                    
                    .card {
                        background: white;
                        border-radius: 12px;
                        padding: 16px;
                        margin-bottom: 16px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    }
                    
                    .card-title {
                        font-size: 18px;
                        font-weight: bold;
                        color: #333;
                        margin-bottom: 12px;
                    }
                    
                    .card-content {
                        font-size: 16px;
                        line-height: 1.6;
                        color: #666;
                    }
                    
                    .card-image {
                        width: 100%;
                        height: 200px;
                        object-fit: cover;
                        border-radius: 8px;
                        margin-bottom: 12px;
                    }
                    
                    .typewriter {
                        overflow: hidden;
                        border-right: 2px solid #333;
                        white-space: nowrap;
                        animation: typing 3s steps(40, end), blink-caret 0.75s step-end infinite;
                    }
                    
                    @keyframes typing {
                        from { width: 0; }
                        to { width: 100%; }
                    }
                    
                    @keyframes blink-caret {
                        from, to { border-color: transparent; }
                        50% { border-color: #333; }
                    }
                    
                    .markdown-content h1, .markdown-content h2, .markdown-content h3 {
                        color: #333;
                        margin-top: 0;
                    }
                    
                    .markdown-content strong {
                        font-weight: bold;
                    }
                    
                    .markdown-content em {
                        font-style: italic;
                    }
                    
                    .markdown-content code {
                        background-color: #f5f5f5;
                        padding: 2px 4px;
                        border-radius: 4px;
                        font-family: monospace;
                    }
                    
                    .markdown-content pre {
                        background-color: #f5f5f5;
                        padding: 12px;
                        border-radius: 8px;
                        overflow-x: auto;
                    }
                    
                    .markdown-content ul {
                        padding-left: 20px;
                    }
                    
                    .markdown-content li {
                        margin-bottom: 4px;
                    }
                </style>
            </head>
            <body>
                <div id="card-container"></div>
                
                <script>
                    class VoiceAssistantUI {
                        constructor() {
                            this.cardContainer = document.getElementById('card-container');
                            this.typewriterSpeed = 50; // 每个字符的延迟时间（毫秒）
                        }
                        
                        // 添加卡片
                        addCard(title, markdownContent, imageUrl, enableTypewriter) {
                            const card = document.createElement('div');
                            card.className = 'card';
                            
                            // 添加标题
                            if (title) {
                                const titleElement = document.createElement('div');
                                titleElement.className = 'card-title';
                                titleElement.textContent = title;
                                card.appendChild(titleElement);
                            }
                            
                            // 添加图片
                            if (imageUrl) {
                                const imageElement = document.createElement('img');
                                imageElement.className = 'card-image';
                                imageElement.src = imageUrl;
                                imageElement.alt = title || 'Card Image';
                                card.appendChild(imageElement);
                            }
                            
                            // 添加内容
                            const contentElement = document.createElement('div');
                            contentElement.className = 'card-content markdown-content';
                            
                            if (enableTypewriter) {
                                this.renderMarkdownWithTypewriter(contentElement, markdownContent);
                            } else {
                                contentElement.innerHTML = marked.parse(markdownContent);
                            }
                            
                            card.appendChild(contentElement);
                            this.cardContainer.appendChild(card);
                            
                            // 滚动到底部
                            this.scrollToBottom();
                            
                            return card;
                        }
                        
                        // 渲染Markdown并应用打字机效果
                        renderMarkdownWithTypewriter(element, markdownContent) {
                            const htmlContent = marked.parse(markdownContent);
                            const tempDiv = document.createElement('div');
                            tempDiv.innerHTML = htmlContent;
                            
                            const textContent = tempDiv.textContent || tempDiv.innerText || '';
                            element.innerHTML = '';
                            
                            this.typewriterEffect(element, textContent, 0);
                        }
                        
                        // 打字机效果实现
                        typewriterEffect(element, text, index) {
                            if (index < text.length) {
                                element.textContent = text.substring(0, index + 1);
                                setTimeout(() => {
                                    this.typewriterEffect(element, text, index + 1);
                                }, this.typewriterSpeed);
                            }
                        }
                        
                        // 滚动到底部
                        scrollToBottom() {
                            window.scrollTo(0, document.body.scrollHeight);
                        }
                        
                        // 清空所有卡片
                        clearAllCards() {
                            this.cardContainer.innerHTML = '';
                        }
                        
                        // 设置打字机速度
                        setTypewriterSpeed(speed) {
                            this.typewriterSpeed = speed;
                        }
                    }
                    
                    // 初始化UI
                    const voiceUI = new VoiceAssistantUI();
                    
                    // 暴露给Android的接口
                    window.VoiceAssistantUI = voiceUI;
                </script>
            </body>
            </html>
            """;
    }
    
    /**
     * JavaScript接口类
     */
    private class LynxJSInterface {
        @JavascriptInterface
        public void addCard(String title, String markdownContent, String imageUrl, boolean enableTypewriter) {
            mainHandler.post(() -> {
                String jsCode = String.format(
                    "window.VoiceAssistantUI.addCard('%s', '%s', '%s', %s);",
                    escapeJavaScript(title),
                    escapeJavaScript(markdownContent),
                    escapeJavaScript(imageUrl),
                    enableTypewriter
                );
                lynxWebView.evaluateJavascript(jsCode, null);
            });
        }
        
        @JavascriptInterface
        public void clearAllCards() {
            mainHandler.post(() -> {
                lynxWebView.evaluateJavascript("window.VoiceAssistantUI.clearAllCards();", null);
            });
        }
        
        @JavascriptInterface
        public void setTypewriterSpeed(int speed) {
            mainHandler.post(() -> {
                String jsCode = String.format("window.VoiceAssistantUI.setTypewriterSpeed(%d);", speed);
                lynxWebView.evaluateJavascript(jsCode, null);
            });
        }
        
        private String escapeJavaScript(String str) {
            if (str == null) return "";
            return str.replace("\\", "\\\\")
                    .replace("'", "\\'")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
        }
    }
    
    /**
     * 添加卡片（优先使用Lynx，失败时使用原生方案）
     */
    public void addCard(String title, String markdownContent, String imageUrl, boolean enableTypewriter) {
        try {
            // 尝试使用Lynx方案
            if (lynxWebView != null) {
                LynxJSInterface jsInterface = new LynxJSInterface();
                jsInterface.addCard(title, markdownContent, imageUrl, enableTypewriter);
            } else {
                // 回退到原生方案
                nativeManager.addCard(title, markdownContent, imageUrl, enableTypewriter);
            }
        } catch (Exception e) {
            // 异常时使用原生方案
            e.printStackTrace();
            nativeManager.addCard(title, markdownContent, imageUrl, enableTypewriter);
        }
    }
    
    /**
     * 清空所有卡片
     */
    public void clearAllCards() {
        try {
            if (lynxWebView != null) {
                LynxJSInterface jsInterface = new LynxJSInterface();
                jsInterface.clearAllCards();
            } else {
                nativeManager.clearAllCards();
            }
        } catch (Exception e) {
            e.printStackTrace();
            nativeManager.clearAllCards();
        }
    }
    
    /**
     * 设置打字机速度
     */
    public void setTypewriterSpeed(int speed) {
        try {
            if (lynxWebView != null) {
                LynxJSInterface jsInterface = new LynxJSInterface();
                jsInterface.setTypewriterSpeed(speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 销毁资源
     */
    public void destroy() {
        if (lynxWebView != null) {
            lynxWebView.destroy();
        }
        if (nativeManager != null) {
            nativeManager.clearAllCards();
        }
    }
}