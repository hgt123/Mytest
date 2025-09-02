#!/bin/bash

# Cursor 登录问题快速修复脚本

echo "======================================="
echo "Cursor 登录快速修复"
echo "======================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "这个脚本将尝试最常见的修复方法"
echo ""

# 1. 创建禁用 HTTP/2 的启动脚本
echo "1. 创建修复版启动脚本..."
cat > ~/cursor-login-fix << 'EOF'
#!/bin/bash
# Cursor 登录修复启动器

echo "启动 Cursor (禁用 HTTP/2 和沙盒模式)..."

# 设置环境变量
export ELECTRON_DISABLE_HTTP2=1

# 启动 Cursor
exec ~/.local/bin/cursor --disable-http2 --no-sandbox "$@"
EOF

chmod +x ~/cursor-login-fix
echo -e "${GREEN}✓ 启动脚本已创建${NC}"

# 2. 创建桌面快捷方式（修复版）
echo ""
echo "2. 创建修复版桌面快捷方式..."
cat > ~/.local/share/applications/cursor-fix.desktop << EOF
[Desktop Entry]
Name=Cursor (Fix)
Comment=Cursor with login fix
Exec=$HOME/cursor-login-fix %F
Terminal=false
Type=Application
Icon=cursor
Categories=Development;IDE;
StartupWMClass=Cursor
EOF

update-desktop-database ~/.local/share/applications 2>/dev/null || true
echo -e "${GREEN}✓ 桌面快捷方式已创建${NC}"

# 3. 同步系统时间
echo ""
echo "3. 尝试同步系统时间..."
if command -v timedatectl &> /dev/null; then
    sudo timedatectl set-ntp true 2>/dev/null && echo -e "${GREEN}✓ 时间同步已启用${NC}" || echo "需要管理员权限来同步时间"
else
    echo "timedatectl 不可用，跳过时间同步"
fi

# 4. 清理部分缓存
echo ""
echo "4. 清理登录相关缓存..."
rm -rf ~/.config/Cursor/Cache/Cache_Data/*auth* 2>/dev/null
rm -rf ~/.config/Cursor/Service\ Worker/CacheStorage/*cursor* 2>/dev/null
echo -e "${GREEN}✓ 缓存已清理${NC}"

# 5. 创建配置文件
echo ""
echo "5. 创建优化配置..."
mkdir -p ~/.config/Cursor
cat > ~/.config/Cursor/argv.json << 'EOF'
{
  "disable-http2": true,
  "disable-gpu-sandbox": true,
  "enable-logging": true,
  "v": 1
}
EOF
echo -e "${GREEN}✓ 配置文件已创建${NC}"

echo ""
echo "======================================="
echo -e "${GREEN}修复完成！${NC}"
echo "======================================="
echo ""
echo "请尝试以下方法启动 Cursor："
echo ""
echo "方法 1: 使用修复脚本"
echo -e "${YELLOW}~/cursor-login-fix${NC}"
echo ""
echo "方法 2: 从应用菜单"
echo "查找 'Cursor (Fix)'"
echo ""
echo "方法 3: 直接使用参数"
echo -e "${YELLOW}cursor --disable-http2 --no-sandbox${NC}"
echo ""
echo "如果仍有问题，请查看详细指南："
echo "cat cursor_login_fix_guide.md"
echo ""