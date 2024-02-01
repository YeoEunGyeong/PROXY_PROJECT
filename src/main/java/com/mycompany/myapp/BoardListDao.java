package com.mycompany.myapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardListDao {
	Logger logger = LoggerFactory.getLogger(BoardListDao.class);
	private SqlSession sqlSession;

	@Autowired
	public BoardListDao(SqlSession sqlSession) {
		logger.info("sql 호출");
		this.sqlSession = sqlSession;
	}

	// 게시글 목록
	public List<BoardListDto> boardList(int startNo, int endNo) {
		logger.info("boardList 호출");
		Map<String, Object> lMap = new HashMap<>();
		lMap.put("startNo", startNo);
		lMap.put("endNo", endNo);
		return sqlSession.selectList("selectBoardList", lMap);
	}

	// 게시글 검색
	public List<BoardListDto> boardSearch(BoardListDto board, String search, String searchBar, String startDate,
			String endDate, int startNo, int endNo) {
		logger.info("boardSearch 호출");
		logger.info(startDate);
		logger.info(endDate);
		Map<String, Object> sMap = new HashMap<>();
		sMap.put("search", search);
		sMap.put("searchBar", searchBar);
		sMap.put("startDate", startDate);
		sMap.put("endDate", endDate);
		sMap.put("startNo", startNo);
		sMap.put("endNo", endNo);
		return sqlSession.selectList("selectBoardSearch", sMap);
	}

	// 게시글 목록 - 총 레코드 수 반환
	public int getTotalCountForList() {
	    return sqlSession.selectOne("totalCountForList");
	}

	// 게시글 검색 - 총 레코드 수 반환
	public int getTotalCountForSearch(BoardListDto board, String search, String searchBar, String startDate, String endDate) {
	    logger.info("getTotalCountForSearch 호출");
	    Map<String, Object> sMap = new HashMap<String, Object>();
	    sMap.put("search", search);
	    sMap.put("searchBar", searchBar);
	    sMap.put("startDate", startDate);
	    sMap.put("endDate", endDate);
	    return sqlSession.selectOne("totalCountForSearch", sMap);
	}
	
	// 게시글 등록
	public BoardListDto boardInsert(BoardListDto board) {
		logger.info("boardInsert 호출");
		sqlSession.insert("selectBoardInsert", board);
		return board;
	}
	
	// 게시글 파일 등록
	public void boardFileInsert(BoardListDto board) {
		logger.info("boardFileInsert 호출");
		sqlSession.insert("boardFileInsert", board);
	}

	// 게시글 상세
	public BoardListDto boardDetail(int seq) {
		logger.info("boardDetail 호출");
		return sqlSession.selectOne("selectBoardDetail", seq);
	}

	// 게시글 상세(파일 조건이 있을 시)
	public List<BoardListDto> boardFileDetail(int seq) {
	    logger.info("boardFileDetail 호출");
	    return sqlSession.selectList("selectBoardFileDetail", seq);
	}
	
	// 게시글 수정
	public void boardUpdate(BoardListDto board) {
		logger.info("boardUpdate 호출");
		sqlSession.update("selectBoardUpdate", board);
	}

	// 게시글 삭제
	public void boardDelete(List<Long> selectedBoards) {
		logger.info("boardDelete 호출");
		sqlSession.delete("selectBoardDelete", selectedBoards);
	}
}
