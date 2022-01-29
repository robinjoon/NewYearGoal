package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import dto.*;
import lombok.NonNull;
import security.AES256;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class GoalDao {
	private JdbcTemplate jdbcTemplate;
	
	public GoalDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public boolean insert(Goal goal) { // 목표 등록
		String sql = "insert into goals(email,name,goalText,knownText,due) values(?,?,?,?,?)";
		String pw = goal.getPw();
		while(pw.length()<16) {
			pw = pw + pw;
		}
		try {
			String knownText = AES256.encrypt(Goal.KNOWN, pw);
			String goalText = AES256.encrypt(goal.getGoalText(), pw);
			int result =  jdbcTemplate.update(sql,goal.getEmail(),goal.getName(),goalText,knownText,goal.getDue().name());
			if(result == 1) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Goal> selectGoals(GoalDue due){ // 목표기간이 특정 기간인 목표 조회
		List<Goal> result = new ArrayList<Goal>();
		String sql = "select * from goals where due = ?";
		try {
			result = jdbcTemplate.query(sql, new GoalRowMapper<Goal>(),due.name());
		}catch (DataAccessException e) {
			result = new ArrayList<Goal>();
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Goal> selectGoals(String email){ // 특정인의 목표 조회
		List<Goal> result = new ArrayList<Goal>();
		String sql = "select * from goals where email = ?";
		try {
			result = jdbcTemplate.query(sql, new GoalRowMapper<Goal>(),email);
		}catch (DataAccessException e) {
			result = new ArrayList<Goal>();
			e.printStackTrace();
		}
		return result;
	}
	
	public long selectGoalCount(int year) { // 특정 연도에 등록한 목표 개수 조회 
		String year_start = year+"-01-01";
		String year_end = year+"-12-31";
		long count = 0;
		String sql = "select count(email) from goals where date(createTime) between ? and ?";
		try {
			count = jdbcTemplate.query(sql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getLong(1);
				}
			},year_start,year_end).get(0);
		}catch (Exception e) {
			count = -1;
			e.printStackTrace();
		}
		return count;
	}
	
	public long selectAchieveGoalCount(int year) { // 특정 연도에 등록한 목표중 달성된 목표 개수 조회 
		String year_start = year+"-01-01";
		String year_end = year+"-12-31";
		long count = 0;
		String sql = "select count(email) from goals where (date(createTime) between ? and ?) and isAchieved = 1";
		try {
			count = jdbcTemplate.query(sql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getLong(1);
				}
			},year_start,year_end).get(0);
		}catch (Exception e) {
			count = -1;
			e.printStackTrace();
		}
		return count;
	}
	
	public boolean achieveGoal(Goal goal) { // 목표 달성
		String sql = "update goals set isAchieve = 1 where email = ? and createTime = ?";
		try {
			int result = jdbcTemplate.update(sql,goal.getEmail(),goal.getCreateTime());
			return result == 1 ? true : false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean canModify(String encryptedKnownText, String email, Timestamp createTime) { // 비밀번호 검증 로직.
		String sql = "select knownText from goals where email =? and createTime =?";
		String dbKnownText = jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}, email,createTime);
		return dbKnownText.equals(encryptedKnownText);
	}

	private class GoalRowMapper<T extends Goal> implements RowMapper<T> {
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			String email = rs.getString("email");
			String name = rs.getString("name");
			String goalText = rs.getString("goalText");
			GoalDue due = GoalDue.from(rs.getString("due"));
			Timestamp createTime = rs.getTimestamp("createTime");
			boolean isAchieved = rs.getBoolean("isAchieved");
			Goal goal = new Goal(email, name, goalText, goalText, due);
			goal.setCreateTime(createTime);
			goal.setAchieved(isAchieved);
			return (T) goal;
		}
	}
}

