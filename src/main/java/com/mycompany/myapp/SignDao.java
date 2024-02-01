package com.mycompany.myapp;

import java.util.List;
import java.util.Map;

public interface SignDao {
    MemberDto signLogin(String userid);

    int getMaxSeq();

    int signWrite(BoardDto writeboard);
    
    int signWriteUpd(BoardDto board, String userRank);

    List<BoardDto> signList(String userId, String userName, String userRank);

    BoardDto signDetail(int seq);

	List<BoardDto> signSearch(String userId, String userRank, String searchOption, String searchKeyword, String searchStatus, String startDate,
			String endDate);

	int signHistory(BoardDto board);

	List<HistoryDto> historyList(int boardSeq);

	List<MemberDto> delegatePage(String userRank);

	void delegateInsert(Map<String, Object> map);

	Map<String, Object> proxyInfo(String userId);


	boolean checkProxy(String userid);

	List<BoardDto> signList(String userId, String userName, String userRank, Map<String, Object> parameters);

	void validDate(String userId);

	String getMemberRank(String memberId);
}
