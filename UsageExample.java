package com.example.voiceassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 使用示例：展示如何在车机Android系统中使用语音助手卡片组件
 */
public class UsageExample extends Activity {
    
    private VoiceAssistantManager voiceAssistantManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 创建主容器
        FrameLayout mainContainer = new FrameLayout(this);
        setContentView(mainContainer);
        
        // 初始化语音助手管理器
        voiceAssistantManager = new VoiceAssistantManager(this, mainContainer);
        
        // 模拟云端大模型返回的数据
        simulateCloudResponse();
    }
    
    /**
     * 模拟云端大模型返回的卡片数据
     */
    private void simulateCloudResponse() {
        // 示例1：纯文本卡片，带打字机效果
        String markdown1 = "# 欢迎使用语音助手\n\n" +
                "我是您的**智能语音助手**，可以帮您：\n\n" +
                "- 查询天气信息\n" +
                "- 播放音乐\n" +
                "- 导航到目的地\n" +
                "- 控制车辆功能\n\n" +
                "请说出您的需求，我会尽力为您服务！";
        
        voiceAssistantManager.addCard(
                "语音助手", 
                markdown1, 
                null, 
                true  // 启用打字机效果
        );
        
        // 延迟添加第二个卡片
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 示例2：带图片的卡片
                String markdown2 = "## 今日天气\n\n" +
                        "**北京** 今天天气晴朗\n\n" +
                        "温度：`22°C - 28°C`\n" +
                        "湿度：65%\n" +
                        "风力：东南风 3级\n\n" +
                        "建议：适合出行，注意防晒";
                
                voiceAssistantManager.addCard(
                        "天气信息", 
                        markdown2, 
                        "https://example.com/weather_image.jpg", 
                        true
                );
            }
        }, 3000);
        
        // 延迟添加第三个卡片
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 示例3：代码示例卡片
                String markdown3 = "## 代码示例\n\n" +
                        "以下是一个简单的Android代码：\n\n" +
                        "```java\n" +
                        "public class HelloWorld {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Hello, World!\");\n" +
                        "    }\n" +
                        "}\n" +
                        "```\n\n" +
                        "这个程序会输出：*Hello, World!*";
                
                voiceAssistantManager.addCard(
                        "代码示例", 
                        markdown3, 
                        null, 
                        true
                );
            }
        }, 6000);
        
        // 延迟添加第四个卡片
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 示例4：链接和格式化文本
                String markdown4 = "## 相关链接\n\n" +
                        "您可以访问以下链接获取更多信息：\n\n" +
                        "- [Android开发者官网](https://developer.android.com)\n" +
                        "- [车载应用开发指南](https://developer.android.com/guide/topics/connectivity/cars)\n" +
                        "- [语音交互最佳实践](https://developer.android.com/guide/topics/ui/accessibility/voice-interaction)\n\n" +
                        "~~过时的信息~~ **重要提示**：请确保您的应用符合车载安全标准。";
                
                voiceAssistantManager.addCard(
                        "开发资源", 
                        markdown4, 
                        null, 
                        true
                );
            }
        }, 9000);
    }
    
    /**
     * 模拟实时更新卡片内容
     */
    private void simulateRealTimeUpdate() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新第一个卡片的内容
                String updatedContent = "# 语音助手状态更新\n\n" +
                        "系统已更新到**最新版本**\n\n" +
                        "新增功能：\n" +
                        "- 支持更多语音命令\n" +
                        "- 优化了响应速度\n" +
                        "- 改进了UI界面\n\n" +
                        "感谢您的使用！";
                
                voiceAssistantManager.updateCard(0, "语音助手", updatedContent, null);
            }
        }, 12000);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        if (voiceAssistantManager != null) {
            voiceAssistantManager.clearAllCards();
        }
    }
}