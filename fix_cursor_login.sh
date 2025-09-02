#!/bin/bash

# Cursor 登录问题诊断和修复脚本

echo "======================================="
echo "Cursor 登录问题诊断工具"
echo "======================================="
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. 检查网络连接
echo "1. 检查网络连接..."
if ping -c 1 google.com &> /dev/null; then
    echo -e "${GREEN}✓ 网络连接正常${NC}"
else
    echo -e "${RED}✗ 网络连接失败${NC}"
    echo "   请检查您的网络连接"
fi

# 2. 检查系统时间
echo ""
echo "2. 检查系统时间..."
SYSTEM_TIME=$(date +%s)
NTP_TIME=$(curl -s http://worldtimeapi.org/api/timezone/Etc/UTC | grep -o '"unixtime":[0-9]*' | cut -d: -f2)

if [ -n "$NTP_TIME" ]; then
    TIME_DIFF=$((SYSTEM_TIME - NTP_TIME))
    TIME_DIFF_ABS=${TIME_DIFF#-}
    
    if [ $TIME_DIFF_ABS -lt 300 ]; then
        echo -e "${GREEN}✓ 系统时间正确（误差: ${TIME_DIFF}秒）${NC}"
    else
        echo -e "${RED}✗ 系统时间不同步（误差: ${TIME_DIFF}秒）${NC}"
        echo "   建议同步系统时间："
        echo "   sudo ntpdate -s time.nist.gov"
        echo "   或"
        echo "   sudo timedatectl set-ntp true"
    fi
else
    echo -e "${YELLOW}⚠ 无法验证系统时间${NC}"
fi

# 3. 检查代理设置
echo ""
echo "3. 检查代理设置..."
if [ -n "$HTTP_PROXY" ] || [ -n "$HTTPS_PROXY" ] || [ -n "$http_proxy" ] || [ -n "$https_proxy" ]; then
    echo -e "${YELLOW}⚠ 检测到系统代理设置：${NC}"
    [ -n "$HTTP_PROXY" ] && echo "   HTTP_PROXY=$HTTP_PROXY"
    [ -n "$HTTPS_PROXY" ] && echo "   HTTPS_PROXY=$HTTPS_PROXY"
    [ -n "$http_proxy" ] && echo "   http_proxy=$http_proxy"
    [ -n "$https_proxy" ] && echo "   https_proxy=$https_proxy"
    echo "   如果使用代理，请在 Cursor 设置中配置相应的代理"
else
    echo -e "${GREEN}✓ 未检测到系统代理${NC}"
fi

# 4. 检查 DNS
echo ""
echo "4. 检查 DNS 解析..."
if nslookup api.cursor.sh &> /dev/null; then
    echo -e "${GREEN}✓ DNS 解析正常${NC}"
else
    echo -e "${RED}✗ DNS 解析失败${NC}"
    echo "   建议更换 DNS 服务器："
    echo "   编辑 /etc/resolv.conf 添加："
    echo "   nameserver 8.8.8.8"
    echo "   nameserver 1.1.1.1"
fi

# 5. 清理 Cursor 缓存
echo ""
echo "5. 清理 Cursor 缓存..."
echo -e "${YELLOW}是否要清理 Cursor 缓存？这可能会解决登录问题。(y/N)${NC}"
read -r CLEAN_CACHE

if [[ "$CLEAN_CACHE" =~ ^[Yy]$ ]]; then
    # 关闭 Cursor
    pkill -f cursor || true
    
    # 清理缓存目录
    rm -rf ~/.config/Cursor/Cache/* 2>/dev/null
    rm -rf ~/.config/Cursor/GPUCache/* 2>/dev/null
    rm -rf ~/.config/Cursor/Service\ Worker/* 2>/dev/null
    
    echo -e "${GREEN}✓ 缓存已清理${NC}"
else
    echo "   跳过缓存清理"
fi

# 6. 创建修复配置
echo ""
echo "6. 创建修复配置..."
echo -e "${YELLOW}是否要创建 Cursor 启动脚本（禁用 HTTP/2 和沙盒）？(y/N)${NC}"
read -r CREATE_SCRIPT

if [[ "$CREATE_SCRIPT" =~ ^[Yy]$ ]]; then
    cat > ~/cursor-fix.sh << 'EOF'
#!/bin/bash
# Cursor 修复启动脚本

# 禁用 HTTP/2 和沙盒模式启动 Cursor
~/.local/bin/cursor --disable-http2 --no-sandbox "$@"
EOF
    
    chmod +x ~/cursor-fix.sh
    echo -e "${GREEN}✓ 修复脚本已创建: ~/cursor-fix.sh${NC}"
    echo "   您可以使用此脚本启动 Cursor"
fi

# 7. 提供解决方案总结
echo ""
echo "======================================="
echo "建议的解决步骤："
echo "======================================="
echo ""
echo "1. 在 Cursor 中禁用 HTTP/2："
echo "   - 打开 Cursor"
echo "   - 按 Ctrl+, 打开设置"
echo "   - 搜索 'HTTP/2'"
echo "   - 启用 'Disable HTTP/2' 选项"
echo ""
echo "2. 如果使用代理："
echo "   - 在设置中搜索 'Proxy'"
echo "   - 输入代理地址 (例如: http://127.0.0.1:7890)"
echo ""
echo "3. 尝试以下命令启动 Cursor："
echo "   cursor --disable-http2 --no-sandbox"
echo ""
echo "4. 如果仍有问题，查看日志："
echo "   tail -f ~/.config/Cursor/logs/main.log"
echo ""
echo "======================================="