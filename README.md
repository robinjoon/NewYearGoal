robinjoon/NewYearGoal
=============
2022년을 기념해 만든 신년 목표설정  시스템
-------------


## Usage
![image](https://user-images.githubusercontent.com/45223837/149436438-413a1f68-3c63-4382-914d-d99881a11db1.png)
1. 화면에 보이는 입력창을 모두 채워주세요.
2. 비밀번호는 반드시 8글자여야합니다.(사용한 암호화방식때문)
3. 목표는 최대 200글자만 가능합니다.
4. 이미 등록된 이메일이라면 또 등록할 수는 없습니다. 현 시점에선 목표의 수정이나 추가가 불가능하니 신중하게 작성해 주세요.
5. 실패 라는 메세지가 뜨면 본인이 사용한 이메일과 함께 메일로 문의남겨주시면 빠르게 해결해 드리겠습니다.

## Q&A

1. 어떻게 목표를 암호화하여 저장하나요?
- 사용자로부터 입력받은 비밀번호로 목표를 암호화하여 DB에 저장합니다. 동시에 "미리 지정해둔 문자열"도 따로 암호화 하여 DB에 저장합니다.
- 지정된 기간이 지나 사용자에게 목표달성 여부를 설정 할 수 있는 링크가 포함된 메일이 전송됩니다.
- 사용자가 메일에 포함된 링크를 클릭한 후 화면에 보여지는 입력창에 비밀번호를 입력합니다.
- 서버에 저장된 "미리 지정된 문자열의 암호문" 을 사용자가 입력한 비밀번호로 복호화 합니다. 만일, 복호화 결과가 원문과 같다면 비밀번호가 일치하는 것으로 판단하여 "암호화된 목표"를 입력된 비밀번호로 복호화 하여 사용자에게 전달합니다. 

