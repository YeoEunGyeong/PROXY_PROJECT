package com.mycompany.myapp;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

public interface SignService {
    MemberDto signLogin(String userid, String password) throws AuthenticationException;

    int getMaxSeq();

    int signWrite(BoardDto writeboard);

    int signWriteUpd(BoardDto board, String userRank);
    
    List<BoardDto> signList(String userId, String userName, String userRank);

    BoardDto signDetail(int seq);

	List<BoardDto> signSearch(String userId, String userRank, String searchOption, String searchKeyword, String searchStatus, String startDate,
			String endDate);

	int signHistory(BoardDto board);

	List<HistoryDto> historyList(int Boardseq);

	List<MemberDto> delegatePage(String userRank);

	void delegateInsert(Map<String, Object> map);

	Map<String, Object> proxyInfo(String userId);

	String getMemberRank(String memberId);

	boolean checkProxy(String userid);

	List<BoardDto> signList(String userId, String userName, String userRank, Map<String, Object> proxyInfo);

	void validDate(String userId);

}
