package com.mycompany.myapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SignDaoImpl implements SignDao {
    Logger logger = LoggerFactory.getLogger(SignDaoImpl.class);
    private final SqlSession sqlSession;

    @Autowired
    public SignDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    
    // 로그인
    @Override
    public MemberDto signLogin(String userid) {
        logger.info("SignDaoImpl: signLogin");
        return sqlSession.selectOne("selectMemberByUserId", userid);
    }

    // 게시글 최대 번호
    @Override
    public int getMaxSeq() {
        return sqlSession.selectOne("getMaxSeq");
    }

    // 글쓰기
    @Override
    public int signWrite(BoardDto writeboard) {
        logger.info("SignDaoImpl: signWrite");
        return sqlSession.insert("selectInsert", writeboard);
    }
    
    // 글수정
    @Override
    public int signWriteUpd(BoardDto board, String userRank) {
        logger.info("SignDaoImpl: signWriteUpd");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("board", board);
        paramMap.put("userRank", userRank);
        
        System.out.println("게시글 수정 paramMap 확인 : ");
        System.out.println("apperId : " + board.getApperId());

        return sqlSession.update("selectUpdate", paramMap);
    }

    // 게시글 전체 리스트
	@Override
	public List<BoardDto> signList(String userId, String userName, String userRank, Map<String, Object> parameters) {
        logger.info("SignDaoImpl: signList");

        // 대리결재 권한 없는 경우 Null 체크
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        parameters.put("userId", userId);
        parameters.put("userName", userName);
        parameters.put("userRank", userRank);

        System.out.println("parameters 값 확인 : " + parameters);
        return sqlSession.selectList("selectList", parameters);
	}
    
    @Override
    public List<BoardDto> signList(String userId, String userName, String userRank) {
        logger.info("SignDaoImpl: signList");

        Map<String, String> parameters = new HashMap<>();
        
        parameters.put("userId", userId);
        parameters.put("userName", userName);
        parameters.put("userRank", userRank);

        //logger.info("userId: {}", userId);
        //logger.info("userName: {}", userName);
        //logger.info("userRank: {}", userRank);
        return sqlSession.selectList("selectList", parameters);
    }
    

    // 게시글 상세 조회
    @Override
    public BoardDto signDetail(int seq) {
        logger.info("SignDaoImpl: signDetail");
        return sqlSession.selectOne("selectDetail", seq);
    }

    // 게시글 검색
	@Override
	public List<BoardDto> signSearch(String userId, String userRank, String searchOption, String searchKeyword, String searchStatus, String startDate,
			String endDate) {
		logger.info("SignDaoImpl: signSearch");

		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("userId", userId);
		searchMap.put("userRank", userRank);
		searchMap.put("searchOption", searchOption);
		searchMap.put("searchKeyword", searchKeyword);
		searchMap.put("searchStatus", searchStatus);
		searchMap.put("startDate", startDate);
		searchMap.put("endDate", endDate);
		return sqlSession.selectList("selectSearch", searchMap);
	}

	// 게시글 결재 상태 히스토리 등록
	@Override
	public int signHistory(BoardDto board) {
		logger.info("SignDaoImpl: signHistory");
		return sqlSession.insert("selectHistoryInsert", board);
	}

	// 게시글 결재 상태 히스토리 리스트
	@Override
	public List<HistoryDto> historyList(int boardSeq) {
		logger.info("SignDaoImpl: historyList");
		return sqlSession.selectList("selectHistoryList", boardSeq);
	}

	// 대리결재자 리스트
	@Override
	public List<MemberDto> delegatePage(String userRank) {
		logger.info("SignDaoImpl: delegatePage");
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("userRank", userRank);
	    return sqlSession.selectList("selectDelegateList", paramMap);
	}

	// 대기결재 데이터 등록
	@Override
	public void delegateInsert(Map<String, Object> map) {
		logger.info("SignDaoImpl: delegateInsert");
		sqlSession.insert("selectDelegateInsert", map);
	}
	
	// 대리결재 정보
	@Override
	public Map<String, Object> proxyInfo(String userId) {
		logger.info("SignDaoImpl: proxyInfo");
		return sqlSession.selectOne("selectProxyInfo", userId);
	}

	@Override
	public boolean checkProxy(String userid) {
		logger.info("SignDaoImpl: checkProxy");
	    Boolean isProxy = sqlSession.selectOne("checkProxy", userid);
	    return isProxy != null && isProxy;
	}


	@Override
	public void validDate(String userId) {
		logger.info("SignDaoImpl: validDate");
		sqlSession.delete("selectDeleteProxy", userId);
	}


	@Override
	public String getMemberRank(String memberId) {
		logger.info("SignDaoImpl: getMemberRank");
		return sqlSession.selectOne("selectGetMemberRank", memberId);
	}
}
