<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Home</title>
<link href='http://fonts.googleapis.com/css?family=Yellowtail'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" media="screen"
	href="css/reset.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="css/text.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="css/style.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="css/grid_12.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="css/slider.css">
<link href='http://fonts.googleapis.com/css?family=Condiment'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oxygen'
	rel='stylesheet' type='text/css'>
<script src="js/jquery-2.0.3.js"></script>
<script src="js/jquery.easing.1.3.js"></script>
<script src="js/tms-0.4.x.js"></script>
<script>
	$(document).ready(function() {
		$('.slider')._TMS({
			show : 0,
			pauseOnHover : true,
			prevBu : false,
			nextBu : false,
			playBu : false,
			duration : 1000,
			preset : 'fade',
			pagination : true,
			pagNums : false,
			slideshow : 7000,
			numStatus : true,
			banners : 'fromRight',
			waitBannerAnimation : false,
			progressBar : false
		})
	});
</script>
<!--[if lt IE 8]>
       <div style=' clear: both; text-align:center; position: relative;'>
         <a href="http://windows.microsoft.com/en-US/internet-explorer/products/ie/home?ocid=ie6_countdown_bannercode">
           <img src="http://storage.ie6countdown.com/assets/100/images/banners/warning_bar_0000_us.jpg" border="0" height="42" width="820" alt="You are using an outdated browser. For a faster, safer browsing experience, upgrade for free today." />
        </a>
      </div>
    <![endif]-->
<!--[if lt IE 9]>
   		<script type="text/javascript" src="js/html5.js"></script>
    	<link rel="stylesheet" type="text/css" media="screen" href="css/ie.css">
	<![endif]-->
</head>

<body>
	<div class="template">	
		<div class="layer">
			<div class="black"></div>
			<div class="main" >
				<div class="center" >
					<div class="header">
						<div class="logo">
							<a target="_blank" href=""><img src="img/jtanglogo4.png" alt="" /></a>
						</div>
						<div class="social-icons">
							<a href="#" class="icon-3"></a> <a href="#" class="icon-2"></a> <a
								href="#" class="icon-1"></a>
						</div>
						<div class="form-search">
							<form id="form-search" method="post">
								<input type="text" value="Type here..."
									onBlur="if(this.value=='') this.value='Type here...'"
									onFocus="if(this.value =='Type here...' ) this.value=''" /> <a
									href="#"
									onClick="document.getElementById('form-search').submit()"
									class="search_button"></a>
							</form>
						</div>

					</div>
					<div class="clear"></div>
					<nav>
						<ul class="menu">
							<li ><a target="" href="index.html">首页</a></li>
							<li class="home-page current"><a target="" href="getAllTanents.action" >租户管理</a></li>
							<li><a target="" href="deploy.jsp">同步部署 </a></li>
							<li><a target="" href="">对象管理</a></li>
							<li><a target="" href="">日志分析</a></li>
							<li><a target="" href="">错误提示</a></li>
						</ul>
					</nav>
					<div class="clear"></div>
					<div class="he"></div>

						<div class="he"></div>
						<div class="he"></div>
						<div>
						   增加一个用户：
						   <form action="addTanent.action" method="post">
						     
						     用户名:<input type="text" name="userName"/>
						     密&nbsp;&nbsp;码<input type="password" name="password">
						     IP:  <input type="text" name="ip">
						     端口号:<input type="text" name="port">  
						     数据库:<input type="text" name="DBName">
						     <input type="submit" value="添加用户" style=" font-size: 20px"/>
						   </form>
						</div>
						
						
						
                        <div class="tanents">
                        
                               <s:iterator value="tanents" var="tanentInfos" status="status">
                                   用户名:<s:property value="#tanentInfos.tanent.userName" /><br/>
                                   IP地址:<s:property value="#tanentInfos.tanent.ip"/><br/>
                                   端口:<s:property value="#tanentInfos.tanent.port"/><br/>
                                   数据库名称:<s:property value="#tanentInfos.tanent.DBName"/><br/>
                                   用户权限:
                                   <s:iterator value="#tanentInfos.privs.allPrivs" var="priv">
                                       [<s:property value="priv"/>]&nbsp;&nbsp;
                                   </s:iterator>
                                   <br/> 
                                   用户表信息:
                                   <br/>
                                   <s:iterator value="#tanentInfos.tableInfos" var="table">
                                      表名:<s:property value="#table.tableName"/>
                                      
                                      关键字:
                                      <s:iterator value="#table.keys" var="key">
                                         <s:property value="key"/>
                                      </s:iterator>
                                      <table>
                                      <tr>
                                        <td>字段名</td><td>字段类型</td>
                                      </tr>
                                        <s:iterator value="#table.columnType" var="type">
                                            <tr>
                                              <td><s:property value="#type.key"/> </td>
                                              <td><s:property value="#type.value"/> </td> 
                                            </tr>
                                        </s:iterator>
                                      </table>
                                   </s:iterator>
                                    
                               </s:iterator>
                               
                        </div>		
			</div>
			</div>
			</div>
			
	</div>
</body>
</html>
