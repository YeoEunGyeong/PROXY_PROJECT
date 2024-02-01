package com.mycompany.myapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignController {
	Logger logger = LoggerFactory.getLogger(SignController.class);
	
	@Autowired
	SignService signService;
	
	// 대리결재
	@RequestMapping(value = "/delegate", method = RequestMethod.GET)
	public String delegatePage(HttpSession session, Model model) {
		logger.info("SignController: delegatePage");
		String userRank = (String) session.getAttribute("RANK");
        String userName = (String) session.getAttribute("NAME");
        String userId = (String) session.getAttribute("ID");
        
        List<MemberDto> mList = signService.delegatePage(userRank);

        model.addAttribute("userRank", userRank);
        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);
        model.addAttribute("mList", mList);
		return "delegatePop";
	}
	
	// 대리결재 선택 대리결재자 - 직급 비동기 
	@ResponseBody
	@RequestMapping(value = "/getMemberRank", method = RequestMethod.POST, produces = "application/text; charset=utf8")
    public String getMemberRank(@RequestParam String memberId) {
		logger.info("SignController: getMemberRank");
		System.out.println("선택된 memberId 확인 : " + memberId);
		
		String rank = signService.getMemberRank(memberId);
		System.out.println("선택된 memberId Rank : " + rank);
        return rank;
    }
	
	// 대리결재 데이터 등록
	@RequestMapping(value = "/approveInsert", method = RequestMethod.POST)
	public ResponseEntity<String> delegateInsert(@RequestParam Map<String, Object> map) {
		logger.info("SignController: delegateInsert");
		System.out.println("map 값 확인: " + map);
		
		signService.delegateInsert(map);
		
	    // 대리결재 승인 완료 메시지를 클라이언트에게 응답
	    String responseMessage = "<script>alert('대리결재 승인'); window.close();</script>";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_HTML);
	    headers.set("Content-Type", "text/html; charset=UTF-8");

	    return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
	}
	
    // 로그인 페이지 로딩
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLoginPage() {
        return "signLogin";
    }
    
    // 결재 글쓰기 페이지 로딩
    @RequestMapping(value = "/signWrite", method = RequestMethod.POST)
    public String signWritePage(HttpSession session, Model model) {
        // 작성자 이름 가져와서 화면 추가
    	String userId = (String) session.getAttribute("ID");
    	String userRank = (String) session.getAttribute("RANK");
        String userName = (String) session.getAttribute("NAME");
        model.addAttribute("userId", userId);
        model.addAttribute("userRank", userRank);
        model.addAttribute("userName", userName);
        // 최대 SEQ 값을 가져와서 모델에 추가
        int maxSeq = signService.getMaxSeq();
        model.addAttribute("maxSeq", maxSeq);

        return "signWrite";
    }
    
    // 로그인 유효성
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String signLogin(
            @RequestParam("userid") String userid,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        logger.info("SignController: signLogin");
        try {
            // 로그인 성공 시 사용자 정보 조회
            MemberDto member = signService.signLogin(userid, password);

            // 세션에 사용자 정보 저장
            session.setAttribute("ID", member.getId());
            session.setAttribute("NAME", member.getName());
            session.setAttribute("RANK", member.getRank());

            // 성공 페이지로 리디렉션
            return "redirect:/main";
        } catch (AuthenticationException e) {
            // 오류 메시지를 모델에 추가
            model.addAttribute("error", e.getMessage());
            // 로그인 페이지로 이동
            return "signLogin";
        }
    }
    
    // 로그아웃
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String signLogout(HttpSession session) {
        session.invalidate();
        return "signLogin";
    }
    
    // 메인 페이지 (전체 리스트 출력)
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String signMain(HttpSession session, Model model) {
        logger.info("SignController: signMain");

        String userId = (String) session.getAttribute("ID");
        String userName = (String) session.getAttribute("NAME");
        String userRank = (String) session.getAttribute("RANK");
        
        // 사용자가 로그인되어 있지 않은 경우
        if (userId == null || userName == null || userRank == null) {
            // 로그인이 필요한 페이지로 이동
            model.addAttribute("message", "로그인 필요 페이지");
            return "signLogin";
        } 
        
        // 사용자와 대리 결재자 비교
        Map<String, Object> proxyInfo = signService.proxyInfo(userId);

        if (proxyInfo != null && proxyInfo.containsKey("PROXY_DATE")) {
            // 프록시 유효 시간 확인
            String proxyDate = (String) proxyInfo.get("PROXY_DATE");
            if (validProxyDate(proxyDate)) {
                // 프록시 유효한 경우, 메인 페이지 띄우기
                List<BoardDto> sList = signService.signList(userId, userName, userRank, proxyInfo);
                model.addAttribute("sList", sList);
                model.addAttribute("proxyInfo", proxyInfo);
                return "signMain";
            } else {
                // 프록시 유효 시간이 지난 경우, 권한 무효화 처리
                signService.validDate(userId);
                return "redirect:/main";
            }
        }

        // 사용자의 글 목록을 가져와 모델에 추가
        List<BoardDto> sList = signService.signList(userId, userName, userRank, proxyInfo);
        model.addAttribute("sList", sList);
        return "signMain";
    }
    
    // 대리결재 유효성 검사
    private boolean validProxyDate(String proxyDate) {
        // SimpleDateFormat을 사용하여 문자열을 날짜로 변환
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date proxyDateTime = format.parse(proxyDate);
            
            // 현재 시간 구하기
            Date currentDateTime = new Date();

            // 프록시 유효 시간보다 1시간 더하기
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(proxyDateTime);
            calendar.add(Calendar.HOUR, 1);
            Date proxyDateTimePlusOneHour = calendar.getTime();

            // 현재 시간과 프록시 유효 시간 + 1시간 비교하여 1시간 이상 경과한 경우
            if (currentDateTime.after(proxyDateTimePlusOneHour)) {
                // 대리결재 권한 취소
                return false;
            } else {
                // 유효한 경우
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 유효하지 않은 경우
        return false;
    }

    
    // 검색 옵션에 따른 검색 리스트 출력 (동기식)
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String signSearch (
    		@RequestParam(value = "search-options", required = false) String searchOption,
    		@RequestParam(value = "search-keyword", required = false) String searchKeyword,
    		@RequestParam(value = "search-status-options", required = false) String searchStatus,
    		@RequestParam(value = "start-date", required = false) String startDate,
    		@RequestParam(value = "end-date", required = false) String endDate,
    		HttpSession session,
    		Model model) {
        
        String userId = (String) session.getAttribute("ID");
        String userRank = (String) session.getAttribute("RANK");
        
    	List<BoardDto> sList = signService.signSearch(userId, userRank, searchOption, searchKeyword, searchStatus, startDate, endDate);
    	model.addAttribute("sList", sList);
    	
    	return "signMain";
    }
    
    // 검색 옵션에 따른 검색 리스트 출력 (비동기식)
    @ResponseBody
    @RequestMapping(value = "/statuslist", method = RequestMethod.GET, produces = "application/json")  
    public Map<String, Object> signStatusList(@RequestParam("signStatus") String signStatus,
                                              @RequestParam(value = "search-options", required = false) String searchOption,
                                              @RequestParam(value = "search-keyword", required = false) String searchKeyword,
                                              @RequestParam(value = "search-status-options", required = false) String searchStatus,
                                              @RequestParam(value = "start-date", required = false) String startDate,
                                              @RequestParam(value = "end-date", required = false) String endDate,
                                              HttpSession session) {
        logger.info("SignController: signStatusList");

        String userId = (String) session.getAttribute("ID");
        String userRank = (String) session.getAttribute("RANK");

        
        // 동기 검색 수행
        List<BoardDto> searchResults = signService.signSearch(userId, userRank, searchOption, searchKeyword, searchStatus, startDate, endDate);

        // Filter the search results based on the selected status
        List<BoardDto> statusList = new ArrayList<>();
        for (BoardDto post : searchResults) {
            if (signStatus.equals(post.getSignStatus())) {
                statusList.add(post);
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("signStatus", signStatus);
        resultMap.put("statusData", statusList);

        return resultMap;
    }


    // 글쓰기
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String signWrite(@RequestParam("subject") String subject,
                            @RequestParam("content") String content,
                            @RequestParam("action") String action,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        logger.info("SignController: signWrite");

        // 사용자 정보 가져오기
        String userId = (String) session.getAttribute("ID");
        String userName = (String) session.getAttribute("NAME");
        String userRank = (String) session.getAttribute("RANK");

        // 사용자와 대리결재자 비교
        Map<String, Object> proxyInfo = signService.proxyInfo(userId);
        System.out.println("상세 조회 proxyInfo 값 확인: " + proxyInfo);
        
        boolean isProxy = false;
        String apperId = null;

        if (proxyInfo != null && !proxyInfo.isEmpty()) {
            String proxyId = (String) proxyInfo.get("PROXY_ID");

            if (userId.equals(proxyId)) {
                isProxy = true;
                userRank = (String) proxyInfo.get("APPER_RANK");
                System.out.println("게시글 작성 대리권한 userRank 확인 : " + userRank);

                apperId = (String) proxyInfo.get("APPER_ID");
            }
        }

        BoardDto writeboard = new BoardDto(); 
        writeboard.setId(userId);
        writeboard.setSubject(subject);
        writeboard.setContent(content);
        writeboard.setRegDate(new Date());
        writeboard.setApperId(apperId);


        // "action" 값에 따라 board의 SignStatus 설정
        if ("save".equals(action)) {
            writeboard.setSignStatus("SAVE");
        } else if ("approve".equals(action)) {
        	// 과장인 경우 결재 시 결재중
            if ("과장".equals(userRank)) {
                writeboard.setSignStatus("APPROVEWAIT");
            } 
            // 부장인 경우 결재 시 결재완료
            else if ("부장".equals(userRank)) {
                writeboard.setSignStatus("ACCEPT");
            } 
            // 과장, 부장이 아닌 경우 결재 시 결재요청
            else {
                writeboard.setSignStatus("APPROVE");
            }
        } 
        else if ("reject".equals(action)) {
            writeboard.setSignStatus("REJECT");
        } 
        else {
        }

        // board를 서비스로 전달하여 처리
        System.out.println("writeboard 확인:");
        System.out.println("Id :" + writeboard.getId());
        System.out.println("Subject: " + writeboard.getSubject());
        System.out.println("Content: " + writeboard.getContent());
        System.out.println("RegDate: " + writeboard.getRegDate());
        System.out.println("ApperId: " + writeboard.getApperId());
        System.out.println("SignStatus: " + writeboard.getSignStatus());
        signService.signWrite(writeboard);
        
        
        // 글등록 처리 후 히스토리 등록 처리
        if ("save".equals(action) || "approve".equals(action)) {
        	int boardSeq = writeboard.getSeq();
        	// System.out.println("boardSeq :" + boardSeq); // 게시글 번호 받아오기 위함
        	
            BoardDto board = new BoardDto();
            board.setSeq(boardSeq);
            board.setRegDate(new Date());
            board.setSignStatus(writeboard.getSignStatus());
            board.setSignerId(writeboard.getId());
            board.setApperId(apperId); // 대리결재자의 ID 추가
            
            signService.signHistory(board);
            System.out.println("board 확인:");
            System.out.println("Seq: " + board.getSeq());
            System.out.println("RegDate: " + board.getRegDate());
            System.out.println("SignStatus: " + board.getSignStatus());
            System.out.println("SignerId: " + board.getSignerId());
            System.out.println("ApperId: " + board.getApperId());
        }
        
        // 사용자의 글 목록을 가져와서 모델에 추가
        List<BoardDto> sList = signService.signList(userId, userName, userRank);
        redirectAttributes.addFlashAttribute("sList", sList);

        // 글쓰기가 완료되면 signMain 페이지로 redirect
        return "redirect:/main";
    }
    
    // 게시글 수정
    @RequestMapping(value = "/writeupd", method = RequestMethod.POST)
    public String signWriteUpd(@RequestParam("seq") int seq,
    							@RequestParam("hiddenId") String hiddenId,
                                @RequestParam("subject") String subject,
                                @RequestParam("content") String content,
                                @RequestParam("action") String action,
                                HttpSession session,
                                Model model) {
        logger.info("SignController: signWriteUpd");

        // 사용자 정보 가져오기
        String userId = (String) session.getAttribute("ID");
        String userName = (String) session.getAttribute("NAME");
        String userRank = (String) session.getAttribute("RANK");
        
        // 사용자와 대리결재자 비교
        Map<String, Object> proxyInfo = signService.proxyInfo(userId);
        System.out.println("상세 조회 proxyInfo 값 확인: " + proxyInfo);
        
        boolean isProxy = false;
        String apperId = null;

        if (proxyInfo != null && !proxyInfo.isEmpty()) {
            String proxyId = (String) proxyInfo.get("PROXY_ID");

            if (userId.equals(proxyId)) {
                isProxy = true;
                userRank = (String) proxyInfo.get("APPER_RANK");
                System.out.println("게시글 수정 대리권한 userRank 확인 : " + userRank);

                apperId = (String) proxyInfo.get("APPER_ID");
            }
        }
        
        BoardDto board = new BoardDto();
        board.setSeq(seq);
        board.setId(hiddenId);
        board.setSubject(subject);
        board.setContent(content);
        board.setSignDate(new Date());
        board.setSignerId(userId);
        board.setApperId(apperId);
        
		// "action" 값에 따라 board의 SignStatus 설정
		// 임시저장 실행
		if ("save".equals(action)) {
			board.setSignStatus("SAVE");
			board.setSignerId("");
		}

		// 결재 실행
		else if ("approve".equals(action)) {
			// 접속한 id와 게시글 작성자 id 일치 확인
			if (userId.equals(board.getId())) {
				// userRank가 '사원', '대리'인 경우 임시저장 -> 결재요청
				if ("사원".equals(userRank) || "대리".equals(userRank)) {
					board.setSignStatus("APPROVE");
					board.setSignerId("");
				}
			} else {
				// userRank가 '과장'인 경우 결재요청 -> 결재중
				if ("과장".equals(userRank)) {
					board.setSignStatus("APPROVEWAIT");
				}
				// userRank가 '부장'인 경우 결재 중 -> 결재완료
				else if ("부장".equals(userRank)) {
					board.setSignStatus("ACCEPT");
				}
			}
		}

		// 반려 실행
		else if ("reject".equals(action)) {
			if ("과장".equals(userRank) || "부장".equals(userRank)) {
				board.setSignStatus("REJECT");
			} 
		} else {
		}
		
        // board를 서비스로 전달하여 처리
        System.out.println("수정 board 확인:");
        System.out.println("Id :" + board.getId());
        System.out.println("Subject: " + board.getSubject());
        System.out.println("Content: " + board.getContent());
        System.out.println("RegDate: " + board.getRegDate());
        System.out.println("ApperId: " + board.getApperId());
        System.out.println("SignStatus: " + board.getSignStatus());
        signService.signWriteUpd(board, userRank);
        
        // signStatus를 전달하기 위해 변수 지정
        String originalSignStatus = board.getSignStatus();
        
        board = new BoardDto();
        board.setSeq(seq); 
        board.setRegDate(new Date());
        board.setSignStatus(originalSignStatus); 
        board.setSignerId(userId);
        board.setApperId(apperId);

        System.out.println("히스토리 board 확인:");
        System.out.println("Seq: " + board.getSeq());
        System.out.println("RegDate: " + board.getRegDate());
        System.out.println("SignStatus: " + board.getSignStatus());
        System.out.println("SignerId: " + board.getSignerId());
        System.out.println("ApperId: " + board.getApperId());
        
        signService.signHistory(board);

        // 사용자의 글 목록을 가져와 모델에 추가
        List<BoardDto> sList = signService.signList(userId, userName, userRank, proxyInfo);
        
        // proxyInfo가 null이 아닌 경우에만 모델에 추가
        if (proxyInfo != null) {
            model.addAttribute("proxyInfo", proxyInfo);
        }
        // 사용자의 글 목록을 가져와서 모델에 추가
        //List<BoardDto> sList = signService.signList(userId, userName, userRank);
        model.addAttribute("sList", sList);

        // 글쓰기가 완료되면 signMain 페이지로 redirect
        return "redirect:/main";
    }
    
    // 게시글 상세 조회
    @RequestMapping(value = "/signdatail", method = RequestMethod.GET)
    public String signDetail (@RequestParam("seq") int seq,
    		HttpSession session,
			Model model) {
    	logger.info("SignController: signDetail");
    	
        String userName = (String) session.getAttribute("NAME");
        String userId = (String) session.getAttribute("ID");
        String userRank = (String) session.getAttribute("RANK");
    	
        // 사용자와 대리결재자 비교
        Map<String, Object> proxyInfo = signService.proxyInfo(userId);
        System.out.println("상세 조회 proxyInfo 값 확인: " + proxyInfo);

        // 대리결재자 권한 부여 여부 확인
        boolean isProxy = false;

        if (proxyInfo != null && !proxyInfo.isEmpty()) {
            String proxyId = (String) proxyInfo.get("PROXY_ID");

            if (userId.equals(proxyId)) {
                isProxy = true;
                userRank = (String) proxyInfo.get("APPER_RANK");
                System.out.println("게시글 수정 대리권한 userRank 확인 : " + userRank);
            }
        }
        
    	BoardDto board = signService.signDetail(seq);
    	List<HistoryDto> historyList = signService.historyList(seq);
    	
    	model.addAttribute("board", board);
        model.addAttribute("userRank", userRank);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("historyList", historyList);
    	return "signDetail";
    }
    
}
