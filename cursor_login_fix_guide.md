# Cursor 登录问题解决指南

## 快速诊断

运行诊断脚本：
```bash
./fix_cursor_login.sh
```

## 常见问题和解决方案

### 1. HTTP/2 协议问题（最常见）

**症状**：登录时超时或无响应

**解决方法**：
1. 打开 Cursor
2. 按 `Ctrl + ,` 打开设置
3. 搜索 "HTTP/2"
4. 启用 "Disable HTTP/2" 选项
5. 重启 Cursor

**或者使用命令行参数启动**：
```bash
cursor --disable-http2
```

### 2. 代理设置问题

**症状**：在使用代理的网络环境下无法登录

**解决方法**：

#### 方法A：在 Cursor 中配置代理
1. 打开设置（`Ctrl + ,`）
2. 搜索 "Proxy"
3. 在 "Application > Proxy" 中输入代理地址
   - 格式：`http://127.0.0.1:端口号`
   - 注意：使用 `http://` 而不是 `https://`

#### 方法B：使用环境变量
```bash
export HTTP_PROXY=http://127.0.0.1:7890
export HTTPS_PROXY=http://127.0.0.1:7890
cursor
```

### 3. 网络防火墙问题

**症状**：连接被拒绝或超时

**解决方法**：
1. 确保以下域名未被防火墙阻止：
   - api.cursor.sh
   - *.cursor.sh
   - auth.cursor.sh

2. 检查端口：
   - 443 (HTTPS)
   - 80 (HTTP)

### 4. 系统时间不同步

**症状**：认证失败，提示时间相关错误

**解决方法**：
```bash
# 方法1：使用 timedatectl
sudo timedatectl set-ntp true

# 方法2：手动同步
sudo ntpdate -s time.nist.gov

# 方法3：安装并启用 ntp 服务
sudo apt install ntp
sudo systemctl enable ntp
sudo systemctl start ntp
```

### 5. DNS 解析问题

**症状**：无法解析 cursor.sh 域名

**解决方法**：
```bash
# 编辑 DNS 配置
sudo nano /etc/resolv.conf

# 添加以下内容
nameserver 8.8.8.8
nameserver 1.1.1.1
```

### 6. 缓存问题

**症状**：之前能登录，突然不能了

**解决方法**：
```bash
# 关闭 Cursor
pkill -f cursor

# 清理缓存
rm -rf ~/.config/Cursor/Cache/*
rm -rf ~/.config/Cursor/GPUCache/*
rm -rf ~/.config/Cursor/Service\ Worker/*

# 重启 Cursor
cursor
```

### 7. 权限问题

**症状**：提示权限相关错误

**解决方法**：
```bash
# 检查并修复权限
chmod -R 755 ~/.config/Cursor
chmod +x ~/.local/bin/cursor
```

## 高级故障排除

### 查看日志

```bash
# 实时查看主日志
tail -f ~/.config/Cursor/logs/main.log

# 查看渲染进程日志
tail -f ~/.config/Cursor/logs/renderer*.log

# 查看网络日志
tail -f ~/.config/Cursor/logs/network.log
```

### 使用调试模式启动

```bash
# 启用详细日志
cursor --verbose

# 禁用 GPU 加速（如果有显示问题）
cursor --disable-gpu

# 完整的调试启动
cursor --verbose --disable-http2 --no-sandbox --disable-gpu
```

### 重置 Cursor 设置

如果以上方法都无效，可以尝试重置：

```bash
# 备份当前设置
cp -r ~/.config/Cursor ~/.config/Cursor.backup

# 重置设置（谨慎使用）
rm -rf ~/.config/Cursor

# 重新启动 Cursor
cursor
```

## 临时解决方案

如果急需使用 Cursor，可以尝试：

1. **使用网页版**（如果有提供）
2. **使用其他网络**（如手机热点）
3. **禁用所有扩展**重新尝试登录

## 获取帮助

如果问题仍未解决：

1. 收集日志信息：
   ```bash
   # 创建日志包
   tar -czf cursor-logs.tar.gz ~/.config/Cursor/logs/
   ```

2. 记录系统信息：
   ```bash
   uname -a > system-info.txt
   cursor --version >> system-info.txt
   ```

3. 联系支持：
   - Cursor 官方论坛
   - GitHub Issues
   - 官方支持邮箱

## 预防措施

1. 定期更新 Cursor 到最新版本
2. 保持系统时间同步
3. 避免使用不稳定的代理
4. 定期清理缓存
5. 备份重要设置