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
		
		$("#countPerPage").change(function(){
			$("#selectedCountPerPage").val($("#countPerPage").val());
			$("#currentPage").val(1);
			$("#frmPage").submit();
		});
		
	});

	function pageProc(curPage){ 
		document.frmPage.currentPage.value=curPage;
		document.frmPage.selectedCountPerPage.value=${selectedCountPerPage };
		document.frmPage.submit();
	}
	
	function showMessage(no, type){
		if(type=="1" || type=="2"){
			window.open("/kaka/msg/detail.do?msgNo="+no,
					"detail",
			"width=455,height=500,left=700,top=200,resizable=yes,location=yes");
		}else{
			window.open("/kaka/alert/detail.do?alertNo="+no,
					"detail",
			"width=455,height=500,left=700,top=200,resizable=yes,location=yes");
		}
	}

</script>
<style type="text/css">
</style>
<div id="wrap">
	<div id="loginwrap">
		<div id="logintop">
			<p>
				<a href="${pageContext.request.contextPath }/design/index.do">HOME</a>> 받은쪽지함
			</p>
		</div>
		<div id="pagelogo">
			<img src="${pageContext.request.contextPath }/img/msg_logo.png" alt="받은쪽지로고">
		</div>
	
		<!-- http://localhost:9090/mymvc/reBoard
		/list.do?currentPage=5&searchCondition=content&searchKeyword=%ED%95%98 -->
		<form name="frmPage" id="frmPage" method="post" action="<c:url value='/msg/myList.do'/>">
			<input type="hidden" id = "currentPage" name="currentPage" value="${searchVo.currentPage }">
			<input type="hidden" name="searchCondition" value="${param.searchCondition }">
			<input type="hidden" name="searchKeyword" value="${searchVO.searchKeyword }">	
			<input type="hidden" id="selectedCountPerPage" name="selectedCountPerPage">
		</form>
		
		<div class="divList">
		<c:if test="${!empty param.searchKeyword }">
			<!-- 검색의 경우 -->
			<p>검색어 : ${param.searchKeyword }, 
				${pagingInfo.totalRecord }건 검색되었습니다.</p>
		</c:if>
		<c:if test="${empty searchVO.searchKeyword }">
			<!-- 전체 조회의 경우 -->
			<p>전체 조회 결과 
				- ${pagingInfo.totalRecord }건 조회되었습니다</p>
		</c:if>
		<select name="countPerPage" id="countPerPage" style="width:100px; font-size: 1.1em;" title="페이지당 갯수">
			<option value="20"
				<c:if test="${selectedCountPerPage=='20' }">
					selected
				</c:if>>20개씩 보기</option>
			<option value="30"
				<c:if test="${selectedCountPerPage=='30' }">
					selected
				</c:if>>30개씩 보기</option>
			<option value="50"
				<c:if test="${selectedCountPerPage=='50' }">
					selected
				</c:if>>50개씩 보기</option>
			<option value="100"
				<c:if test="${selectedCountPerPage=='100' }">
					selected
				</c:if>>100개씩 보기</option>
		</select>
		<table class="box2"
			 	summary="받은 신고 쪽지에 관한 표">
			<caption>받은 신고 쪽지함</caption>
			<colgroup>
				<col style="width:25%;" />
				<col style="width:50%;" />
				<col style="width:15%;" />
				<col style="width:10%;" />		
			</colgroup>
			<thead>
			  <tr>
			    <th scope="col">쪽지 유형</th>
			    <th scope="col">제목</th>
			    <th scope="col">쪽지 수신일</th>
			    <th scope="col">확인여부</th>
			  </tr>
			</thead> 
			<tbody>  
			<c:if test="${empty alist}">
				<tr>
					<td colspan="6" class="align_center">
					해당 데이터가 없습니다
					</td>
				</tr>
			</c:if>
			<c:if test="${!empty alist}">
				<!--게시판 내용 반복문 시작  -->
				<c:forEach var="vo" items="${alist }">
					<tr style="text-align: center">
						<td>
							<c:if test="${vo['TYPE'] == '1'}">
								자유게시판 신고
							</c:if>
							<c:if test="${vo['TYPE'] == '2'}">
								경매 차량 신고
							</c:if>
							<c:if test="${vo['TYPE'] == 'DENY'}">
								차량 등록 거부
							</c:if>
							<c:if test="${vo['TYPE'] == 'DEFER'}">
								경매 등록 보류
							</c:if>
							<c:if test="${vo['TYPE'] == 'AGAIN'}">
								경매 재등록
							</c:if>
						</td>
						<td style="text-align: left;">
							<a onclick="showMessage(${vo['MSG_NO']},'${vo['TYPE'] }')">
								<!-- 제목이 긴 경우 일부만 보여주기 -->
								<c:if test="${fn:length(vo['TITLE'])>30}">
									${fn:substring(vo['TITLE'], 0,30)}...
								</c:if>
								<c:if test="${fn:length(vo['TITLE'])<=30}">
									${vo['TITLE']}
								</c:if>
							</a>
						</td>
						<td><fmt:formatDate value="${vo['REGDATE']}"
							pattern="yyyy-MM-dd"/>
						</td>
						<td>${vo['CONFIRM'] }</td>
					</tr>				
				</c:forEach>
				<!--반복처리 끝  -->
			</c:if>
			</tbody>
		</table>	   
		</div>
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
		<div class="divSearch">
		   	<form name="frmSearch" method="post" 
		   	action="<c:url value='/msg/myList.do' />" >
		        <select name="searchCondition">
		            <option value="title"
		           	   <c:if test="${param.searchCondition=='title'}">
		            		selected
		               </c:if>
		            >제목</option>
		            <option value="content" 
		            	<c:if test="${param.searchCondition=='content'}">
		            		selected
		               </c:if>
		            >내용</option>
		        </select>   
		        <input type="text" name="searchKeyword" 
		        	title="검색어 입력" value="${param.searchKeyword}" >   
				<input type="submit" value="검색">
		    </form>
		</div>

	</div>
</div>
<%@ include file="../design/inc/bottom.jsp"%>