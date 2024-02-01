package com.mycompany.myapp;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class BoardListDto {
    
    private int seq;
    private String mem_name;
    private String mem_id;
    private String board_subject;
    private String board_content;
    private Date reg_date;
    private Date upd_date;
    private String view_cnt;
    private String useyn;
    
    // 검색 기능 추가 시 필요 변수
    private String search; // 검색창
    private String searchBar; // 검색타입
    private String startDate;
    private String endDate;
    
    // 페이징 처리 시 필요 변수
    private int pageNo; 
    private int pageSize;
    private int startNo;
    private int endNo;
    private int totalCount;
    private int totalPage;
    private int startPage;
    private int endPage;
    
    // 파일 업로드 관련 필드 추가
    private MultipartFile file;
    private int file_seq;
    private int list_seq;
    private String real_name;
    private String save_name;
    private String save_path;

    // Getter methods
    public int getSeq() {
        return seq;
    }

    public String getMem_name() {
        return mem_name;
    }

    public String getMem_id() {
        return mem_id;
    }

    public String getBoard_subject() {
        return board_subject;
    }

    public String getBoard_content() {
        return board_content;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public Date getUpd_date() {
        return upd_date;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public String getUseyn() {
        return useyn;
    }
    
    public String getSearch() {
        return search;
    }
    
    public String getSearchBar() {
        return searchBar;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartNo() {
        return startNo;
    }

    public int getEndNo() {
        return endNo;
    }
    
    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }
    
    public MultipartFile getFile() {
        return file;
    }
    
    public int getFile_seq() {
        return file_seq;
    }
    
    public int getList_seq() {
        return list_seq;
    }

    public String getReal_name() {
        return real_name;
    }

    public String getSave_name() {
        return save_name;
    }

    public String getSave_path() {
        return save_path;
    }

    // Setter methods
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public void setBoard_subject(String board_subject) {
        this.board_subject = board_subject;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public void setUpd_date(Date upd_date) {
        this.upd_date = upd_date;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }

    public void setUseyn(String useyn) {
        this.useyn = useyn;
    }
    
    public void setSearch(String search) {
    	this.search = search;
    }
    
    public void setSearchBar(String searchBar) {
    	this.searchBar = searchBar;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setStartNo(int startNo) {
        this.startNo = startNo;
    }

    public void setEndNo(int endNo) {
        this.endNo = endNo;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
    
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
    public void setFile_seq(int file_seq) {
        this.file_seq = file_seq;
    }

    public void setList_seq(int list_seq) {
        this.list_seq = list_seq;
    }
    
    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public void setSave_name(String save_name) {
        this.save_name = save_name;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }
}