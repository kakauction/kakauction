<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../inc/top.jsp" %>
<script type="text/javascript" 
	src="<c:url value='/js/member.js'/>"></script>
	
<script type="text/javascript">
$(document).ready(function(){
	$("#wr_submit").click(function(event){
		if($("#name").val().length < 1){
			alert("이름을 입력하세요");
			$("#name").focus();
			return false;
		}else if(!validate_userid($("#userid").val())){
			alert("아이디는 영문이나 숫자 _만 가능합니다!");
			$("#userid").focus();
			return false;
		}else if($("#pwd").val().length<1){
			alert("비밀번호를 입력하세요");
			$("#pwd").focus();
			return false;
		}else if($("#pwd").val()!=$("#pwd2").val()){
			alert("비밀번호가 일치하지 않습니다.");
			$("#pwd2").focus();
			return false;
		}else if($("#chkId").val()!='Y'){
			alert("아이디 중복확인을 하셔야 합니다!");
			$("#btnChkId").focus();
			return false;
		}else if(!validate_hp($("#hp2").val()) || 
				!validate_hp($("#hp3").val())){
			alert("휴대폰 번호는 숫자여야 합니다");
			$("#hp2").focus();
			return false;
		}
	});//click
	
	$("#btnChkId").click(function(){
		var userid=$("#userid").val();
				
		window.open(
		"<c:url value='design/checkUserid.do?userid="
				+userid+"'/>",	"chkId",
"width=450,height=250,left=50,top=50,resizable=yes,location=yes");
	});
	$(function(){
		$("#email2 option[value='etc']").click(function () {
   			$("#email3").show();
		}
	});
});
	
	
	
</script>
<div id="wrap">
	<div id="registerwrap">
		<div id="registertop">
			<p>
				<a href="${pageContext.request.contextPath }/design/index.do">HOME</a>>회원정보수정
			</p>
		</div>
		<div id="pagelogo">
			<img src="${pageContext.request.contextPath }/img/edit_logo.png"
				alt="로그인로고">
		</div>

			
		<table id="registertable">
			<colgroup>
				<col width="120" />
				<col width="440" />
				<col width="140" />
				<col width="*" />
			</colgroup>
			<tbody>

				<tr>
					<th><label for="name">
					<img src="${pageContext.request.contextPath }/img/check.png" alt="" />성명(한글)</label></th>
					<td>
        				<input type="text" name="name" id="name" style="ime-mode:active; width: 120px;" value="" readonly="readonly">
					</td>
					<th><label for="userid">
					<img src="${pageContext.request.contextPath }/img/check.png" alt=""/>회원 ID</label></th>
					<td>
			      		<input type="text" name="userid" id="userid" style="ime-mode:inactive; width: 120px;" value="" readonly="readonly">&nbsp;
					</td>
				</tr>
				<tr>
					<th><label for="email1">
					<img src="${pageContext.request.contextPath }/img/check.png" alt="" />E-mail</label></th>
					<td>
					 
					<input type="text" name="email1" id="email1" title="이메일주소 앞자리" style="width: 120px;"> @
						<select name="email2" id="email2" title="이메일주소 뒷자리">
							<option value="naver.com">naver.com</option>
							<option value="hanmail.net">hanmail.net</option>
							<option value="nate.com">nate.com</option>
							<option value="gmail.com">gmail.com</option>
							<option value="etc">직접입력</option>
					</select> <input type="text" name="email3" id="email3"
						title="직접입력인 경우 이메일주소 뒷자리" style="visibility: hidden; width:100px"> </select></td>

					<th><label for="hp1">
					<img src="${pageContext.request.contextPath }/img/check.png" alt="" />핸드폰</label></th>
					<td>
						<select name="hp1" id="hp1" title="휴대폰 앞자리">
								<option value="010">010</option>
								<option value="011">011</option>
								<option value="016">016</option>
								<option value="017">017</option>
								<option value="018">018</option>
								<option value="019">019</option>
						</select> -
	
        				<input type="text" name="hp2" id="hp2" maxlength="4" title="휴대폰 가운데자리"
        				style="width: 70px;"> -

        	      		<input type="text" name="hp3" id="hp3" maxlength="4" title="휴대폰 뒷자리"
        				style="width: 70px;">
        			</td>
				</tr>
				<tr>
					<th><label for="zipcode">
					<img src="${pageContext.request.contextPath }/img/check.png" alt="" />주소</label></th>
					<td colspan="3">
						<ul class="address_ul">
							<li>
								<input type="text" name="zipcode" id="zipcode" ReadOnly title="우편번호" style="width: 120px;">
        						<input type="Button" value="우편번호 찾기" id="btnZipcode" title="새창열림" >
        					</li>
							<li id="address_li">
								<input type="text" name="address" id="address" ReadOnly title="주소" style="width:230px;">
							</li>
							<li id="address_li">
								<input type="text" name="addressDetail" title="상세주소" placeholder="상세주소" 
								 style="width: 320px;" required>
							</li>
						</ul>
					</td>
				</tr>

			</tbody>
		</table>
		<div id="btn_area">
			<ul>
				<li id="btn_register">
					<button type="submit" onclick="#">회원정보수정</button>
				</li>
				<li id="btn_cancel">
					<button type="submit" onclick="#">취소</button>
				</li>
			</ul>
		</div>
	</div>
</div>
 

<%@ include file="../inc/bottom.jsp"%>