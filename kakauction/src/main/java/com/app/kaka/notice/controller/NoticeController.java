package com.app.kaka.notice.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.kaka.common.PaginationInfo;
import com.app.kaka.common.SearchVO;
import com.app.kaka.common.Utility;
import com.app.kaka.notice.model.NoticeService;
import com.app.kaka.notice.model.NoticeVO;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping(value = "/write.do", method = RequestMethod.GET)
	public String noticeWrite_get(){
		logger.info("글쓰기 페이지");
		
		return "notice/write";
	}
	
	@RequestMapping(value = "/write.do", method = RequestMethod.POST)
	public String noticeWrite_post(HttpServletRequest request, 
				@ModelAttribute NoticeVO vo){
		//글쓰기 처리 - post
		//1. 파라미터 읽어오기
		logger.info("글쓰기 처리, 파라미터 noticeVo={}", vo);
		
		//2. db작업 - insert
		//[1] 파일업로드 처리하기
		List<Map<String, Object>> fileList = noticeService.fileupload(request);
		
		String fileName = "";		
		String ofileName = "";
		long fileSize = 0;
		for(Map<String, Object> myMap : fileList){
			fileName = (String) myMap.get("noticeFilename");
			ofileName = (String) myMap.get("noticeOriginalname");
			fileSize = (Long) myMap.get("noticeFilesize");
		}//for
		
		vo.setNoticeFilename(fileName);
		vo.setNoticeOriginalname(ofileName);
		vo.setNoticeFilesize(fileSize);
		
		//[2] db에 insert하기
		/*
		reBoard.xml => ReBoardDAO => ReBoardDAOMybatis
		=> ReBoardService => ReBoardServiceImpl
		=> ReBoardWriteController  
		*/
		logger.info("글쓰기 처리, 파라미터 noticeVo={}", vo);
		int cnt = noticeService.insertNotice(vo);
		logger.info("글쓰기 결과, cnt={}", cnt);
		
		//3. 결과저장, 뷰페이지 리턴
		return "redirect:/notice/list.do";
	}
	
	@RequestMapping("/list.do")
	public String listNotice(@ModelAttribute SearchVO searchVo,
			Model model){
logger.info("글목록 조회, 파라미터 searchVo={}", searchVo);
		
		PaginationInfo pagingInfo = new PaginationInfo();
		pagingInfo.setBlockSize(Utility.BLOCK_SIZE);
		pagingInfo.setRecordCountPerPage(Utility.RECORD_COUNT_PER_PAGE);
		pagingInfo.setCurrentPage(searchVo.getCurrentPage());
		
		searchVo.setBlockSize(Utility.BLOCK_SIZE);
		searchVo.setRecordCountPerPage(Utility.RECORD_COUNT_PER_PAGE);
		searchVo.setFirstRecordIndex(pagingInfo.getFirstRecordIndex());
				
		//2. db작업 - select
		List<NoticeVO> alist = noticeService.selectAll(searchVo);
		logger.info("글목록 조회 결과 alist.size()={}", 
				alist.size());
		
		//전체 레코드 개수 조회하기
		int totalRecord 
			= noticeService.selectTotalCount(searchVo);
		pagingInfo.setTotalRecord(totalRecord);
				
		//3. 결과 저장, 뷰페이지 리턴
		model.addAttribute("alist", alist);
		model.addAttribute("pagingInfo", pagingInfo);
		
		return "notice/list";
	}
}