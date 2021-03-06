package com.app.kaka.freeboard.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.app.kaka.common.FileUploadWebUtil;
import com.app.kaka.common.PaginationInfo;
import com.app.kaka.common.SearchVO;
import com.app.kaka.common.Utility;
import com.app.kaka.freeboard.model.FreeboardService;
import com.app.kaka.freeboard.model.FreeboardVO;
import com.app.kaka.freeboardreport.model.FreeboardreportVO;
import com.app.kaka.notice.model.NoticeVO;

@Controller
@RequestMapping("/freeboard")
public class FreeboardController {
	private Logger logger = LoggerFactory.getLogger(FreeboardController.class);
	
	@Autowired
	private FileUploadWebUtil webUtil;
	
	@Autowired
	private FreeboardService freeboardService;
	
	@RequestMapping(value="/write.do", method=RequestMethod.GET)
	public String wirte_get(){
		logger.info("글 쓰기 화면 보여주기");
		return "freeboard/write";
	}
	@RequestMapping(value="/write.do", method=RequestMethod.POST)
	public String wirte_post(HttpServletRequest request, @ModelAttribute FreeboardVO freeboardVo){
		//글쓰기 처리 - post
		//1. 파라미터 읽어오기
		logger.info("글쓰기 처리, 파라미터 freeboardVo={}", freeboardVo);
		
		//2. db작업 - insert
		//[1]파일 업로드 처리하기
		List<Map<String, Object>> fileList = webUtil.fileUpload(request, webUtil.FREEBOARD_UPLOAD);
		//[2]db에 insert하기
		String fileName="";
		String ofileName="";
		long fileSize=0;
		for(Map<String, Object> myMap : fileList){
			fileName = (String)myMap.get("fileName");
			ofileName=(String)myMap.get("ofileName");
			fileSize = (Long)myMap.get("fileSize");
			
		}//for
		freeboardVo.setFreeboardFilename(fileName);
		freeboardVo.setFreeboardOriginalname(ofileName);
		freeboardVo.setFreeboardFilesize(fileSize);
		
		logger.info("vo를 알고 싶어요={}",freeboardVo);
		/*
		reBoard.xml => ReBoardDAO => ReBoardDAOMybatis
		=> ReBoardService => ReBoardServiceImpl
		=> ReBoardWriteController  
		*/

		
		int cnt = freeboardService.insertFreeboard(freeboardVo);
		logger.info("글쓰기 결과, cnt={}", cnt);
		
		//3. 결과저장, 뷰페이지 리턴
		return "redirect:/freeboard/list.do";
	}
/*	@RequestMapping("/list.do")
	public String listFreeboard(){
		
	}*/
	@RequestMapping("updateCount.do")
	public String updateCountFreeboard(@RequestParam(defaultValue="0") int freeboardNo, HttpServletRequest request, Model model){
		logger.info("조회수 증가 파라미터, freeboardNo={}", freeboardNo);
		if (freeboardNo==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "/freeboard/list.do");
			
			return "common/message";
		}
		
		int cnt = freeboardService.updateReadCount(freeboardNo);
		logger.info("조회수 증가 파라미터, cnt={}", cnt);
		
