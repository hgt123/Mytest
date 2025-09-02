# Cursor 在 Ubuntu 上的安装指南

## 方法一：使用自动安装脚本（推荐）

运行我们提供的安装脚本：

```bash
./install_cursor.sh
```

## 方法二：手动安装

### 1. 下载 Cursor

打开终端，运行以下命令下载最新版本：

```bash
wget -O cursor.AppImage https://downloader.cursor.sh/linux/appImage/x64
```

### 2. 赋予执行权限

```bash
chmod +x cursor.AppImage
```

### 3. 测试运行

```bash
./cursor.AppImage
```

### 4. 安装到系统（可选）

如果您想将 Cursor 安装到系统中，以便可以从任何地方启动：

```bash
# 创建目录
mkdir -p ~/.local/bin

# 移动文件
mv cursor.AppImage ~/.local/bin/cursor

# 创建桌面快捷方式
cat > ~/.local/share/applications/cursor.desktop <<EOF
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
update-desktop-database ~/.local/share/applications
```

### 5. 添加到 PATH（如果需要）

编辑 `~/.bashrc` 或 `~/.zshrc`，添加：

```bash
export PATH="$HOME/.local/bin:$PATH"
```

然后运行：

```bash
source ~/.bashrc  # 或 source ~/.zshrc
```

## 故障排除

### 问题 1：缺少依赖

如果 Cursor 无法启动，可能需要安装一些依赖：

```bash
sudo apt update
sudo apt install libgtk-3-0 libnotify4 libnss3 libxss1 libxtst6 xdg-utils libatspi2.0-0 libsecret-1-0
```

### 问题 2：沙盒问题

如果遇到沙盒相关的错误，可以尝试：

```bash
cursor --no-sandbox
```

### 问题 3：权限问题

确保 AppImage 有执行权限：

```bash
ls -l ~/.local/bin/cursor
# 应该看到 -rwxr-xr-x 权限
```

## 卸载 Cursor

如果需要卸载：

```bash
rm ~/.local/bin/cursor
rm ~/.local/share/applications/cursor.desktop
update-desktop-database ~/.local/share/applications
```

## 更新 Cursor

Cursor 通常会自动更新。如果需要手动更新，只需重新运行安装脚本或重新下载最新的 AppImage。

## 相关链接

- [Cursor 官网](https://cursor.com)
- [Cursor 下载页面](https://cursor.com/downloads)