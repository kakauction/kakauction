package com.app.kaka.admin.freeboard.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.kaka.common.FileUploadWebUtil;
import com.app.kaka.common.PaginationInfo;
import com.app.kaka.common.SearchVO;
import com.app.kaka.common.Utility;
import com.app.kaka.freeboard.model.FreeboardService;
import com.app.kaka.freeboard.model.FreeboardVO;

@Controller
@RequestMapping("/admin/freeboard")
public class AdminFreeboardController {
	private Logger logger = LoggerFactory.getLogger(AdminFreeboardController.class);
	
	@Autowired
	private FreeboardService freeboardService;
	
	@Autowired
	private FileUploadWebUtil webUtil;
	
	@RequestMapping("/freeboardList.do")
	public String freeboardList(@ModelAttribute SearchVO searchVo, @RequestParam(defaultValue="20") int selectedCountPerPage,
			Model model){
		logger.info("글목록 조회, 파라미터 searchVo={}", searchVo);
		
		PaginationInfo pagingInfo = new PaginationInfo();
		pagingInfo.setBlockSize(Utility.BLOCK_SIZE);
		pagingInfo.setRecordCountPerPage(selectedCountPerPage);
		pagingInfo.setCurrentPage(searchVo.getCurrentPage());
		
		searchVo.setBlockSize(Utility.BLOCK_SIZE);
		searchVo.setRecordCountPerPage(selectedCountPerPage);
		searchVo.setFirstRecordIndex(pagingInfo.getFirstRecordIndex());
		
		//2. db작업 - select
		List<FreeboardVO> alist = freeboardService.selectAll(searchVo);
		//logger.info("글목록 조회 결과 alist.size()={}", alist.size());
		
		//전체 레코드 개수 조회하기
		int totalRecord = freeboardService.selectTotalCount(searchVo);
		pagingInfo.setTotalRecord(totalRecord);
		
		//3. 결과 저장, 뷰페이지 리턴
		model.addAttribute("alist", alist);
		model.addAttribute("pagingInfo", pagingInfo);
		model.addAttribute("selectedCountPerPage", selectedCountPerPage);
		
		return "admin/freeboard/freeboardList";
	}
	
	@RequestMapping("/updateCount.do")
	public String updateCountFreeboard(@RequestParam(defaultValue="0") int freeboardNo, HttpServletRequest request, Model model){
		logger.info("조회수 증가 파라미터, freeboardNo={}", freeboardNo);
		if (freeboardNo==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "admin/freeboard/freeboardList.do");
			
			return "common/message";
		}
		
		int cnt = freeboardService.updateReadCount(freeboardNo);
		logger.info("조회수 증가 파라미터, cnt={}", cnt);
		
		return "redirect:/admin/freeboard/freeboardDetail.do?freeboardNo="+freeboardNo;
	}
	
	@RequestMapping("/freeboardDetail.do")
	public String detailFreeboard(@RequestParam(defaultValue="0") int freeboardNo,HttpSession session,
			HttpServletRequest request, Model model){
		logger.info("글 상세목록 파라미터 freeboardNo={}",freeboardNo);
		
		if (freeboardNo==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "/admin/freeboard/freeboardList.do");
			
			return "common/message";
		}
		
		FreeboardVO freeVo = freeboardService.detailFreeboard(freeboardNo);
		logger.info("글 상세목록 freeboardVo = {}", freeVo);
		
		String fileInfo="", downInfo="";
		if(freeVo.getFreeboardFilename()!=null 
				&& !freeVo.getFreeboardFilename().isEmpty()){
			String contextPath = request.getContextPath();
			double fileSize 
					= Math.round((freeVo.getFreeboardFilesize()/1000.0)*10)/10.0;
			
			fileInfo="<img src='"+ contextPath 
					+"/image/file.gif' alt='파일이미지'>";
			fileInfo+=freeVo.getFreeboardOriginalname()
					+ " ("+fileSize +"KB)";
			
			downInfo="다운:"+freeVo.getFreeboardDowncount();
		}
		
		model.addAttribute("freeVo", freeVo);
		model.addAttribute("fileInfo", fileInfo);
		model.addAttribute("downInfo", downInfo);
		
		return "/admin/freeboard/freeboardDetail";
	}
	
	@RequestMapping("/freeboardDelete.do")
	public String deleteNotice(HttpServletRequest request,@ModelAttribute FreeboardVO freeboardVo,
			@RequestParam String freeboardFilename,Model model){
		logger.info("공지 삭제 파라미터 freeboardVo={},freeboardFilename={}",freeboardVo.getFreeboardNo(), freeboardFilename);
		
		if (freeboardVo.getFreeboardNo()==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "/freeboard/list.do");
			
			return "common/message";
		}
		
		//저장 프로시저에서 사용할 map 만들기
		Map<String, String> map = new HashMap<String, String>();
		map.put("freeboardNo", freeboardVo.getFreeboardNo()+"");
		map.put("freeboardGroupno", freeboardVo.getFreeboardGroupno()+"");
		map.put("freeboardStep", freeboardVo.getFreeboardStep()+"");
		logger.info("글삭제시 파라미터 map={}", map);
		
		int cnt = freeboardService.deleteFreeboard(map);
		
		//파일이 첨부된 경우에는 파일도 삭제처리한다
		if(freeboardFilename!=null && !freeboardFilename.isEmpty() && cnt>1){
			String upPath=webUtil.getUploadPath(request, webUtil.FREEBOARD_UPLOAD);
			File delFile = new File(upPath ,freeboardFilename);
			if(delFile.exists()){
				boolean bool=delFile.delete();
				logger.info("파일 삭제 여부:{}", bool);					
			}
		}//if
		
		logger.info("공지 삭제 결과, cnt={}",cnt);
		
		return "redirect:/admin/freeboard/freeboardList.do";
	}
}
