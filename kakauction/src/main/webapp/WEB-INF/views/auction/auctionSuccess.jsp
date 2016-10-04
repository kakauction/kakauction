<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../design/inc/top.jsp"%>
<script type="text/javascript" 
	src="<c:url value='/jquery/jquery-3.1.0.min.js'/>"></script>
<script type="text/javascript">	
	$(document).ready(function(){
		$(".divList .box2 tbody tr")
			.hover(function(){
				$(this).css("background","skyblue")
					.css("cursor","pointer");
			}, function(){
				$(this).css("background","");
			});
		
		$("#cancleAuction").click(function(){
			alert("거래 취소하신 경매입니다!");
		});

		$("#successAuction").click(function(){
			alert("결제 완료 하신 경매입니다!");
		});
	});
	function pageProc(curPage){
		document.frmPage.currentPage.value=curPage;
		document.frmPage.submit();
	}
</script>
<div id="wrap">
	
	<div id="wrapdiv">
		<div id="wraptop">
			<p>
				<a href="${pageContext.request.contextPath }/design/index.do">HOME</a>>나의낙찰현황
			</p>
		</div>
		<div id="pagelogo">
			<img
				src="${pageContext.request.contextPath }/img/auctionSuccess_logo.png"
				alt="회원가입로고">
		</div>
		<form name="frmPage" method="post" action="<c:url value='/auction/list.do'/>">
			<input type="hidden" name="currentPage"> <input type="hidden"
				name="searchCondition" value="${param.searchCondition }"> <input
				type="hidden" name="searchKeyword"
				value="${searchVO.searchKeyword }">
		</form>
		<!-- 페이징 처리를 위한 form 시작-->
		<div style="margin-top: 20px; margin-bottom: 20px;"><p>내가 참여한 경매 건 수 : ${alistsize }</p></div>
		<form name="frmPage" method="post">
			<input type="hidden" name="startDay" value="${param.startDay}">
			<input type="hidden" name="endDay" value="${param.endDay}">
			<input type="hidden" name="currentPage">	
		</form>
		
		<form name="frm1" method="post" id="frmOrderList" action="<c:url value='/auction/myAuctionList.do'/>" >
			<!-- 조회기간 include -->
			<%@include file="../design/inc/dateTerm.jsp" %>
			<input type="submit" value="조회" >
		</form>

		<div class="divList">
			<p>전체 조회 결과 - ${alistsize }건 조회되었습니다</p>
			<table class="box2">
				<caption>자료실</caption>
				<colgroup>
					<col style="width: 10%;" />
					<col style="width: 7%;" />
					<col style="width: 20%;" />
					<col style="width: 10%;" />
					<col style="width: 7%;" />
					<col style="width: 10%;" />
					<col style="width: 7%;" />
					<col style="width: 12%;" />
					<col style="width: 12%;" />
					<col style="width: 4%;" />
				</colgroup>
				<thead>
					<tr class="listTitle">
						<th rowspan="2" class="listImg">사진</th>
						<th rowspan="2" class="listSize">차종</th>
						<th>물건명</th>
						<th>연식</th>
						<th>변속기</th>
						<th rowspan="2">낙찰가</th>
						<th rowspan="2" class="readCount">조회</th>
					</tr>
					<tr>
						<th>소재지</th>
						<th>주행거리</th>
						<th>연료</th>
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
						<c:forEach var="map" items="${alist }">
							<tr style="text-align: center">
								<td class="listImg"><img alt="사진" height="56px;" width="90px;" src="<c:url value='/picture_upload/${map["PICTURE_1"]}'/>"></td>
								<td class="listSize">${map['CAR_SIZE']}</td>
								<c:if test="${map['LB_BUYCAR_YN']=='정상결제'}">								
									<td class="listName" style="text-align: left;">
											<a id="successAuction">
												${map['CAR_MODEL']}<br>
												<c:if test="${fn:length(map['CAR_LOC'])>30}">
													${fn:substring(map['CAR_LOC'], 0,30)}...
												</c:if> 
												<c:if test="${fn:length(map['CAR_LOC'])<=30}">
													${map['CAR_LOC']}
												</c:if><br>
											</a>
											<span style="color: fuchsia;">이미 거래 완료한 차량입니다.</span></td>
								</c:if>
								<c:if test="${map['LB_BUYCAR_YN']=='N'}">							
									<td class="listName" style="text-align: left;">
										<a class="auctionTitle" href="<c:url value='/delivery/detail.do?auctionNo=${map["AUCTION_NO"]}'/>">
												${map['CAR_MODEL']}<br>
											<c:if test="${fn:length(map['CAR_LOC'])>30}">
												${fn:substring(map['CAR_LOC'], 0,30)}...
											</c:if> 
											<c:if test="${fn:length(map['CAR_LOC'])<=30}">
												${map['CAR_LOC']}
											</c:if>
										</a>
									</td>
								</c:if>
								<c:if test="${map['LB_BUYCAR_YN']=='거래취소'}">							
									<td class="listName" style="text-align: left;">
										<a class="auctionTitle" id="cancleAuction">
												${map['CAR_MODEL']}<br>
											<c:if test="${fn:length(map['CAR_LOC'])>30}">
												${fn:substring(map['CAR_LOC'], 0,30)}...
											</c:if> 
											<c:if test="${fn:length(map['CAR_LOC'])<=30}">
												${map['CAR_LOC']}
											</c:if>
										</a><br>
										<span style="color: fuchsia;">거래 취소한 차량입니다.</span>
									</td>
								</c:if>
								<td>${map['CAR_BIRTH']}<br> ${map['CAR_DIST']}km
								</td>
								<td>${map['CAR_AM']}<br> ${map['CAR_GAS']}
								</td>
								<td><fmt:formatNumber pattern="#,###" value="${map['RECORD_PRICE']}"/>만원
								</td>
								<td class="readCount">${map['AUCTION_READ_COUNT']}</td>
							</tr>
						</c:forEach>
						<!--반복처리 끝  -->
					</c:if>
					
				</tbody>
			</table>
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
<%@ include file="../design/inc/bottom.jsp"%>