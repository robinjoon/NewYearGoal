package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Goal {
	public static final String KNOWN="Happy new Year";
	private String email;
	private String name;
	private String goalText;
	private String pw;
	public Goal() {}
}
