<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.handyfarm.dao.HandyFarmDAO" %>
<%@ page import="com.handyfarm.entity.HandyFarmDTO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
      <title>로보 수정</title>
      <link rel="stylesheet" href="../css/common_ui.css">
      <link rel="stylesheet" href="../css/main_tab.css">
      <script src="../js/jquery-3.5.1.min.js"></script>
   </head>
   <body>
      <div class="wrap">
		<!-- upButton -->
		<button class="b-n HF-back d-b m-t-ml m-l" value="back" onclick="history.back(-1);" >
			<img class="ud-img f-l" src="../icon/upButton.png" alt="back"/>
		</button>			
		<!-- // upButton -->
			
        <!-- title -->
        <div class="title">
       	    로보 수정
        </div>
        <!-- // title -->
        
      	<form method="post" name="form">
	         <input type="hidden" name="robo_serial" value="${ robo_serial }">
	         <input type="hidden" name="gh_id" value="${ gh_id }">
	         
	         <!-- forEach robo_search 시작 -->
	         <c:forEach var="robo_search" items="${robo_search_list}" varStatus="status">
	         <!-- title -->
	         <!-- picture insert -->
	         <div class="cameraBox shadow" >
	            <!-- camera_img -->
	            <div class="">
	            <img class="camera-img" src="../icon/camera.png" alt="camera" onclick=document.all.file.click();>
	            <input type="file" name="file" id="file" style="display: none;"/>
	            </div>
	            <!-- camera_img -->
	         </div>
	         <!-- picture insert -->   
	         
	         <!-- Nickname -->
	         <div class="labelsetting labelNick t-a-l">
	           	 별명
	         </div>
	         <!-- Nickname -->
	         <!-- textBox Nickname -->
	         <div class="m-0-a">
	            <input class ="textBox b-n shadow p-x-ml f-s HF-DarkGray" value="${robo_search.robo_nickname}" type="text" maxlength="8" name="robo_nickname"/>
	         </div>
	         
	        
	         <!-- forEach robo_search 끝 -->
	         
	         
	         <!-- textBox Nickname -->
	         <!-- Crops -->
	         <div class="labelsetting labelNick t-a-l">
	            농작물
	         </div>
	         <!-- Crops -->
	         <!-- DropBox Crops-->
	         <select name="crops_id" class="p-x-ml d-b b-n shadow">
				<option value="${robo_search.crops_id}">${robo_search.crops_name}</option>
				<!-- forEach cultivar_list 시작 -->
				<c:forEach var="crops" items="${crops_list}" varStatus="status">
					<option value="${crops.crops_id}"> ${crops.crops_name}</option>
				</c:forEach>
				<!-- forEach cultivar_list 시작 -->
			</select>
			</c:forEach>
	         <!-- DropBox Crops -->
	         <!-- cancel button -->
	         <button class="d-ib cancel-b-Btn m-t-40" onclick="history.back(-1);">
	            취소
	         </button>
	         <!-- cancel button -->
	         
	         <!-- ok button -->
	         <input type="submit" class="d-ib sel-pageBtn okBox m-t-40"  value="확인" formaction=roboUpdate.do>
	         <!-- ok button -->
	     </form>
      </div>
   </body>
</html>