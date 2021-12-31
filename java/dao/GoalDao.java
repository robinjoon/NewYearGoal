package dao;

import javax.sql.DataSource;
import dto.*;
import security.AES256;

import org.springframework.jdbc.core.JdbcTemplate;

public class GoalDao {
	private JdbcTemplate jdbcTemplate;
	
	public GoalDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public boolean insert(Goal goal) {
		String sql = "insert into goals(email,name,goalText,knownText) values(?,?,?,?)";
		String pw = goal.getPw();
		while(pw.length()<16) {
			pw = pw + pw;
		}
		try {
			String knownText = AES256.encrypt(Goal.KNOWN, pw);
			String goalText = AES256.encrypt(goal.getGoalText(), pw);
			int result =  jdbcTemplate.update(sql,goal.getEmail(),goal.getName(),goalText,knownText);
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
}
