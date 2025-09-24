package com.example.voiceassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 语音助手动态卡片组件
 * 支持Markdown文本渲染、图片展示和打字机效果
 */
public class VoiceAssistantCard extends CardView {
    
    private TextView titleTextView;
    private TypewriterTextView contentTextView;
    private ImageView imageView;
    private LinearLayout cardContainer;
    
    private ExecutorService executorService;
    private Handler mainHandler;
    
    public VoiceAssistantCard(Context context) {
        super(context);
        init();
    }
    
    public VoiceAssistantCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public VoiceAssistantCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.voice_assistant_card, this, true);
        
        titleTextView = findViewById(R.id.title_text);
        contentTextView = findViewById(R.id.content_text);
        imageView = findViewById(R.id.card_image);
        cardContainer = findViewById(R.id.card_container);
        
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        
        // 设置卡片样式
        setCardElevation(8f);
        setRadius(16f);
        setCardBackgroundColor(getResources().getColor(android.R.color.white));
    }
    
    /**
     * 设置卡片内容
     * @param title 卡片标题
     * @param content Markdown格式的内容
     * @param imageUrl 图片URL（可选）
     * @param enableTypewriter 是否启用打字机效果
     */
    public void setCardContent(String title, String content, String imageUrl, boolean enableTypewriter) {
        // 设置标题
        if (title != null && !title.isEmpty()) {
            titleTextView.setText(title);
            titleTextView.setVisibility(VISIBLE);
        } else {
            titleTextView.setVisibility(GONE);
        }
        
        // 设置图片
        if (imageUrl != null && !imageUrl.isEmpty()) {
            loadImage(imageUrl);
            imageView.setVisibility(VISIBLE);
        } else {
            imageView.setVisibility(GONE);
        }
        
        // 设置内容
        if (enableTypewriter) {
            contentTextView.setTypewriterText(content);
        } else {
            contentTextView.setText(content);
        }
    }
    
    /**
     * 异步加载图片
     */
    private void loadImage(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imageView);
    }
    
    /**
     * 自定义打字机效果的TextView
     */
    public static class TypewriterTextView extends TextView {
        
        private CharSequence text;
        private int index;
        private long delay = 50; // 每个字符的延迟时间（毫秒）
        private Handler handler;
        private Runnable characterAdder;
        
        public TypewriterTextView(Context context) {
            super(context);
            init();
        }
        
        public TypewriterTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }
        
        private void init() {
            handler = new Handler(Looper.getMainLooper());
        }
        
        /**
         * 设置打字机效果的文本
         */
        public void setTypewriterText(CharSequence text) {
            this.text = text;
            this.index = 0;
            
            // 清空当前文本
            setText("");
            
            // 开始打字机效果
            startTypewriter();
        }
        
        private void startTypewriter() {
            if (index < text.length()) {
                // 添加下一个字符
                setText(text.subSequence(0, index + 1));
                index++;
                
                // 延迟后继续
                characterAdder = new Runnable() {
                    @Override
                    public void run() {
                        startTypewriter();
                    }
                };
                handler.postDelayed(characterAdder, delay);
            }
        }
        
        /**
         * 设置打字速度
         * @param delay 每个字符的延迟时间（毫秒）
         */
        public void setTypewriterDelay(long delay) {
            this.delay = delay;
        }
        
        /**
         * 停止打字机效果
         */
        public void stopTypewriter() {
            if (characterAdder != null) {
                handler.removeCallbacks(characterAdder);
            }
        }
        
        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            stopTypewriter();
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}