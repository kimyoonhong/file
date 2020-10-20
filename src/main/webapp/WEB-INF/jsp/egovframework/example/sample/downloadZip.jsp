<%@ page language="java" contentType="text/html; charset=UTF-8"
     pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  /> 
<%
  request.setCharacterEncoding("UTF-8");
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이미지 파일 등록 화면</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/cmm/fms/EgovMultiFile.js'/>" ></script>
<script type="text/javascript">

var suffix = 1;

function all_check() {
	if( $("#check_all").is(':checked') ){
        $("input[name=img]").prop("checked", true);
      }else{
        $("input[name=img]").prop("checked", false);
      }
}

</script>

 
</head>
<body>
<form id="test" method="post" action="${contextPath}/downloadZip.do">
	<fieldset>
		<legend> 파일 다운로드</legend>
		
		<c:forEach var="List" items="${HongList}" varStatus="status" >
			<div>
				<input type="checkbox" name="img"  value="${List.NAME}"/>
			</div> 
		</c:forEach>
		
		<input type="button" id="check_all" value="전체선택" onclick="all_check()" />
		<input type="submit" value="다운로드"/>
	</fieldset>
</form>

</body>
</html>