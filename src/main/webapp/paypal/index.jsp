<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="topay.do">
	<table>
		<tr>
			<td>金额：</td>
			<td><input type="text" id="amount" name="amount" value="0.1"/></td>
		</tr>
		<tr>
			<td>币种：</td>
			<td>
				<select id="currency" name="currency">
					<option value="USD">USD</option>
				</select>
			</td>
		</tr>
		<tr align="center">
			<td colspan="2">
				<button type="submit">
    			<img src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/PP_logo_h_100x26.png" width="100px;" height="30px;"/></button>
			</td>
		</tr>
	</table>
</form>

<form method="post" action="payRefund.do" >
	<table>
		<tr>
			<td>saleId</td>
			<td><input readonly="readonly" type="text" id="id" name="id" value="<c:out value="${saleId }"></c:out>"/></td>
		</tr>
		<tr>
			<td>退款金额：</td>
			<td><input type="text" id="refundAmount" name="refundAmount" value="0.1"/></td>
		</tr>
		<tr align="center">
			<td colspan="2">
				<button type="submit" value="退款">退款</button>
			</td>
		</tr>
	</table>
</form>
<label style="color: red;"><c:out value="${message }"></c:out></label>
</body>
</html>