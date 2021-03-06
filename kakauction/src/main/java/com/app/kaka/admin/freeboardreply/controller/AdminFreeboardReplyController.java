package com.app.kaka.admin.freeboardreply.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.kaka.common.PaginationInfo;
import com.app.kaka.common.SearchVO;
import com.app.kaka.common.Utility;
import com.app.kaka.freereply.model.FreeReplyService;
import com.app.kaka.freereply.model.FreeReplyVO;

@Controller
@RequestMapping("/admin/freeboardreply")
public class AdminFreeboardReplyController {
	private Logger logger = LoggerFactory.getLogger(AdminFreeboardReplyController.class);
	
	@Autowired
	private FreeReplyService freereplyService;
	
	@RequestMapping("/insertComment.do")
	public String insertComment(@ModelAttribute FreeReplyVO vo, Model model){

		logger.info("댓글 달기, 파라미터 FreeReplyVO={}", vo);
		
		int cnt = freereplyService.insertComment(vo);
		logger.info("댓글달기 결과 cnt = {}", cnt);
		
		String msg = "", url = "/admin/freeboard/detail.do?freeboardNo="+vo.getFreereplyGroupno();
		if(cnt>0){
			msg = "댓글을 달았습니다!";
		}else{
			msg = "댓글달기 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	@RequestMapping("/comment.do")
	public String showComment(@ModelAttribute SearchVO searchVo, Model model){
		logger.info("댓글 보기, 파라미터 searchVo={}", searchVo);

		PaginationInfo pagingInfo = new PaginationInfo();
		pagingInfo.setBlockSize(Utility.BLOCK_SIZE);
		pagingInfo.setRecordCountPerPage(Utility.REPLY_RECORD_COUNT_PER_PAGE);
		pagingInfo.setCurrentPage(searchVo.getCurrentPage());
		
		searchVo.setBlockSize(Utility.BLOCK_SIZE);
		searchVo.setRecordCountPerPage(Utility.REPLY_RECORD_COUNT_PER_PAGE);
		searchVo.setFirstRecordIndex(pagingInfo.getFirstRecordIndex());
		
		List<FreeReplyVO> alist = freereplyService.selectComment(searchVo);
		logger.info("댓글 조회 결과, alist.size() = {}", alist.size());
		
		//전체 레코드 개수 조회하기
		int totalRecord = freereplyService.selectTotalCount(searchVo);
		pagingInfo.setTotalRecord(totalRecord);
				
		//3. 결과 저장, 뷰페이지 리턴
		model.addAttribute("alist", alist);
		model.addAttribute("pagingInfo", pagingInfo);
		
		return "admin/freeboardreply/comment";
	}
}
