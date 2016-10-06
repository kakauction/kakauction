package com.app.kaka.member.controller;

import javax.servlet.http.HttpSession;

import org.aspectj.weaver.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.kaka.common.Utility;
import com.app.kaka.member.model.MemberService;
import com.app.kaka.member.model.MemberVO;


@Controller
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;

	@RequestMapping("/agreement.do")
	public String agreement(){
		logger.info("약관동의 화면 보여주기");
		
		return "member/agreement";
	}
	
	@RequestMapping("/register.do")
	public String register(){
		logger.info("회원가입 페이지 보여주기");
		
		return "member/register";
	}
	
	@RequestMapping("/pwdCheck.do")
	public String pwdCheck(){
		logger.info("비밀번호 확인 페이지 보여주기");
		
		return "member/pwdCheck";
	}
	
	@RequestMapping("/memberAdd.do")
	public String memberAdd(@ModelAttribute MemberVO memberVo,@RequestParam String hp1, @RequestParam String hp2,
			@RequestParam String hp3, @RequestParam String jumin1, @RequestParam String jumin2,
			@RequestParam String email1, @RequestParam String email2, @RequestParam String email3,
			Model model){
		logger.info("회원가입 memberVo={}",memberVo);
		logger.info("주민 2 jumin2={}",jumin2);
		
		String memberHp = memberVo.getMemberHp();
		if (memberHp==null || memberHp.isEmpty()) {
			memberVo.setMemberHp(hp1+"-"+hp2+"-"+hp3);
		}
		
		String memberJumin = memberVo.getMemberJumin();
		
		if (memberJumin==null || memberJumin.isEmpty()) {
			memberVo.setMemberJumin(jumin1+"-"+jumin2);
		}
		
		if (email2.equals("etc")){
			memberVo.setMemberEmail(email1+"@"+email3);
		}else{
			memberVo.setMemberEmail(email1+"@"+email2);
		}
		
		
		String jumin = memberVo.getMemberJumin();
		logger.info("주민번호 중복 확인, 파라미터 jumin={}",jumin);

		int result = memberService.checkMemberJumin(jumin);
		logger.info("주민번호 중복확인 결과, result={}",result);
		
		try {
			String shaPwd = Utility.hashEncryption(memberVo.getMemberPwd());
			memberVo.setMemberPwd(shaPwd);
			logger.info("비밀번호 암호화 후 memberVo={}",memberVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String msg="",url="";
		if (result==MemberService.NONE_EXIST_JUMIN) {
			int cnt = memberService.insertMember(memberVo);
			logger.info("회원가입 결과 cnt={}",cnt);
			
			if(cnt>0){
				url="/member/registerCheck.do?memberId="+memberVo.getMemberId();
			}else{
				msg="가입 실패!";
				url="/member/register.do";
			}
		}else{
			msg="이미 가입하신 회원입니다.로그인 하세요";
			url="/login/login.do";
		}
		
		model.addAttribute("msg",msg);
		model.addAttribute("url",url);
		
		return "common/message";
	}
	
	@RequestMapping("/ajaxCheckUserid.do")
	@ResponseBody
	public int ajaxCheckId(@RequestParam String memberId){
		logger.info("ajax-아이디 중복확인, 파라미터 userid={}",memberId);
		
		int result = memberService.checkMemberId(memberId);
		logger.info("ajax아이디 중복확인 결과, result={}",result);
		//해당 아이디가 존재하면 1, 존재하지 않으면 2를 리턴
		
		return result;
	}
	
	@RequestMapping("/registerCheck.do")
	public String registerOk(@RequestParam String memberId, Model model){
		logger.info("회원 가입 성공!");
		
		model.addAttribute("memberId", memberId);
		
		return "member/registerCheck";
	}
	
	@RequestMapping(value="/memberCheck.do", method=RequestMethod.GET)
	public String memberCheck_get(HttpSession session, Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 수정 전 확인 페이지 memberId={}",memberId);
		
		model.addAttribute("memberId", memberId);
		
		return "member/pwdCheck";
	}
	
	@RequestMapping(value="/memberCheck.do", method=RequestMethod.POST)
	public String memberCheck_post(@ModelAttribute MemberVO memberVo,HttpSession session,Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 수정 아이디 memberId={}",memberId);
		
		int result = memberService.loginCheck(memberVo);
		
		String msg="",url="/member/pwdCheck.do";
		if (result==MemberService.LOGIN_OK) {
			url="/member/memberEdit.do";
		}else if (result==MemberService.PWD_DISAGREE) {
			msg="비밀번호가 틀렸습니다";
		}else{
			msg="비밀번호 체크 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	@RequestMapping(value="/pwdCheck2.do", method=RequestMethod.GET)
	public String pwdCheck2_get(HttpSession session, Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 수정 전 확인 페이지 memberId={}",memberId);
		
		model.addAttribute("memberId", memberId);
		
		return "member/pwdCheck2";
	}
	
	@RequestMapping(value="/pwdCheck2.do", method=RequestMethod.POST)
	public String pwdCheck2_post(@ModelAttribute MemberVO memberVo,HttpSession session,Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 수정 아이디 memberId={}",memberId);
		
		int result = memberService.loginCheck(memberVo);
		
		String msg="",url="/member/pwdCheck2.do";
		if (result==MemberService.LOGIN_OK) {
			url="/fing/pwd_Edit.do";
		}else if (result==MemberService.PWD_DISAGREE) {
			msg="비밀번호가 틀렸습니다";
		}else{
			msg="비밀번호 체크 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	@RequestMapping(value="/memberEdit.do", method=RequestMethod.GET)
	public String memberEdit_get(HttpSession session ,Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 수정창 보여주기, userid={}",memberId);
		
		MemberVO memVo = memberService.selectMemberByUserid(memberId);
		
		//이메일 나누기
		String email = memVo.getMemberEmail();
		int find1 = email.indexOf("@");
		String email1 = email.substring(0, find1);
		String email2 = email.substring(find1+1);
		
		//핸드폰 나누기
		String hp = memVo.getMemberHp();
		int find2 = hp.indexOf("-");
		int find3 = hp.lastIndexOf("-");
		String hp1 = hp.substring(0, find2);
		String hp2 = hp.substring(find2+1, find3);
		String hp3 = hp.substring(find3+1);

		//뿌린 값을 모델로 넣어넣어
		model.addAttribute("email1", email1);
		model.addAttribute("email2", email2);
		model.addAttribute("hp1", hp1);
		model.addAttribute("hp2", hp2);
		model.addAttribute("hp3", hp3);
		
		model.addAttribute("memVo", memVo);
		logger.info("보여주기 결과 memVo={}",memVo);
		
		return "member/memberEdit";
	}
	
	@RequestMapping(value="/memberEdit.do", method=RequestMethod.POST)
	public String memberEdit_post(@ModelAttribute MemberVO memberVo,@RequestParam String hp1, 
			@RequestParam String hp2,@RequestParam String hp3,@RequestParam String email1, 
			@RequestParam String email2, @RequestParam String email3, HttpSession session, Model model){
		logger.info("널 보고싶다");
		//아이디 받아와
		String memberId = (String)session.getAttribute("memberId");
		memberVo.setMemberId(memberId);
		logger.info("세션으로 아이디를 받았지요 memberId={}",memberId);
		
		String memberHp = memberVo.getMemberHp();
		if (memberHp==null || memberHp.isEmpty()) {
			memberVo.setMemberHp(hp1+"-"+hp2+"-"+hp3);
		}
		
		if (email2.equals("etc")){
			memberVo.setMemberEmail(email1+"@"+email3);
		}else{
			memberVo.setMemberEmail(email1+"@"+email2);
		}
		
		String msg="",url="/member/memberEdit.do";
		int cnt = memberService.updateMember(memberVo);	
		logger.info("회원수정 파라미터 memberVo={}",memberVo);
		if (cnt>0) {
			msg="수정 성공!";
			
			logger.info("수정 결과 cnt={}",cnt);
		} else {
			msg="수정 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	
	
	@RequestMapping(value="/memberOut.do", method=RequestMethod.GET)
	public String memberOut_get(HttpSession session, Model model){
		logger.info("회원 탈퇴 화면 입니다.");
		String memberId = (String)session.getAttribute("memberId");
		logger.info("탈퇴하려는 회원 아이디 memberId={}",memberId);
		
		model.addAttribute("memberId", memberId);
		
		return "member/memberOut";
	}
	
	@RequestMapping(value="/memberOut.do", method=RequestMethod.POST)
	public String memberOut_post(@ModelAttribute MemberVO memberVo, HttpSession session, Model model){
		String memberId = (String)session.getAttribute("memberId");
		logger.info("회원 탈퇴 아이디 memberId={}",memberId);
		
		int result = memberService.loginCheck(memberVo);
		
		String msg="",url="/member/memberOut.do";
		if (result==MemberService.LOGIN_OK) {
			int cnt = memberService.memberOut(memberId);
			if (cnt>0) {
				msg="회원탈퇴 성공";
				url="/index.do";
				
				session.invalidate();
			}else{
				msg="회원탈퇴 실패";
			}
		}else if (result==MemberService.PWD_DISAGREE) {
			msg="비밀번호가 틀렸습니다";
		}else{
			msg="비밀번호 체크 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
	
	@RequestMapping("/myInfo.do")
	public String myInfo(){
		return "member/myInfo";
	}
}
