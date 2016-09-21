<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../design/inc/top.jsp"%>
<script type="text/javascript" src="<c:url value='/jquery/jquery-3.1.0.min.js'/>"></script>
<script type="text/javascript">	
	$(document).ready(function(){
		$(".divList .box2 tbody tr").hover(function(){
				$(this).css("background","skyblue")
					.css("cursor","pointer");
			}, function(){
				$(this).css("background","");
			});
		
 		$(".accordion").click(function() {
			$( this ).toggleClass( "active" );
			$( this ).next().toggleClass( "show" );
		});
		
		$(".fixedOpen").click(function() {
			$(this).css("display", "none");
		});
		$(".closebtn").click(function() {
			$(".fixedOpen").fadeIn(900);
			/* $(".fixedOpen").css("display","block"); */
		});
	});
	function pageProc(curPage){
		document.frmPage.currentPage.value=curPage;
		document.frmPage.submit();
	}
	
	function openNav() {
		document.getElementById("mySidenav").style.width = "40%";
		document.getElementById("slideWrap").style.marginLeft = "350px";
	}

	function closeNav() {
		document.getElementById("mySidenav").style.width = "0";
		document.getElementById("slideWrap").style.marginLeft = "0";
	}
	
	$( document ).ready( function() {
		  var Offset = $( '.offset' ).offset();
		  $( window ).scroll( function() {
		    if ( $( document ).scrollTop() > Offset.top ) {
		      $( '.sidenav' ).addClass( 'searchFixed' );
		    }
		    else {
		      $( '.sidenav' ).removeClass( 'searchFixed' );
		    }
		});
	});
	
	

</script>

<style type="text/css">
.specialList{
	width: 100%;
	height: 100px;
	margin-top: 20px;
}
.specialList ul li{
	float: left;
	margin-left: 10px;
}
.specialList ul li a{
	text-decoration: none;
}
.first{
	margin-left: 10px;
}
.imgbox img{
	width: 180px;
}
.txt{
	text-decoration: none;
}
.box2 tbody tr td a:hover{
	text-decoration: underline;
	color: blue;
}
</style>
 		<!--상세검색 -->
		<%@ include file="../detailSearch/detailSearch.jsp" %>
		<!--상세검색 -->
		<span onclick="openNav()" class="fixedOpen"><img src="<c:url value='/img/sangsae.png'/>"></span>
		<div id="slideWrap">