		return "redirect:/freeboard/detail.do?freeboardNo="+freeboardNo;
	}
	
	@RequestMapping("/detail.do")
	public String detailFreeboard(@ModelAttribute SearchVO searchVo, @RequestParam(defaultValue="20") int selectedCountPerPage,
			@RequestParam(defaultValue="0") int freeboardNo, HttpSession session,
			HttpServletRequest request, Model model){
		logger.info("글 상세목록 파라미터 freeboardNo={}",freeboardNo);
		
		if (freeboardNo==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "/freeboard/list.do");
			
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
		//디테일에 사용된것
		model.addAttribute("freeVo", freeVo);
		model.addAttribute("fileInfo", fileInfo);
		model.addAttribute("downInfo", downInfo);
		
		//디테일에 리스트 뽑기
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
		int totalRecord 
			= freeboardService.selectTotalCount(searchVo);
		pagingInfo.setTotalRecord(totalRecord);
		
		//3. 결과 저장, 뷰페이지 리턴
		model.addAttribute("alist", alist);
		model.addAttribute("pagingInfo", pagingInfo);
		model.addAttribute("selectedCountPerPage", selectedCountPerPage);
		
		return "/freeboard/detail";
	}
	
	@RequestMapping("/list.do")
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
		int totalRecord 
			= freeboardService.selectTotalCount(searchVo);
		pagingInfo.setTotalRecord(totalRecord);
		
		//3. 결과 저장, 뷰페이지 리턴
		model.addAttribute("alist", alist);
		model.addAttribute("pagingInfo", pagingInfo);
		model.addAttribute("selectedCountPerPage", selectedCountPerPage);
		
		return "freeboard/list";
	}
	
	@RequestMapping(value="/edit.do", method=RequestMethod.GET)
	public String editFreeboard_get(@RequestParam(defaultValue="0") int freeboardNo, Model model){
		
		logger.info("글수정 화면 보여주기, 파라미터 freeboardNo={}",freeboardNo);
		if(freeboardNo==0){
			model.addAttribute("msg", "잘못된 url입니다");
			model.addAttribute("url", "/freeboard/list.do");
			return "common/message";
		}
		
		//2.
		FreeboardVO freeboardVO = freeboardService.selectByNo(freeboardNo);
		
		//3.
		model.addAttribute("freeboardVO", freeboardVO);
		return "freeboard/edit";
	}
	@RequestMapping(value="/edit.do", method=RequestMethod.POST)
	public String editFreeboard_post(HttpServletRequest request, @ModelAttribute FreeboardVO freeboardVo, 
			@RequestParam String freeboardFilename, Model model){
		
		logger.info("글수정 처리, 파라미터 FreeboardVO={}",freeboardVo);

		//상품등록시 이미지 업로드
		logger.info("글 수정 처리 왜 안되냐, request={},uploadType={}",request,webUtil.FREEBOARD_UPLOAD);
		List<Map<String, Object>> fileList = webUtil.fileUpload(request, webUtil.FREEBOARD_UPLOAD);
		logger.info("상품 등록 처리 얘는 뭐냐, fileList={}",fileList);
		//업로드된 파일명 구해오기
		if(freeboardFilename!=null && !freeboardFilename.isEmpty()){
			String fileName="";
			String ofileName="";
			long fileSize=0;
			for(Map<String, Object> mymap : fileList ){
				fileName =  (String) mymap.get("fileName");
				fileSize =  (Long) mymap.get("fileSize");
				ofileName =  (String) mymap.get("ofileName");
				
				freeboardVo.setFreeboardFilename(fileName);
				freeboardVo.setFreeboardFilesize(fileSize);
				freeboardVo.setFreeboardOriginalname(ofileName);
				logger.info("세터 다 하고 FreeboardVO={}",freeboardVo);
			}
		}
		
		//파일이 첨부된 경우에는 파일도 삭제처리한다
		if(freeboardFilename!=null && !freeboardFilename.isEmpty()){
			String upPath=webUtil.getUploadPath(request, webUtil.FREEBOARD_UPLOAD);
			File delFile = new File(upPath ,freeboardFilename);
			if(delFile.exists()){
				boolean bool=delFile.delete();
				logger.info("파일 삭제 여부:{}", bool);					
			}
		}//if
				
		String msg = "", url ="";
		int cnt = freeboardService.updateFreeboard(freeboardVo);
		logger.info("게시글 등록 결과 cnt={}",cnt);
		if(cnt>0){
			msg="글 수정 성공";
			url="/freeboard/list.do";
		}else{
			msg="글 수정 실패";
			url="/freeboard/edit.do";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";

	}
	
	@RequestMapping("/delete.do")
	public String deleteNotice(HttpServletRequest request,@ModelAttribute FreeboardVO freeboardVo,
			@RequestParam String freeboardFilename,Model model){
		logger.info("공지 삭제 파라미터 freeboardVo={},freeboardFilename={}",freeboardVo.getFreeboardNo(), freeboardFilename);
		
		if (freeboardVo.getFreeboardNo()==0) {
			model.addAttribute("msg", "잘못된 url입니다.");
			model.addAttribute("url", "/freeboard/list.do");
			
			return "common/message";
		}
		
		//저장 프로시저에서 사용할 map 만들기
		Map<String, String> map 
			= new HashMap<String, String>();
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
		
		return "redirect:/freeboard/list.do";
	}
	
	@RequestMapping("/download.do")
	public ModelAndView download(@RequestParam(defaultValue="0") int freeboardNo, @RequestParam String freeboardFilename,
			HttpServletRequest request,Model model){
		logger.info("다운로드 파라미터, noticeNo={},fileName={}",freeboardNo, freeboardFilename);
		
		int cnt = freeboardService.updateDownCount(freeboardNo);
		logger.info("다운로드 수 증가 freeboardNo={}",freeboardNo);
		
		//3.
		//다운로드할 파일정보를 이용해서 파일 객체를 
		//만든 후 뷰에 넘긴다
		
		//ModelAndView(String viewName, Map map)		
		Map<String, Object> map 
			= new HashMap<String, Object>();
		String upPath = webUtil.getUploadPath(request,webUtil.FREEBOARD_UPLOAD);
		
		File file = new File(upPath, freeboardFilename);
		//생성한 파일 객체를 map에 저장한 후 뷰에 넘긴다
		map.put("myfile", file);
		
		ModelAndView mav = new ModelAndView("downloadView", map);
		
		return mav;
	}
	
	@RequestMapping("/likeBoard.do")
	public String likeBoard(@RequestParam String memberId, @RequestParam int freeboardNo, Model model){
		logger.info("좋아요 파라미터 memberId={}, auctionNo={}", memberId, freeboardNo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberId", memberId);
		map.put("freeboardNo", freeboardNo);
		
		int cnt = freeboardService.myLikeCount(map);
		String msg="", url="/freeboard/detail.do?freeboardNo="+freeboardNo;
		if(cnt<1){
			int result = freeboardService.likeBoard(map);
			msg="추천하셨습니다";
		}else{
			msg="이미추천하셨습니다";
		}
		int likeCnt = freeboardService.boardLikeCount(freeboardNo);
		if(likeCnt>9){
			int recnt = freeboardService.bestFreeboardcnt(freeboardNo);
			if(recnt<1){
				recnt = freeboardService.insertbestFreeboard(freeboardNo);
			}
		}
		
		model.addAttribute("msg",msg);
		model.addAttribute("url",url);
		
		return "common/message";
	}
	
}
