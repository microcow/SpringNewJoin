package com.restdemo.controller;

import com.restdemo.domain.AuthRequestDTO;
import com.restdemo.domain.Board;
import com.restdemo.domain.JwtResponseDTO;
import com.restdemo.domain.User;
import com.restdemo.domain.UserAlreadyExistsException;
import com.restdemo.service.BoardService;
import com.restdemo.service.JwtService;
import com.restdemo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // 리턴되는 정보가 json 형태로 리턴된다 //jdk랑 그래들 버전 통일 (gredle8.9 / jdk22)
public class UserController {

 
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userservice;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    BoardService boardservice;    
    @Autowired
    AuthenticationManager authenticationManager; // 해당 authenticationManager객체는 authenticationManager메서드의 결과(return)를 담고있음

    @PostMapping("/api/SetToken")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){ // @RequestBody 애노테이션은 프론트에서 보낸 요청의 본문(즉, body)을 Java 객체로 변환하는 데 사용
        Authentication authentication = authenticationManager.authenticate(
        	new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        // 실제로 입력받은 값과 db의 정보를 비교 및 인증하는 과정은 위 코드에서 일어남. 인증 성공 시 토큰 return (위 과정에서 username을 가지고 authenticationManager메서드에서 설정한 loadUserByUsername도 호출하고 비밀번호를 Bcrypt방식으로 비교도 함)
        if(authentication.isAuthenticated()){
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
            jwtResponseDTO.setAccessToken(jwtService.GenerateToken(authRequestDTO.getUsername()));
            /* 토큰을 JwtResponseDTO 클래스의 객체에 accessToken이라는 인스턴스 변수에 setAccessToken 메서드를 통해 저장하고 있음
               그래서, 프론트에서 return받는 객체이름이 responseData라고 가정하면 responseData.accessToken 이렇게 서버에서 보낸 토큰값을 불러와야함
            */
            return jwtResponseDTO; // 만약 이미 토큰을 발급받은 유저가 다시 로그인할 경우 
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
    
    @GetMapping("/hello")
    public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User();
    }
   
    @PostMapping("/api/Signup") // Signup은 시큐리티에서 .permitAll() 처리 되었기에 인증되지 않은 이용자도 접근 가능 
    public ResponseEntity<Void> Signup(@RequestBody User user){ // ResponseEntity<Void>는 상태 코드만 반환하고, 추가적인 응답 본문이 필요 없는 경우 사용
    	
    	user.setPassword(passwordEncoder.encode(user.getPassword())); // BCryptPasswordEncoder를 주입받음 (BCryptPasswordEncoder 객체를 생성한 후, encode 메서드를 사용하여 비밀번호를 인코딩)
    	
    	 try {
             userservice.createUser(user);
             return ResponseEntity.ok().build();  // HTTP 200 OK 반환
         } catch (DuplicateKeyException e) {
             // 중복된 이메일일 경우 커스텀 예외를 던집니다.
        	 throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.", e);
             // DuplicateKeyException은 RuntimeException의 자식클래스 (RuntimeException클래스를 UserAlreadyExistsException쿨래스가 상속받고있음)
             // 모든 UserAlreadyExistsException은 GlobalExceptionHandler에서 글로벌하게 관리됨
             // 즉, DuplicateKeyException이 발생하면 UserAlreadyExistsException로 포장하여 GlobalExceptionHandler로 보내짐
         }
    	
 
    }
    
    @PostMapping("/api/Signin")
    public JwtResponseDTO Signin(@RequestBody User user){ // Signin은 시큐리티에서 .permitAll() 처리 되었기에 인증되지 않은 이용자도 접근 가능 
    	User getUser = userservice.readUser(user.getUsername());
    	if (getUser.getEmail().equals(user.getUsername()) || // 프론트에서 username 객체에 email정보를 보내고 있기에 user.getUsername은 이메일 형식임(SignUp에서는 email객체에 email정보를 담아서 보내고있음(차이점))
    		getUser.getPassword().equals(user.getPassword())) {
    	
    	AuthRequestDTO token = new AuthRequestDTO();
    	token.setUsername(user.getUsername());
    	token.setPassword(user.getPassword()); // 입력받은 값과 db에 저장된 값이 일치하는지 인증해야되기 때문에 암호화된 비밀번호가 아닌 유저에게 입력받은 값을 세팅해야함
    	
    	return AuthenticateAndGetToken(token);// token 생성 과정에서 시큐리티의 authenticationManager메서드가 호출됨
    	}
    	else return null;
    }
    
    @Secured({"ROLE_USER"})
    @PostMapping("/api/CreateBoard")
    public String CreateBoard(@RequestBody Board board, @AuthenticationPrincipal UserDetails userDetails){ // ★ @AuthenticationPrincipal UserDetails userDetails : doFilterInternal메서드에서 인증처리 후 인증된 사용자 정보를 받음
    	
    	if(board.getContents() != null || board.getTitle() != null) {
    		board.setName(userDetails.getUsername());
    		
    		boardservice.createBoard(board);
    		
    		board.setP_board(board.getB_id());
    		board.setDepth(1);
    		board.setGrpord(0);
    			
    		boardservice.updateBoard(board);
    
    		return "Complete!";
    	}
    	else
    	return "someting wrong...";
    }
    
    @Secured({"ROLE_USER"})
    @PostMapping("/api/BoardList")
    public List<Board> BoardList(@AuthenticationPrincipal UserDetails userDetails){ // ★ @AuthenticationPrincipal UserDetails userDetails : doFilterInternal메서드에서 인증처리 후 인증된 사용자 정보를 받음
    	List<Board> boardList = new ArrayList();
    	boardList = boardservice.boardList();
    	
    	return boardList;
    }
    
    @Secured({"ROLE_USER"})
    @PostMapping("/api/BoardDetail")
    public Board BoardDetail(@RequestBody String b_id, @AuthenticationPrincipal UserDetails userDetails){ // ★ @AuthenticationPrincipal UserDetails userDetails : doFilterInternal메서드에서 인증처리 후 인증된 사용자 정보를 받음
    	Board board = new Board();
    	board = boardservice.readBoard(Integer.parseInt(b_id));
    	
    	return board;
    }
    
    
   
   
    // @PreAuthorize 어노테이션을 사용하여 특정 메서드가 실행되기 전에 접근 권한을 확인할 수 있습니다.
    // 이 메서드는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있음
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Secured({"ROLE_USER"}) // @PreAuthorize와 @Secured의 차이 : @PreAuthorize는 보다 복잡한 표현식 사용 가능
    @GetMapping("/ping")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
   
    // 이 메서드는 ROLE_USER 권한을 가진 사용자이면서, username 파라미터 값이 현재 사용자와 동일한 경우에만 호출할 수 있음
    @PreAuthorize("hasRole('ROLE_USER') and #username == authentication.principal.username")
    public void userSpecificOperation(String username) {  
    }

}