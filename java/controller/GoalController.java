package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dao.GoalDao;
import dto.*;
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
}
