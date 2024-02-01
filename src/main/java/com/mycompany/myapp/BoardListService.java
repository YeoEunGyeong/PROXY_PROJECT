package com.mycompany.myapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardListService {
	Logger logger = LoggerFactory.getLogger(BoardListService.class);
	BoardListDao bDao;

	@Autowired
	public BoardListService(BoardListDao bDao) {
		this.bDao = bDao;
	}

	// 게시글 목록
	public List<BoardListDto> boardList(int currentPage, int pageSize) {
		logger.info("boardList 호출");
	    int startNo = (currentPage - 1) * pageSize + 1;
	    int endNo = currentPage * pageSize;
	    return bDao.boardList(startNo, endNo);
	}

	// 게시글 검색
	public List<BoardListDto> boardSearch(String search, String searchBar, String startDate, String endDate, int currentPage, int pageSize) {
		logger.info("boardSearch 호출");
		int startNo = (currentPage - 1) * pageSize + 1;
	    int endNo = currentPage * pageSize;
	    BoardListDto board = new BoardListDto();
	    return bDao.boardSearch(board, search, searchBar, startDate, endDate, startNo, endNo);
	}
																																											
	// 전체 페이지 수를 반환하는 메소드 추가
	public int getTotalPages(boolean isSearch, String search, String searchBar, String startDate, String endDate, int pageSize) {
	    int totalCount;
	    if (isSearch) {
	        totalCount = bDao.getTotalCountForSearch(new BoardListDto(), search, searchBar, startDate, endDate);
	    } else {
	        totalCount = bDao.getTotalCountForList();
	    }
	    return (int) Math.ceil((double) totalCount / pageSize);
	}
	
	// 게시글 등록
	public List<BoardListDto> boardInsert(BoardListDto board) {
		logger.info("boardInsert 호출");
		bDao.boardInsert(board);
		return bDao.boardList(0, 0);
	}
	
	// 게시글 파일 등록
	public void boardFileInsert(String real_name, String save_name, String save_path, int list_seq) {
		logger.info("boardFileInsert 호출");
		logger.info(real_name);
		logger.info(save_name);
		logger.info(save_path);

		BoardListDto board = new BoardListDto();
		board.setReal_name(real_name);
		board.setSave_name(save_name);
		board.setSave_path(save_path);
		board.setList_seq(list_seq);
		bDao.boardFileInsert(board);
	}

	// 게시글 상세(단일 행을 불러오기 때문에 List 사용하지 않아도 됨)
	public BoardListDto boardDetail(int seq) {
		logger.info("boardDetail 호출");
		return bDao.boardDetail(seq);
	}

	// 게시글 상세(파일 조건이 있을 시)
	public List<BoardListDto> boardFileDetail(int seq) {
	    logger.info("boardFileDetail 호출");
	    return bDao.boardFileDetail(seq);
	}

	// 게시글 수정
	@Transactional
	public BoardListDto boardUpdate(BoardListDto board) {
		logger.info("boardUpdate 호출");
		bDao.boardUpdate(board);
		return board;
	}

	// 게시글 삭제
	@Transactional
	public void boardDelete(List<Long> selectedBoards) {
		logger.info("boardDelete 호출");
		bDao.boardDelete(selectedBoards);
	}
}
