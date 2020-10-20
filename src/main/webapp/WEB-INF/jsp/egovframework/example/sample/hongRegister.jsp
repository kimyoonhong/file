<%@ page language="java" contentType="text/html; charset=UTF-8"
     pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}"  /> 
<%
  request.setCharacterEncoding("UTF-8");
  response.setCharacterEncoding("UTF-8");
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>

<title>이미지 파일 등록 화면</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<c:url value='/js/EgovMultiFile.js'/>" ></script>
<script type="text/javascript">


    // function readURL(input) {
      //if (input.files && input.files[0]) {
	     // var reader = new FileReader();
	      // reader.onload = function (e) {
	      //  $('#preview').attr('src', e.target.result);
       //   }
       //  reader.readAsDataURL(input.files[0]);
       //  } 
    //  } 
  
    // 체크박스 컨트롤
  function all_check(a) {
	if( a==1 ){
        $("input[name=IMAGE_PATH]").prop("checked", true);
      }else{
        $("input[name=IMAGE_PATH]").prop("checked", false);
      }
	} 
  function backToList(obj){
    obj.action="${contextPath}/egovSampleList.do";
    obj.submit();
  }
  function remove1(a,obj){
	  var image_name = encodeURIComponent(a);
	  obj.action="${contextPath}/remove.do";
	  obj.submit();
  }
  
  
  function remove(a){
	  alert(a);
	  var image_name = encodeURIComponent(a);
	  alert(image_name);
	    $.ajax({
	    	type:"POST",
	    	async: false,
	    	url:"${contextPath}/remove.do",
	    	data: {image_name : image_name},
	    	 success : function(data,textstatus){
	    		if(data.trim() == 'true'){
	    			alert("삭제되었습니다.");
	    		} else if(data.trim() == 'false') {
	    			alert("오류");
	    		}
	    	},
	    	error : function(data,textStatus) {
	    		alert(" 에러 " + data);
	    	},
	    	complete : function(data,textStatus) {
	    		
	    	} 	
	    }); // end of ajax
	    history.go(0);
	  }
  
  var cnt=1;
 
  
  
  function fn_addFile(){
	  $("#d_file").append("<br>"+"<input type='file' name='file"+cnt+"' />");
	  cnt++;
  } 
  
</script>
</head>
<body>

<form name="frm" method="post" action="${contextPath}/insertHong.do" enctype="multipart/form-data">
		<table >
			<tr>
				<td align="center">  </td>		
				<td>이미지 폴더 명<input type="text" name="name" value="test"/></td>	 
				<!-- onchange="readURL(this);" -->
				<td> <input type="file" name="image_path"   required /></td> 	
				<!-- <td align="left"> <input type="button" value="+" onClick="fn_addFile()" required/></td>	 -->							
			</tr>
			<!-- <tr>
				
			  	<td><img  id="preview" src="#"   width=30 height=30/></td>
			</tr> -->
			<tr>
	      		<td colspan="3"><div id="d_file"></div></td>
	   		</tr>
	    </table>
	    <table>
	    <tr>
	    	<td width=500px align = "center">
		    	<input type="submit" value="등록" />
				<input type=button value="목록보기"onClick="backToList(this.form)" />
			</td>
		</tr>
	    </table>
	    
</form>
<br><br><br>


	<table border=1>
		<tr>
			<td width=50 align="center">번호</td>
			<td width=150 align="center">파일명</td>
			<td width=100 align="center">이미지</td>
			<td width=100 align="center">등록일</td>
			<td width=40 align="center">삭제</td>
			<td width=70 align="center">다운로드</td>
		</tr>
		<c:forEach var="List" items="${HongList}" varStatus="status" >
			  <tr>
			  <td align="center">${List.HONG_IDX}</td>
			  <td align="center">${List.IMAGE_NAME}</td>
			  <td align="center">
			  	<a target="_blank" href="${contextPath}/getByteImage.do?hong_idx=${List.HONG_IDX}">이미지보기</a>
			  </td>
			  <td align="center">${List.REG_DATE}</td>
			  <td align="center"><a href="${contextPath}/remove.do?hong_idx=${List.HONG_IDX}">삭제</a></td>
			  <td><input type=button value="다운로드" onClick="location.href='${contextPath}/download.do?hong_idx=${List.HONG_IDX}'"/></td>
			  <%-- <td><input type=button id="remove" value="삭제" onClick="remove('${List.image_name}')"/></td> --%>
			  </tr>	
		</c:forEach>
	</table>

	<form id="test" method="post" action="${contextPath}/downloadZip.do">
		<fieldset>
			<legend> 파일 다운로드</legend>
			
			<c:forEach var="List" items="${HongList}" varStatus="status" >
				<div>
					${List.ORIGINALFILENAME}
					<input type="checkbox" name="IMAGE_PATH"  value="${List.IMAGE_PATH}"/>
				</div> 
			</c:forEach>
			
			<input type="button" id="check_all" value="전체선택" onclick="all_check(1)" />
			<input type="button" id="check_all" value="전체해제" onclick="all_check(2)" />
			<input type="submit" value="다운로드"/>
		</fieldset>
	</form>





</body>
</html>