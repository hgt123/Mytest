package com.example.voiceassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.style.URLSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简化的Markdown渲染器
 * 专门为车载语音助手优化，支持常用Markdown语法
 */
public class MarkdownRenderer {
    
    private Context context;
    private int textColor;
    private int linkColor;
    private int codeBackgroundColor;
    
    public MarkdownRenderer(Context context) {
        this.context = context;
        this.textColor = 0xFF333333;
        this.linkColor = 0xFF2196F3;
        this.codeBackgroundColor = 0xFFF5F5F5;
    }
    
    /**
     * 渲染Markdown文本为Spannable
     */
    public Spannable renderMarkdown(String markdown) {
        SpannableStringBuilder builder = new SpannableStringBuilder(markdown);
        
        // 处理各种Markdown语法
        processHeaders(builder);
        processBold(builder);
        processItalic(builder);
        processStrikethrough(builder);
        processUnderline(builder);
        processLinks(builder);
        processInlineCode(builder);
        processCodeBlocks(builder);
        processLists(builder);
        
        return builder;
    }
    
    /**
     * 处理标题 (# ## ###)
     */
    private void processHeaders(SpannableStringBuilder builder) {
        // H1
        Pattern h1Pattern = Pattern.compile("^# (.+)$", Pattern.MULTILINE);
        Matcher h1Matcher = h1Pattern.matcher(builder);
        while (h1Matcher.find()) {
            int start = h1Matcher.start();
            int end = h1Matcher.end();
            String text = h1Matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new RelativeSizeSpan(1.5f), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        // H2
        Pattern h2Pattern = Pattern.compile("^## (.+)$", Pattern.MULTILINE);
        Matcher h2Matcher = h2Pattern.matcher(builder);
        while (h2Matcher.find()) {
            int start = h2Matcher.start();
            int end = h2Matcher.end();
            String text = h2Matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new RelativeSizeSpan(1.3f), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        // H3
        Pattern h3Pattern = Pattern.compile("^### (.+)$", Pattern.MULTILINE);
        Matcher h3Matcher = h3Pattern.matcher(builder);
        while (h3Matcher.find()) {
            int start = h3Matcher.start();
            int end = h3Matcher.end();
            String text = h3Matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new RelativeSizeSpan(1.1f), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理粗体 (**text**)
     */
    private void processBold(SpannableStringBuilder builder) {
        Pattern boldPattern = Pattern.compile("\\*\\*(.+?)\\*\\*");
        Matcher matcher = boldPattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理斜体 (*text*)
     */
    private void processItalic(SpannableStringBuilder builder) {
        Pattern italicPattern = Pattern.compile("\\*(.+?)\\*");
        Matcher matcher = italicPattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new StyleSpan(Typeface.ITALIC), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理删除线 (~~text~~)
     */
    private void processStrikethrough(SpannableStringBuilder builder) {
        Pattern strikePattern = Pattern.compile("~~(.+?)~~");
        Matcher matcher = strikePattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new StrikethroughSpan(), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理下划线 (__text__)
     */
    private void processUnderline(SpannableStringBuilder builder) {
        Pattern underlinePattern = Pattern.compile("__(.+?)__");
        Matcher matcher = underlinePattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new UnderlineSpan(), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理链接 ([text](url))
     */
    private void processLinks(SpannableStringBuilder builder) {
        Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)");
        Matcher matcher = linkPattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            String url = matcher.group(2);
            
            builder.replace(start, end, text);
            builder.setSpan(new URLSpan(url), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(linkColor), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理行内代码 (`code`)
     */
    private void processInlineCode(SpannableStringBuilder builder) {
        Pattern codePattern = Pattern.compile("`([^`]+)`");
        Matcher matcher = codePattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, text);
            builder.setSpan(new BackgroundColorSpan(codeBackgroundColor), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.MONOSPACE), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理代码块 (```code```)
     */
    private void processCodeBlocks(SpannableStringBuilder builder) {
        Pattern codeBlockPattern = Pattern.compile("```([\\s\\S]*?)```");
        Matcher matcher = codeBlockPattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1).trim();
            
            builder.replace(start, end, text);
            builder.setSpan(new BackgroundColorSpan(codeBackgroundColor), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.MONOSPACE), start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 处理列表 (- item)
     */
    private void processLists(SpannableStringBuilder builder) {
        Pattern listPattern = Pattern.compile("^- (.+)$", Pattern.MULTILINE);
        Matcher matcher = listPattern.matcher(builder);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String text = matcher.group(1);
            
            builder.replace(start, end, "• " + text);
            builder.setSpan(new BulletSpan(20), start, start + text.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    
    /**
     * 设置文本颜色
     */
    public void setTextColor(int color) {
        this.textColor = color;
    }
    
    /**
     * 设置链接颜色
     */
    public void setLinkColor(int color) {
        this.linkColor = color;
    }
    
    /**
     * 设置代码背景颜色
     */
    public void setCodeBackgroundColor(int color) {
        this.codeBackgroundColor = color;
    }
}