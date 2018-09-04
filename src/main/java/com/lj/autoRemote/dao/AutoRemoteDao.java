package com.lj.autoRemote.dao;

import com.lj.autoRemote.beans.ServerInfoBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据存储层
 */
@Repository
public class AutoRemoteDao {

    private Logger logger = Logger.getLogger(AutoRemoteDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplateForSqlLite;

    /**
     * 插入备案表信息
     * @param serverInfoBean
     * @throws Exception
     */
    public void saveServerInfo(ServerInfoBean serverInfoBean) throws Exception{
        String sql = "insert into tb_server (ip,serverName,serverPath) values (?,?,?) ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp(),serverInfoBean.getServerName(),serverInfoBean.getServerPath()});
    }

    /**
     * 更新备案表信息
     * @param serverInfoBean
     * @throws Exception
     */
    public void updateServerInfoById(ServerInfoBean serverInfoBean) throws Exception{
        String sql = "update tb_server set ip=?,serverName=?,serverPath=? where id=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp(),serverInfoBean.getServerName(),serverInfoBean.getServerPath(),serverInfoBean.getId()});
    }

    /**
     * 查询所有备案信息
     * @return
     * @throws Exception
     */
    public List<ServerInfoBean> queryServerInfoList()throws Exception{
        String sql = "select * from tb_server";
        return jdbcTemplateForSqlLite.query(sql,new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setServerName(rs.getString("serverName"));
                serverInfoBean.setServerPath(rs.getString("serverPath"));
                return serverInfoBean;
            }
        });
    }

    /**
     * 查询本程序部署列表
     * @return
     * @throws Exception
     */
    public List<ServerInfoBean> queryMyselfList(){
        String sql = "select * from tb_server where serverName='web-autoremote'";
        return jdbcTemplateForSqlLite.query(sql,new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setServerName(rs.getString("serverName"));
                serverInfoBean.setServerPath(rs.getString("serverPath"));
                return serverInfoBean;
            }
        });
    }

}
