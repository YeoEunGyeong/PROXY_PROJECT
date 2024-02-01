package com.mycompany.myapp;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignServiceImpl implements SignService {
    private static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Autowired
    private SignDao signDao;

    // 로그인 유효성
    @Override
    public MemberDto signLogin(String userid, String password) throws AuthenticationException {
        logger.info("SignServiceImpl: signLogin");

        // 사용자 조회
        MemberDto member = signDao.signLogin(userid);

        if (member == null) {
            throw new AuthenticationException("등록되지 않은 사용자");
        }

        // 비밀번호 검증
        if (!password.equals(member.getPw())) {
            throw new AuthenticationException("비밀번호 불일치");
        }

        return member;
    }
    
    // 게시글 최대 번호
    @Override
    public int getMaxSeq() {
        return signDao.getMaxSeq();
    }

    // 글쓰기
    @Override
    public int signWrite(BoardDto writeboard) {
        logger.info("SignServiceImpl: signWrite");
        return signDao.signWrite(writeboard);
    }

    // 글수정
    @Override
    public int signWriteUpd(BoardDto board, String userRank) {
    	logger.info("SignServiceImpl: signWriteUpd");
    	logger.info("userRank : {}", userRank);
    	System.out.println("게시글 수정 apperId 확인 : " + board.getApperId());
    	return signDao.signWriteUpd(board, userRank);
    }
    
    // 게시글 전체 리스트
    @Override
    public List<BoardDto> signList(String userId, String userName, String userRank) {
        logger.info("SignServiceImpl: signList");
        return signDao.signList(userId, userName, userRank);
    }

    // 게시글 상세 조회
    @Override
    public BoardDto signDetail(int seq) {
        logger.info("SignServiceImpl: signDetail");
        return signDao.signDetail(seq);
    }

    // 게시글 검색
	@Override
	public List<BoardDto> signSearch(String userId, String userRank, String searchOption, String searchKeyword, String searchStatus, String startDate,
			String endDate) {
		logger.info("SignServiceImpl: signSearch");
		return signDao.signSearch(userId, userRank, searchOption, searchKeyword, searchStatus, startDate, endDate);
	}

	// 게시글 결재 상태 히스토리
	@Override
	public int signHistory(BoardDto board) {
		logger.info("SignServiceImpl: signHistory");
		return signDao.signHistory(board);
	}

	// 게시글 결재 상태 히스토리 리스트
	@Override
	public List<HistoryDto> historyList(int boardSeq) {
		logger.info("SignServiceImpl: historyList");
		return signDao.historyList(boardSeq);
	}

	// 대리결재자 리스트
	@Override
	public List<MemberDto> delegatePage(String userRank) {
		logger.info("SignServiceImpl: delegatePage");
		return signDao.delegatePage(userRank);
	}

	// 대리결재 데이터 등록
	@Override
	public void delegateInsert(Map<String, Object> map) {
		logger.info("SignServiceImpl: delegateInsert");
		signDao.delegateInsert(map);
	}

	// 대리결재 정보
	@Override
	public Map<String, Object> proxyInfo(String userId) {
		logger.info("SignServiceImpl: proxyInfo");
		return signDao.proxyInfo(userId);
	}

	// 대리결재 정보 직급(비동기)
	@Override
	public String getMemberRank(String memberId) {
		logger.info("SignServiceImpl: getMemberRank");
        return signDao.getMemberRank(memberId);
	}

	// 로그인 시 대리결재자 확인
	@Override
	public boolean checkProxy(String userid) {
		logger.info("SignServiceImpl: checkProxy");
		return signDao.checkProxy(userid);
	}

	// 게시글 전체 리스트(대리결재자 정보 포함)
	@Override
	public List<BoardDto> signList(String userId, String userName, String userRank, Map<String, Object> proxyInfo) {
        logger.info("SignServiceImpl: signList");
        return signDao.signList(userId, userName, userRank, proxyInfo);
	}

	// 대리결재자 권한 유효성
	@Override
	public void validDate(String userId) {
		logger.info("SignServiceImpl: validDate");
		signDao.validDate(userId);
	}
}
