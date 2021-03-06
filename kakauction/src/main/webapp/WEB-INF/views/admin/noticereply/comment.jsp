<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<title>Insert title here</title>
<script type="text/javascript" src="<c:url value='/jquery/jquery-3.1.0.min.js'/>"></script>
<script type="text/javascript">	
	function pageProc(curPage){
		document.frmPage.currentPage.value=curPage;
		document.frmPage.submit();
	}
</script>
</head>
<body>
	<form name="frmPage" method="post" action="<c:url value='/admin/notice/detail.do?noticeNo=${noticeVo.noticeNo}'/>">
		<input type="hidden" name="currentPage">
	</form>
	<div class="comment">
		<h3>댓글</h3><br>
		<table class="box2"
	 		summary="게시판 댓글에 대한 표로써, 작성자, 내용, 작성일시에 대한 정보를 제공합니다." style="width: 500px">
	 		<colgroup>
				<col style="width:20%;" />
				<col style="width:60%;" />
				<col style="width:20%;" />
			</colgroup>
			<thead>
				<tr>
				    <th scope="col">작성자</th>
				    <th scope="col">내용</th>
				    <th scope="col">작성일시</th>
				</tr>
			</thead>
			<c:if test="${empty alist }">
				<tr>
					<td colspan="3" class="align_center">등록된 댓글이 없습니다.</td>
				</tr>
			</c:if>
			<c:if test="${!empty alist }">
				<c:forEach var="notireplyVo" items="${alist }">
					<tr>
						<td style="text-align: center">${notireplyVo.memberId }</td>
						<td>${notireplyVo.notireplyContent }</td>
						<td style="text-align: center"><fmt:formatDate value="${notireplyVo.notireplyRegdate }" pattern="yyyy-MM-dd"/></td>
					</tr>
				</c:forEach>
				<!--반복처리 끝  -->
			  </c:if>
	 	</table>
	 	<div class="divPage">
			<!-- 이전 블럭으로 이동 -->
			<c:if test="${pagingInfo.firstPage>1 }">	
				<a href="#" onclick="pageProc(${pagingInfo.firstPage-1})">
					<img src="<c:url value='/image/first.JPG'/>" 
							alt="이전블럭으로">
				</a>	
			</c:if>
			
			<!-- 페이지 번호 추가 -->						
			<!-- [1][2][3][4][5][6][7][8][9][10] -->
			<c:forEach var="i" begin="${pagingInfo.firstPage }" 
				end="${pagingInfo.lastPage }">	 
				<c:if test="${i==pagingInfo.currentPage }">
					<span style="color:blue;font-weight: bold">
						${i }</span>
				</c:if>		
				<c:if test="${i!=pagingInfo.currentPage }">
						<a href="#" onclick="pageProc(${i})">
						[${i}]</a>
				</c:if>
			</c:forEach>
			<!--  페이지 번호 끝 -->
			
			<!-- 다음 블럭으로 이동 -->
			<c:if test="${pagingInfo.lastPage<pagingInfo.totalPage }">
				<a href="#" 
				onclick="pageProc(${pagingInfo.lastPage+1})">
					<img src="<c:url value='/image/last.JPG'/>"
							alt="다음블럭으로">
				</a>
			</c:if>
		</div>
	</div>
	<form id="replyComment" name="replyComment" method="post" action="<c:url value='/admin/notireply/insertComment.do'/>">
		<div class="commentWrite" style="width: 500px; margin-top: 20px;">
			<fieldset>
				<legend>댓글달기</legend>
					<p style="margin-top: 10px;">
						<label for="memberId" style="margin-right: 10px;">작성자</label>
						<input type="text" id="memberId" name="memberId" style="width: 30%"  value="${sessionScope.memberId }" readonly>
					</p>
					<input type="hidden" name="notireplyGroupno" value="${noticeVo.noticeNo}">
					<p style="margin-top: 20px;">
						<textarea rows="5" cols="95" name="notireplyContent"></textarea>
					</p>
					<div style="text-align: center; margin-top: 10px;">
						<input type="submit" name="commentSubmit" value="확인">
					</div>
			</fieldset>
		</div>
	</form>
	
	<%-- <c:if test="${noticeVo.prevNoticeNo!='0' } ">
		<a href="<c:url value='/notice/updateCount.do?noticeNo=${noticeVo.prevNoticeNo}'/>">이전글</a> | 
		<a href="<c:url value='/notice/updateCount.do?noticeNo=${noticeVo.prevNoticeNo}'/>">${noticeVo.prevNoticeTitle }</a> ${noticeVo.prevNoticeNo} | ${!(noticeVo.prevNoticeNo!='0') }
	</c:if>
	<br>
	<c:if test="${noticeVo.nextNoticeNo!='0' } ">
		<a href="<c:url value='/notice/updateCount.do?noticeNo=${noticeVo.nextNoticeNo}'/>">다음글</a> | 
		<a href="<c:url value='/notice/updateCount.do?noticeNo=${noticeVo.nextNoticeNo}'/>">${noticeVo.nextNoticeTitle }</a> | ${noticeVo.nextNoticeNo}
	</c:if> --%>
	<c:if test="${noticeVo.nextNoticeNo!='0' }"> 
		<a href="<c:url value='/admin/notice/updateCount.do?noticeNo=${noticeVo.nextNoticeNo}'/>">다음글</a> |
		<a href="<c:url value='/admin/notice/updateCount.do?noticeNo=${noticeVo.nextNoticeNo}'/>">${noticeVo.nextNoticeTitle }</a>
	</c:if>
	<br>
	<c:if test="${noticeVo.prevNoticeNo!='0' }">
		<a href="<c:url value='/admin/notice/updateCount.do?noticeNo=${noticeVo.prevNoticeNo}'/>">이전글</a> | 
		<a href="<c:url value='/admin/notice/updateCount.do?noticeNo=${noticeVo.prevNoticeNo}'/>">${noticeVo.prevNoticeTitle }</a>
	</c:if>
</body>
</html>