# 远程服务管控

将该服务部署到多台服务器启动后，通过访问任意一台服务，可对其他所有部署了该节点的机器进行管控。

## 准备完善：
1. 本服务自动部署更新升级(远程更新部署自身、远程重启自身)（已经实现）
1. 自动上传程序包实现自动化部署运行（已实现服务自身程序包自动上传自动化部署运行，其他程序包自动部署暂未实现）
1. 指定需要监控的服务器日志关键词，定时监控并通知
1. 支持程序包上传及部署

---

### 数据库脚本
```
create table tb_server_deploy
(
  id            integer not null
    primary key
  autoincrement,
  ip            varchar not null,
  serverName    varchar not null,
  serverPath    varchar not null,
  state         varchar,
  command_start varchar,
  seq           integer
);

-- auto-generated definition
create table tb_server_node
(
  id        integer
    primary key
  autoincrement,
  ip        varchar,
  state     varchar,
  starttime integer(15)
);

create unique index tb_server_node_ip_uindex
  on tb_server_node (ip);

```
定期对数据库执行,释放Sqlite空间资源:
```
VACUUM
```