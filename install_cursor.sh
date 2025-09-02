#!/bin/bash

# Cursor 安装脚本 for Ubuntu/Linux
# 该脚本会自动下载并安装最新版本的 Cursor

set -e

echo "======================================="
echo "Cursor Editor 安装脚本"
echo "======================================="

# 创建临时目录
TEMP_DIR=$(mktemp -d)
cd "$TEMP_DIR"

# 下载最新版本的 Cursor AppImage
echo "正在下载 Cursor 最新版本..."
DOWNLOAD_URL="https://downloader.cursor.sh/linux/appImage/x64"
wget -O cursor.AppImage "$DOWNLOAD_URL" || {
    echo "错误: 下载失败，请检查网络连接"
    exit 1
}

# 赋予执行权限
chmod +x cursor.AppImage

# 创建应用程序目录（如果不存在）
mkdir -p "$HOME/.local/bin"
mkdir -p "$HOME/.local/share/applications"

# 移动 AppImage 到应用程序目录
echo "正在安装 Cursor..."
mv cursor.AppImage "$HOME/.local/bin/cursor"

# 创建桌面文件
echo "正在创建桌面快捷方式..."
cat > "$HOME/.local/share/applications/cursor.desktop" <<EOF
[Desktop Entry]
Name=Cursor
Comment=AI-powered code editor
Exec=$HOME/.local/bin/cursor %F
Terminal=false
Type=Application
Icon=cursor
Categories=Development;IDE;
StartupWMClass=Cursor
EOF

# 更新桌面数据库
update-desktop-database "$HOME/.local/share/applications" 2>/dev/null || true

# 清理临时目录
cd ..
rm -rf "$TEMP_DIR"

# 检查 PATH
if [[ ":$PATH:" != *":$HOME/.local/bin:"* ]]; then
    echo ""
    echo "提示: 请将以下行添加到您的 ~/.bashrc 或 ~/.zshrc 文件中："
    echo 'export PATH="$HOME/.local/bin:$PATH"'
    echo ""
    echo "然后运行: source ~/.bashrc (或 source ~/.zshrc)"
fi

echo ""
echo "======================================="
echo "✅ Cursor 安装完成！"
echo "======================================="
echo ""
echo "您可以通过以下方式启动 Cursor："
echo "1. 从应用程序菜单中查找 'Cursor'"
echo "2. 在终端中运行: cursor"
echo ""
echo "首次启动时，Cursor 会引导您完成初始设置。"
echo ""