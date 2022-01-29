package controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import dao.GoalDao;
import dto.*;
import security.AES256;
@RestController
@RequestMapping("/goals")
public class GoalController {
	@Autowired
	private GoalDao dao;
	@PostMapping("")
	public ResponseEntity<Goal> newGoal(@RequestBody Goal goal){
		if(dao.insert(goal)) {
			System.out.println("성공");
			return ResponseEntity.ok(goal);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	@GetMapping("/{email:.+}") // 이메일로 사용자의 목표 리스트 검색
	public ResponseEntity<List<Goal>> getGoalList(@PathVariable("email") String email){
		List<Goal> result = dao.selectGoals(email);
		if(result.isEmpty()) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok(result);
		}
	}
	@GetMapping("/{year}/all/count") // 해당 년도에 등록된 모든 목표 개수
	public ResponseEntity<Long> getGoalCount(@PathVariable("year") int year){
		long count = dao.selectGoalCount(year);
		if(count==-1) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok(count);
		}
	}
	@GetMapping("/{year}/achieved/count") // 해당 년도에 등록된 모든 목표중 달성된 목표 개수
	public ResponseEntity<Long> getAchieveGoalCount(@PathVariable("year") int year){
		long count = dao.selectAchieveGoalCount(year);
		if(count==-1) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok(count);
		}
	}
	@PutMapping("/{email:.+}/{timestamp}") // 목표 달성
	public ResponseEntity<?> achieveGoal(@PathVariable("email")String email, @PathVariable("timestamp")String timestampString,@RequestBody Goal goal) throws Exception{
		timestampString = timestampString.replace('T', ' ');
		Timestamp timestamp = Timestamp.valueOf(timestampString); 
		if(!email.equals(goal.getEmail()) || !timestamp.equals(timestamp)) {
			return ResponseEntity.badRequest().build();
		}else if(dao.canModify(AES256.encrypt(Goal.KNOWN,goal.getPw()), email, timestamp)){ // 비밀번호 검증
			boolean result = dao.achieveGoal(goal);
			if(result) {
				return ResponseEntity.ok().build();
			}else {
				return ResponseEntity.badRequest().build();
			}
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //인증 실패
		}
	}
}
