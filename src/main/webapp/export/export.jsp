<%@ page language="java" pageEncoding="GBK"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@page import="java.util.List" import="java.io.*" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>分润管理系统</title>
<style type="text/css">
table#border {
	border-top: #d4d4d4 1px solid;
	border-left: #d4d4d4 1px solid;
}

table#border td {
	border-bottom: #d4d4d4 1px solid;
	border-right: #d4d4d4 1px solid;
}
</style>

</head>
<body>
	<%
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String url=request.getParameter("file");
		String filepath = new String(url.getBytes("ISO-8859-1"),"UTF-8");
		if (filepath != null) {
			OutputStream os = null;
			FileInputStream fis = null;
			try {
				String file = filepath;
				if (!(new File(file)).exists()) {
					System.out.println("没有文件");
					return;
				}
				String filename = file.substring(file.lastIndexOf("/")+1);
				os = response.getOutputStream();
				response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));
				response.setContentType("application/octet-stream");//八进制流 与文件类型无关
				byte temp[] = new byte[1024];
				fis = new FileInputStream(file);
				int n = 0;
			while ((n = fis.read(temp)) != -1) {
				os.write(temp, 0, n);
			}
		} catch (Exception e) {
		
		} finally {
			if (os != null)
			os.close();
			if (fis != null)
			fis.close();
		}
			out.clear();
			out = pageContext.pushBody();
		}
	%> 
</body>
</html>