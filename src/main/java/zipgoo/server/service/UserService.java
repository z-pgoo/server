package zipgoo.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zipgoo.server.domain.Role;
import zipgoo.server.domain.User;
import zipgoo.server.dto.LoginDto;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.exception.ErrorResponse;
import zipgoo.server.jwt.JwtService;
import zipgoo.server.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Transactional
    public ResponseEntity<Map<String, Object>> signUp(UserSignUpDto userSignUpDto) throws Exception{
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if(userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(400, "이미 존재하는 닉네임 입니다.");
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            return ResponseEntity.badRequest().build();
        }


        String accessToken = jwtService.createAccessToken();
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setAccessTokenHeader(response, accessToken);
        jwtService.setRefreshTokenHeader(response, refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);

        // 액세스 토큰으로 해당 sns api에서 정보 받아오는 코드 구현하고, 이메일, 닉네임, 생년월일 등 받아오기
        // https://developers.naver.com/docs/login/profile/profile.md

        String email = null;
        String birthDate = null;

        System.out.println(userSignUpDto.getSnsType());
        System.out.println(userSignUpDto.getAccessToken());
        System.out.println(userSignUpDto.getSnsType());

        //----------------------------------------------------------------------------------------------------------------------------------------------------------------//
        if(userSignUpDto.getSnsType().equals("naver")) {

            String snsAccessToken = userSignUpDto.getAccessToken();
            String header = "Bearer " + snsAccessToken;
            String apiURL = "https://openapi.naver.com/v1/nid/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = getBody(apiURL, requestHeaders);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            System.out.println(jsonNode);

            email = jsonNode.get("response").get("email").asText();
            String birthday = jsonNode.get("response").get("birthday").asText();
            String birthyear = jsonNode.get("response").get("birthyear").asText();
            birthDate = birthyear + "." + birthday.substring(0, 2) + "." + birthday.substring(2); // 1999.07.15와 같은 형식
        }

        else if(userSignUpDto.getSnsType().equals("kakao")){

            String snsAccessToken = userSignUpDto.getAccessToken();
            String header = "Bearer " + snsAccessToken;
            String apiURL = "https://kapi.kakao.com/v2/user/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = getBody(apiURL, requestHeaders);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            email = jsonNode.get("kakao_account").get("email").asText();
            birthDate = jsonNode.get("kakao_account").get("birthday").asText();
        }

        else{
            Map<String, Object> result = new HashMap<>();
            result.put("data", Collections.emptyMap());
            return ResponseEntity.badRequest().body(result);
        }



        //----------------------------------------------------------------------------------------------------------------------------------------------------------------//

        User user = User.builder()
                .email(email)
                .nickname(userSignUpDto.getNickname())
                .birthDate(birthDate)
                .role(Role.USER)
                .refreshToken(refreshToken)
                .build();
        userRepository.saveAndFlush(user);
        System.out.println(refreshToken);

        Map<String, Object> result = new HashMap<>();

        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("email", user.getEmail());
            data.put("nickname", user.getNickname());
            data.put("birthDate", user.getBirthDate());

            result.put("data", data);
        } else {
            result.put("data", Collections.emptyMap());
        }

        return ResponseEntity.ok().body(result);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> login(LoginDto loginDto) throws Exception{
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        // 액세스 토큰으로 해당 sns api에서 정보 받아오는 코드 구현하고, 이메일, 닉네임, 생년월일 등 받아오고 닉네임에 해당하는 정보 가져오기.
        String email = null;

        if(loginDto.getSnsType().equals("naver")) {

            String snsAccessToken = loginDto.getAccessToken();
            String header = "Bearer " + snsAccessToken;
            String apiURL = "https://openapi.naver.com/v1/nid/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = getBody(apiURL, requestHeaders);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            email = jsonNode.get("response").get("email").asText();
        }
        else if(loginDto.getSnsType().equals("kakao")){

            String snsAccessToken = loginDto.getAccessToken();
            String header = "Bearer " + snsAccessToken;
            String apiURL = "https://kapi.kakao.com/v2/user/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = getBody(apiURL, requestHeaders);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            email = jsonNode.get("kakao_account").get("email").asText();
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        //------------------------------------------------------------------//

        String accessToken = jwtService.createAccessToken();
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setAccessTokenHeader(response, accessToken);
        jwtService.setRefreshTokenHeader(response, refreshToken);

        Optional<User> user = userRepository.findByEmail(email);
        Map<String, Object> result = new HashMap<>();

        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("email", user.get().getEmail());
            data.put("nickname", user.get().getNickname());
            data.put("birthDate", user.get().getBirthDate());

            result.put("data", data);
            return ResponseEntity.ok().body(result);
        } else {
            result.put("code", 400);
            result.put("message", "해당 정보가 존재하지 않습니다. 회원가입을 진행합니다.");
            return ResponseEntity.badRequest().body(result);
        }


    }

    public static String getBody(String apiURL, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiURL);
        try{
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()){
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                return readBody(con.getInputStream());
            }
            else{
                return readBody(con.getErrorStream());
            }

        }catch (IOException e){
            throw new RuntimeException("API 요청 및 응답 실패");
        }finally {
            con.disconnect();
        }
    }

    public static HttpURLConnection connect(String apiURL){
        try{
            URL url = new URL(apiURL);
            return (HttpURLConnection)url.openConnection();
        }catch (MalformedURLException e){
            throw new RuntimeException("API URL 연결이 잘못되었습니다. : " + apiURL, e);
        }catch (IOException e){
            throw new RuntimeException("연결 실패했습니다." + apiURL, e);
        }
    }

    public static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try(BufferedReader lineReader = new BufferedReader(streamReader)){
            StringBuilder responseBody = new StringBuilder();

            String line;
            while((line = lineReader.readLine()) != null){
                responseBody.append(line);
            }

            return responseBody.toString();

        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다." , e);
        }
    }

}