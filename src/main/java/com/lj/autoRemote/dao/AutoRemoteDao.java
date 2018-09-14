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
        String sql = "insert into tb_server_deploy (ip,serverName,serverPath,command_start,state,seq) values (?,?,?,?,?,?) ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp(),serverInfoBean.getServerName(),serverInfoBean.getServerPath(),serverInfoBean.getCommandStart(),serverInfoBean.getState(),serverInfoBean.getSeq()});
    }

    /**
     * 更新备案表信息
     * @param serverInfoBean
     * @throws Exception
     */
    public void updateServerInfoById(ServerInfoBean serverInfoBean) throws Exception{
        String sql = "update tb_server_deploy set ip=?,serverName=?,serverPath=?,state=?,command_start=? where id=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp(),serverInfoBean.getServerName(),serverInfoBean.getServerPath(),serverInfoBean.getState(),serverInfoBean.getCommandStart(),serverInfoBean.getId()});
    }

    /**
     * 查询所有备案信息
     * @return
     * @throws Exception
     */
    public List<ServerInfoBean> queryServerInfoList()throws Exception{
        String sql = "select * from tb_server_deploy";
        return jdbcTemplateForSqlLite.query(sql,new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setServerName(rs.getString("serverName"));
                serverInfoBean.setServerPath(rs.getString("serverPath"));
                serverInfoBean.setState(rs.getString("state"));
                serverInfoBean.setSeq(rs.getInt("seq"));
                return serverInfoBean;
            }
        });
    }

    /**
     * 查询所有备案信息(按ID查询)
     * @return
     * @throws Exception
     */
    public ServerInfoBean queryServerInfoById(int id){
        String sql = "select * from tb_server_deploy where id=?";
        return jdbcTemplateForSqlLite.queryForObject(sql,new Object[]{id},new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setServerName(rs.getString("serverName"));
                serverInfoBean.setServerPath(rs.getString("serverPath"));
                serverInfoBean.setState(rs.getString("state"));
                serverInfoBean.setSeq(rs.getInt("seq"));
                return serverInfoBean;
            }
        });
    }

    /**
     * 查询各节点信息
     * @return
     * @throws Exception
     */
    public List<ServerInfoBean> queryNodeServerInfoList(){
        String sql = "select * from tb_server_node";
        return jdbcTemplateForSqlLite.query(sql,new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setState(rs.getString("state"));
                serverInfoBean.setStartTime(rs.getLong("startTime"));
                return serverInfoBean;
            }
        });
    }

    /**
     * 查询各节点信息(按ID查询)
     * @return
     * @throws Exception
     */
    public ServerInfoBean queryNodeServerInfoById(int id){
        String sql = "select * from tb_server_node where id=?";
        return jdbcTemplateForSqlLite.queryForObject(sql,new Object[]{id},new RowMapper<ServerInfoBean>(){
            @Override
            public ServerInfoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                ServerInfoBean serverInfoBean = new ServerInfoBean();
                serverInfoBean.setId(rs.getInt("id"));
                serverInfoBean.setIp(rs.getString("ip"));
                serverInfoBean.setState(rs.getString("state"));
                serverInfoBean.setStartTime(rs.getLong("startTime"));
                return serverInfoBean;
            }
        });
    }

    /**
     * 插入程序节点信息
     * @param serverInfoBean
     * @throws Exception
     */
    public void saveNodeServerInfo(ServerInfoBean serverInfoBean){
        String sql = "insert into tb_server_node (ip) values (?) ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp()});
    }

    /**
     * 更新程序节点信息
     * @param serverInfoBean
     * @throws Exception
     */
    public void updateNodeServerInfo(ServerInfoBean serverInfoBean){
        String sql = "update tb_server_node set ip=? where id=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{serverInfoBean.getIp(),serverInfoBean.getId()});
    }

    /**
     * 更新程序节点启动状态(根据ID)
     */
    public void updateNodeServerInfo(int id, int state) {
        String sql = "update tb_server_node set state=?,starttime=? where id=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{state+"",System.currentTimeMillis(),id});
    }
    /**
     * 更新程序节点启动状态(根据IP)
     */
    public void updateMyselfInfoByIp(String ip,int state) {
        String sql = "update tb_server_node set state=?,starttime=? where ip=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{state+"",System.currentTimeMillis(),ip});
    }

    /**
     * 删除节点(根据Id)
     */
    public void delMyselfInfoById(int id) {
        String sql = "delete from tb_server_node where id=? ";
        jdbcTemplateForSqlLite.update(sql,new Object[]{id});
    }

}
