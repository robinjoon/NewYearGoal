package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import controller.GoalController;



@Configuration
public class ControllerConfig {
	@Bean
	public GoalController getGoalController() {
		return new GoalController();
	}
}
