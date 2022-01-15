package dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Goal {
	public static final String KNOWN="Happy new Year";
	@NonNull private String email;
	@NonNull private String name;
	@NonNull private String goalText;
	@NonNull private String pw;
	private Timestamp createTime;
	public Goal() {}
}
