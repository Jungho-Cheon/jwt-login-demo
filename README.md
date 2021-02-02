# JWT 기반의 로그인 기능 구현

### 프로젝트 목표
* Spring Boot기반의 WAS에서 JWT 기반의 로그인 서비스를 구현한다.

### 로그인 프로세스
1. 사용자가 클라이언트(React.js App)를 통해 서버로 로그인을 요청한다.
2. 서버는 사용자의 Credential이 유효한지 검증한다. (이메일, 비밀번호 검증)
3. 검증이 완료되면 응답 헤더에 `Set-Cookie` 키로 `refreshToken`을 추가하고, Response Body에 `accessToken`을 반환한다.
4. 클라이언트는 accessToken을 보안상 쿠키나 localstorage에 저장하지 않고 변수에 저장하여 메모리에 들고 사용한다.

### accessToken 재발급 시나리오
* accessToken이 만료되는 경우
    * 자동으로 재발급을 하는 경우 클라이언트에서 setTimeout()을 통해 로그인을 한 순간부터 일정 시간이 지나면 자동으로 accessToken을 재발급 받도록 로직을 구현한다.
    * 자동으로 재발급을 하지 않는 경우 클라이언트에서 로그인 페이지로 리다이렉트하여 다시 로그인하도록 한다.
* 사용자가 클라이언트 웹을 떠난경우
    * `Cookie`에 존재하는 `refreshToken`을 서버로 전송하여 새로운 `accessToken`을 발급받는다.
    
> `csrf 토큰` 역시 쿠키로 전달한다. 프론트엔드 프레임워크에서 접근할 수 있도록 `http-only`를 해제한다.
