package dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Goal {
	public static final String KNOWN="Happy new Year";
	@NonNull private String email;
	@NonNull private String name;
	@NonNull private String goalText;
	@NonNull private String pw;
	@NonNull private GoalDue due;
	@Setter
	private Timestamp createTime;
	@Setter
	private boolean isAchieved;
	public Goal() {}
}
