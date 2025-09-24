package com.example.voiceassistant;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 语音助手卡片管理器
 * 负责管理多个动态卡片的展示和更新
 */
public class VoiceAssistantManager {
    
    private Context context;
    private LinearLayout cardContainer;
    private ScrollView scrollView;
    private List<VoiceAssistantCard> cards;
    private MarkdownRenderer markdownRenderer;
    
    public VoiceAssistantManager(Context context, ViewGroup parentContainer) {
        this.context = context;
        this.cards = new ArrayList<>();
        this.markdownRenderer = new MarkdownRenderer(context);
        
        setupUI(parentContainer);
    }
    
    private void setupUI(ViewGroup parentContainer) {
        // 创建滚动容器
        scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        
        // 创建卡片容器
        cardContainer = new LinearLayout(context);
        cardContainer.setOrientation(LinearLayout.VERTICAL);
        cardContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        
        // 设置内边距
        int padding = (int) (16 * context.getResources().getDisplayMetrics().density);
        cardContainer.setPadding(padding, padding, padding, padding);
        
        scrollView.addView(cardContainer);
        parentContainer.addView(scrollView);
    }
    
    /**
     * 添加新的语音助手卡片
     */
    public VoiceAssistantCard addCard(String title, String markdownContent, String imageUrl, boolean enableTypewriter) {
        VoiceAssistantCard card = new VoiceAssistantCard(context);
        
        // 渲染Markdown内容
        if (markdownContent != null && !markdownContent.isEmpty()) {
            // 这里可以集成Markwon库进行更完整的Markdown渲染
            // 目前使用简化的渲染器
            card.setCardContent(title, markdownContent, imageUrl, enableTypewriter);
        } else {
            card.setCardContent(title, "", imageUrl, enableTypewriter);
        }
        
        // 设置卡片布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, (int) (12 * context.getResources().getDisplayMetrics().density));
        
        card.setLayoutParams(params);
        cardContainer.addView(card);
        cards.add(card);
        
        // 滚动到底部显示新卡片
        scrollToBottom();
        
        return card;
    }
    
    /**
     * 更新指定卡片的内容
     */
    public void updateCard(int index, String title, String markdownContent, String imageUrl) {
        if (index >= 0 && index < cards.size()) {
            VoiceAssistantCard card = cards.get(index);
            card.setCardContent(title, markdownContent, imageUrl, false);
        }
    }
    
    /**
     * 移除指定卡片
     */
    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            VoiceAssistantCard card = cards.get(index);
            cardContainer.removeView(card);
            cards.remove(index);
        }
    }
    
    /**
     * 清空所有卡片
     */
    public void clearAllCards() {
        cardContainer.removeAllViews();
        cards.clear();
    }
    
    /**
     * 获取卡片数量
     */
    public int getCardCount() {
        return cards.size();
    }
    
    /**
     * 获取指定位置的卡片
     */
    public VoiceAssistantCard getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }
    
    /**
     * 滚动到底部
     */
    private void scrollToBottom() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    
    /**
     * 设置卡片间距
     */
    public void setCardSpacing(int spacingDp) {
        int spacingPx = (int) (spacingDp * context.getResources().getDisplayMetrics().density);
        
        for (int i = 0; i < cards.size(); i++) {
            VoiceAssistantCard card = cards.get(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) card.getLayoutParams();
            params.setMargins(0, 0, 0, spacingPx);
            card.setLayoutParams(params);
        }
    }
    
    /**
     * 设置Markdown渲染器样式
     */
    public void setMarkdownStyle(int textColor, int linkColor, int codeBackgroundColor) {
        markdownRenderer.setTextColor(textColor);
        markdownRenderer.setLinkColor(linkColor);
        markdownRenderer.setCodeBackgroundColor(codeBackgroundColor);
    }
}