package dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GoalDue {
	ONE_YEAR, SIX_MONTHS, THREE_MONTHS, ONE_WEEK, NEXT_YEAR;

	@JsonCreator
	public static GoalDue from(String s) {
		return GoalDue.valueOf(s.toUpperCase());
	}
}
