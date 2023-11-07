package org.example.agentInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AgentInfoDao {
	private DataSource datasource;

	public Long save(AgentInfo agentInfo){
		String sql = " INSERT INTO AGENT_INFO(agent_id) VALUES(?) ";

		try(Connection connection = datasource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(sql);
		){
			pstmt.setLong(1, agentInfo.getAgentId());
			pstmt.executeUpdate();

			return pstmt.getGeneratedKeys().getLong(1);
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public List<AgentInfo> findAll(){
		List<AgentInfo> agnetInfoList = new LinkedList<>();
		String sql = " SELECT * FROM AGENT_INFO ";
		String q = String.format(sql);
		try(Connection connection = datasource.getConnection();
			Statement statement =	connection.createStatement();
			ResultSet rs = statement.executeQuery(q);
		){
			while (rs.next()){
				AgentInfo agentInfo = AgentInfo.builder()
					.agentInfoId(rs.getLong("agent_info_id"))
					.agentId(rs.getLong("agent_id"))
					.build();

				agnetInfoList.add(agentInfo);
			}
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}finally {
			return agnetInfoList;
		}
	}

	public AgentInfo findLastAgentInfo(){
		String sql = " SELECT MAX(agent_id) as agent_id FROM AGENT_INFO  ";
		String q = String.format(sql);
		AgentInfo agentInfo = null;
		try(Connection connection = datasource.getConnection();
			Statement statement =	connection.createStatement();
			ResultSet rs = statement.executeQuery(q);
		){
			while (rs.next()){
				agentInfo = AgentInfo.builder()
					.agentId(rs.getLong("agent_id"))
					.build();
			}
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
		return agentInfo;
	}

	public void deleteAgentInfo(){
		String sql = " DELETE FROM AGENT_INFO ";
		try(Connection connection = datasource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(sql);
		){
			pstmt.executeUpdate();
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
	}

}
