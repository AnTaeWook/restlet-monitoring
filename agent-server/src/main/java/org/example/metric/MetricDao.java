package org.example.metric;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.example.dto.MetricXmlRpcDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MetricDao {

	private DataSource datasource;

	public Long save(Metric metric){
		String sql = " INSERT INTO METRIC(cpu_rate, total_memory, used_memory, free_memory, inbound_traffic, outbound_traffic, created_at) VALUES(?, ?, ?, ?, ?, ?, ? ) ";

		try(Connection connection = datasource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(sql);
		){
			pstmt.setFloat(1, metric.getCpuRate());
			pstmt.setFloat(2, metric.getTotalMemory());
			pstmt.setFloat(3, metric.getUsedMemory());
			pstmt.setFloat(4, metric.getFreeMemory());
			pstmt.setFloat(5, metric.getInboundTraffic());
			pstmt.setFloat(6, metric.getOutboundTraffic());
			pstmt.setString(7, localDateTimeToString(metric.getCreatedAt()));
			pstmt.executeUpdate();

			return pstmt.getGeneratedKeys().getLong(1);
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public List<MetricXmlRpcDto> findAll(){
		List<MetricXmlRpcDto> metricsList = new LinkedList<>();
		String sql = " SELECT * FROM METRIC ";
		String q = String.format(sql);
		try(Connection connection = datasource.getConnection();
			Statement statement =	connection.createStatement();
			ResultSet rs = statement.executeQuery(q);
		){
			while (rs.next()){
				MetricXmlRpcDto metrics = MetricXmlRpcDto.of(rs.getFloat("total_memory"), rs.getFloat("free_memory"), rs.getFloat("used_memory")
					, rs.getFloat("cpu_rate"), rs.getFloat("inbound_traffic"), rs.getFloat("outbound_traffic"),
					LocalDateTime.parse(rs.getString("created_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
				metricsList.add(metrics);
			}
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}finally {
			return metricsList;
		}
	}

	public void deleteBeforeData(LocalDateTime localDateTime){
		String sql = " DELETE FROM METRIC WHERE created_at <= ? ";
		try(Connection connection = datasource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(sql);
		){
			pstmt.setString(1, localDateTimeToString(localDateTime));
			pstmt.executeUpdate();
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
	}

	public void deleteAll(){
		String sql = " DELETE FROM METRIC ";
		try(Connection connection = datasource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(sql);
		){
			pstmt.executeUpdate();
		}catch (SQLException e){
			log.error(e.getMessage(), e);
		}
	}

	private LocalDateTime stringToLocalDateTime(String created_at){
		return LocalDateTime.parse(created_at, getDateTimeFormatter());
	}

	private String localDateTimeToString(LocalDateTime localDateTime){
	 return	localDateTime.format(getDateTimeFormatter());
	}

	private DateTimeFormatter getDateTimeFormatter(){
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	}

}
