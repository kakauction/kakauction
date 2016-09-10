<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../design/inc/top.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		var highMember = "";
		
		setInterval(newHighPrice,1000);
		
		function newHighPrice(){
			$.ajax({
				url : "<c:url value='/auction/rankAuction.do'/>",
				type : "GET",
				dataType : "json",
				success : function(highVo) {
					highMember=highVo.buyerMemberId;
					$("#nowHighPrice").html(highVo.recordPrice+ "만원<br>"+highVo.buyerMemberId).css("text-align","right");
				},
				error : function(xhr,status,error) {
					alert("에러=>"+ status+"message=>"+xhr.responseText+ ":"+ error);
				}
			});
		}
		$("#goAuction").click(function(){
			var auctionNo= $("#auctionNo").val();
			var carNum=$("#carNum").val();
			var sellerMemberId=$("#sellerMemberId").val();
			var buyerMemberId=$("#byuerMemberId").val();
			var auctionmap = {"auctionNo":$("#auctionNo").val(),"recordPrice":50,
					"carNum":$("#carNum").val(),"sellerMemberId":$("#sellerMemberId").val(),"buyerMemberId":$("#byuerMemberId").val()};
			alert("auctionNo="+auctionmap["auctionNo"]+",recordPrice="+auctionmap["recordPrice"]+
					",carNum="+auctionmap["carNum"]+"sellerMemberId="+auctionmap["sellerMemberId"]+
					",buyerMemberId="+auctionmap["buyerMemberId"]);
			$.ajax({
				url : "<c:url value='/auction/insertAuctionGo.do'/>",
				data : {"auctionNo":auctionNo,"recordPrice":50,	"carNum":carNum,"sellerMemberId":sellerMemberId,"buyerMemberId":buyerMemberId},
				type : "POST",
				dataType : "json",
				success : function(highVo) {
					if(highMember==buyerMemberId){
						alert("현재 최고가를 응찰하신 회원입니다.");
					}
					$("#highPrice").attr("value",highVo.recordPrice);
				},
				error : function(xhr,status,error) {
					alert("에러=>"+ status+"message=>"+xhr.responseText+ ":"+ error);
				}
			});
		});
	});
</script>
<style type="text/css">
	table, td{
		border: 1px solid red;
	}
</style>
	<div>
	<input type="hidden" id="auctionNo" value="${auctionGo['AUCTION_NO'] }">
	<input type="hidden" id="carNum" value="${auctionGo['CAR_NUM'] }">
	<input type="hidden" id="sellerMemberId" value="${auctionGo['SELLER_MEMBER_ID'] }">
	<input type="hidden" id="highPrice" value="${auctionGo['AUCTION_FIRSTPRICE'] }">
	<input type="hidden" id="byuerMemberId" value="${memberId }">
		<table>
			<thead>
				<tr>
					<td><img alt="logo" src="<c:url value='/img/logo.png'/>"></td>
					<td><a href="#"><img alt="관심경매" src="<c:url value='/img/auctionChoice.png'/>"></a></td>
					<td><img alt="관심경매" src="<c:url value='/img/blank.png'/>"></td>
					<td>${memberId }</td>
					<td>경매 나가기</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="3">
						<table>
							<tr>
								<th colspan="6">${auctionGo['CAR_NUM'] }/${auctionGo['CAR_MODEL'] }(${auctionGo['CAR_SIZE'] })</th>
							</tr>
							<tr>
								<td>년식</td>
								<td>${auctionGo['CAR_BIRTH'] }</td>
								<td>주행거리</td>
								<td><fmt:formatNumber pattern="#,###" value="${auctionGo['CAR_DIST'] }"/>km</td>
								<td rowspan="2">특이</td>
								<td rowspan="2">특이점이 뭘까</td>
							</tr>
							<tr>
								<td>변속기</td>
								<td>${auctionGo['CAR_AM'] }</td>
								<td>연료</td>
								<td>${auctionGo['CAR_GAS'] }</td>
							</tr>
							<tr>
								<td>차량매매가격</td>
								<td><fmt:formatNumber pattern="#,###" value="${auctionGo['CAR_PRICE'] }"/>원</td>
								<td>변경</td>
								<td>변경쓰는곳</td>
								<td>여기는</td>
								<td>크엉</td>
							</tr>
							<tr>
								<td colspan="4">${auctionGo['PICTURE1'] }</td>
								<td colspan="2">${auctionGo['PICTURE2'] }</td>
							</tr>
						</table>
					</td>
					<td colspan="2">
						<table>
							<tr>
								<td><input type="button" value="후상담 낙찰결과"></td>
							</tr>
							<tr>
								<td>
									<a href="#">정보 1-cardetailinfo</a><br>
									<a href="#">정보 2-sellmemberinfo</a><br>
									<a href="#">정보 3-auctioninfo</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5">
						<table>
							<tr>
								<td rowspan="2">경매번호<br> 
									${auctionGo['AUCTION_NO_YEAR'] } - ${auctionGo['AUCTION_NO_CAR'] } -${auctionGo['AUCTION_NO'] }
								</td>
								<td colspan="3">경매상태</td>
								<td>
									<p style="text-align: center;">현재 입찰가</p>
									<p style="text-align: right;">억천백십일만원</p>
								</td>
								<td>
									권리-내가 최고입찰가면 불이 띵동
								</td>
								<td rowspan="2">
									<input id="goAuction" type="button" value="응찰">
								</td>
							</tr>
							<tr>
								<td>1</td>
								<td>2</td>
								<td>3</td>
								<td id="nowHighPrice"></td>
								<td>낙찰</td>
							</tr>
						</table>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
<%@ include file="../design/inc/bottom.jsp" %>
