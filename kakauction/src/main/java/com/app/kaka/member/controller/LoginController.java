package com.app.kaka.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.app.kaka.common.Utility;
import com.app.kaka.member.model.MemberService;
import com.app.kaka.member.model.MemberVO;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static final Logger logger
		= LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public String login_get(){
		logger.info("로그인창입니다.");
		
		return "login/login";
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public String login_post(@ModelAttribute MemberVO memVo,
			@RequestParam(required=false) String chkSave, 
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		logger.info("로그인입력 파라미터 memberVo={}",memVo);
		
		String memberId = memVo.getMemberId();
		logger.info("memberId={}",memberId);
		
		String memberGrade = memberService.whereMemberGrade(memberId);
		logger.info("memberGrade={}",memberGrade);
		if ("BLACKLIST".equals(memberGrade)) {
			model.addAttribute("msg", "신고로 차단된 회원입니다.");
			model.addAttribute("url", "/login/login.do");
			
			return "common/message";
		}
		
		logger.info("아이디저장 chkSave={}",chkSave);
		
		String shaPwd = "";
		try {
			shaPwd = Utility.hashEncryption(memVo.getMemberPwd());
			logger.info("shaPwd={}",shaPwd);
			memVo.setMemberPwd(shaPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int result = memberService.loginCheck(memVo);
		logger.info("로그인 처리 결과, result={}",result);
		
		String msg="",url="/login/login.do";
		if (result==MemberService.LOGIN_OK) {
			//로그인 성공
			//[1] 세션에 저장
			MemberVO memberVo = memberService.selectMemberByUserid(memVo.getMemberId());

			HttpSession session = request.getSession();
			session.setAttribute("memberId", memVo.getMemberId());
			session.setAttribute("memberName", memberVo.getMemberName());
			session.setAttribute("memberGrade", memberVo.getMemberGrade());
			
			//[2] 쿠키에 저장
			Cookie ck = new Cookie("ck_userid", memVo.getMemberId());
			if (chkSave!=null) {
				//아이디 저장을 체크한 경우 => 쿠키에 저장
				ck.setMaxAge(1000*24*60*60); //쿠키 유효기간  - 1000일
				response.addCookie(ck);
			}else{
				ck.setMaxAge(0); //0주면 삭제가 됩니다.
				response.addCookie(ck);
			}
			
			msg=memberVo.getMemberName()+"님 로그인 되었습니다.";
			url="/index.do";
		}else if (result==MemberService.PWD_DISAGREE) {
			msg="비밀번호가 일치하지 않습니다.";
		}else if (result==MemberService.ID_NONE) {
			msg="해당 아이디가 존재하지 않습니다.";
		}else{
			msg="로그인 처리 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session, Model model){
		//1. 
		logger.info("로그아웃 처리");
		
		//2.
		//session.invalidate();
		session.removeAttribute("memberId");
		session.removeAttribute("memberName");
		session.removeAttribute("memberGrade");
		
		//3.
		model.addAttribute("msg", "로그아웃되었습니다.");
		model.addAttribute("url", "/index.do");
		
		return "common/message";
	}
}