<div id="wrap">
	<div id="wrapdiv">
		<div id="wraptop">
			<div class="offset"></div>
			<p>
					<a href="${pageContext.request.contextPath }/design/index.do">HOME</a>>경매리스트
			</p>
		</div>
		<div id="pagelogo">
			<img src="${pageContext.request.contextPath }/img/auctionList_logo.png"
				alt="경매리스트로고">
		</div>
		
		<div class="specialList">
			<ul>
				<li class="first">
					<a href="">
						<div class="imgbox">
							<img src="${pageContext.request.contextPath }/img/test_img1.jpg"
									alt="자동차사진" />
						</div><span class="txt">기아 뉴 쏘렌토R 2013년형</span></a>
				</li>
				<li>
					<a href="">
						<div class="imgbox">
							<img src="${pageContext.request.contextPath }/img/test_img2.jpg"
										alt="자동차사진" />
						</div><span class="txt">쉐보레(국산) 캡티바 2012년식</span></a>
				</li>
				<li>
					<a href="">
						<div class="imgbox">
							<img src="${pageContext.request.contextPath }/img/test_img3.jpg"
								alt="자동차사진" />
						</div><span class="txt">현대 제네시스 2012년형</span></a>
				</li>
				<li>
					<a href="">
						<div class="imgbox">
							<img src="${pageContext.request.contextPath }/img/test_img4.jpg"
								alt="자동차사진" />
						</div><span class="txt">현대 제네시스 쿠페 2010년형</span></a>
				</li>
				<li class="last">
					<a href="">
					<div class="imgbox">
						<img src="${pageContext.request.contextPath }/img/test_img5.jpg"
							alt="자동차사진" />
					</div> <span class="txt">현대 아반떼MD 2011년형</span></a>
				</li>
			</ul>
		</div>
		<br><br><hr>
		<form name="frmPage" method="post" action="<c:url value='/auction/list.do'/>">
			<input type="hidden" name="currentPage"> <input type="hidden"
				name="searchCondition" value="${param.searchCondition }"> <input
				type="hidden" name="searchKeyword"
				value="${searchVO.searchKeyword }">
		</form>

		<div class="divList">
			<c:if test="${!empty param.searchKeyword }">
				<p>검색어 : ${param.searchKeyword }, ${pagingInfo.totalRecord }건
					검색되었습니다.</p>
			</c:if>
			<c:if test="${empty searchVO.searchKeyword }">
				<p>전체 조회 결과 - ${pagingInfo.totalRecord }건 조회되었습니다</p>
			</c:if>
			<table class="box2"
				summary="자료실에 관한 표로써, 번호, 제목, 작성자, 작성일, 조회수에 대한 정보를 제공합니다.">
				<caption>자료실</caption>
				<colgroup>
					<col style="width: 4%;" />
					<col style="width: 14%;" />
					<col style="width: 20%;" />
					<col style="width: 8%;" />
					<col style="width: 7%;" />
					<col style="width: 7%;" />
					<col style="width: 6%;" />
					<col style="width: 9%;" />
					<col style="width: 9%;" />
					<col style="width: 4%;" />
				</colgroup>
				<thead>
					<tr class="listTitle">
						<th colspan="10">
							<div class="carListDiv">
								<ul>
									<li><a style="cursor: pointer;" class="on" onclick=""><span>전체</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">경차</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">소형</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">준중형</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">중형</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">대형</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">스포츠카</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">SUV</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">RV</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">승합차</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">밴</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">화물차</a></li>
									<li><a style="cursor: pointer;" class="" onclick="">버스</a></li>
									<li class="more_btn"><button class="more_btn"></button></li>
								</ul>
							</div>
						</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty alist}">
						<tr>
							<td colspan="10" class="align_center">해당 데이터가 없습니다</td>
						</tr>
					</c:if>
					<c:if test="${!empty alist}">
						<!--게시판 내용 반복문 시작  -->
						<c:forEach var="vo" items="${alist }">
							<tr style="text-align: center">
								<c:if test="${vo.auctionState=='END'}">
									<td colspan="9"><a href="#" style="color: gray;">이미 종료된 경매 입니다</a></td>
								</c:if>
								<c:if test="${vo.auctionState!='END'}">
								<td class="listCheckbox"><input type="checkbox" id="checklist_1497871" value="1497871" /></td>
								<td class="listImg"><img alt="사진" height="100px;" width="150px;" src="<c:url value='/picture_upload/${vo.picture1}'/>"></td>
								<td class="listName" style="text-align: left;">
									<a class="auctionTitle${countTd }" href="<c:url value='/auction/updateCount.do?auctionNo=${vo.auctionNo}'/>">
											${vo.carModel}<br>
									</a>
									</td>
								<td>${vo.carBirth}</td>
								<td>${vo.carDist}km</td>
								<td>${vo.carFailSell}</td>
								<td>${vo.auctionState}</td>
								<td>${vo.auctionRegdate}</td>
								<td>${vo.auctionFinish }</td>
								</c:if>
								<td class="readCount">${vo.auctionReadCount }</td>
							</tr>
						</c:forEach>
						<!--반복처리 끝  -->
					</c:if>
				</tbody>
			</table>
		</div>
		<div class="gototop">
			<a href="#" onclick="">맨위로&#9652;</a>
		</div>
		
		<div class="pagediv">
			<ul class="page">
				<!-- 이전 블럭으로 이동 -->
				<li class="firstPage"><c:if test="${pagingInfo.firstPage>1 }">
					<a href="#" onclick="pageProc(${pagingInfo.firstPage-1})">&laquo;</a>
				</c:if></li>
				<!-- 페이지 번호 추가 -->
				<!-- [1][2][3][4][5][6][7][8][9][10] -->
				<li><c:forEach var="i" begin="${pagingInfo.firstPage }" end="${pagingInfo.lastPage }">
					<c:if test="${i==pagingInfo.currentPage }">
						<span> ${i }</span>
					</c:if>
					<c:if test="${i!=pagingInfo.currentPage }">
						<a href="#" onclick="pageProc(${i})"> ${i}</a>
					</c:if>
				</c:forEach></li>
				<!--  페이지 번호 끝 -->
	
				<!-- 다음 블럭으로 이동 -->
				<li><c:if test="${pagingInfo.lastPage<pagingInfo.totalPage }">
					<a href="#" onclick="pageProc(${pagingInfo.lastPage+1})">&raquo;</a>
				</c:if></li>
			</ul>
		</div>
	</div>
</div>
</div>
<%@ include file="../design/inc/bottom.jsp"%>