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
<!-- Loading Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">

<!-- Loading Flat UI -->
<link href="css/flat-ui.css" rel="stylesheet">

<script src="js/jquery-2.0.3.js"></script>
<script src="js/jquery.easing.1.3.js"></script>
<script src="js/tms-0.4.x.js"></script>
<!-- Load JS here for greater good =============================-->
<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="js/jquery.ui.touch-punch.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-select.js"></script>
<script src="js/bootstrap-switch.js"></script>
<script src="js/flatui-checkbox.js"></script>
<script src="js/flatui-radio.js"></script>
<script src="js/jquery.tagsinput.js"></script>
<script src="js/jquery.placeholder.js"></script>
<script src="js/wangluo.js"></script>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript"
	src="/jtang-databus-console/dwr/interface/TanentBean.js">
</script>
<script src="Chart/Chart.js"></script>
</head>

<body>
	<div class="template" id="template_container">
		<div style="height: 20px;"></div>
		<div style="width: 95%; margin-left: 2.5%; margin-right: 2.5%;">
			<div class="header" style="display: none;">
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
			<div style="clear: both;"></div>

			<div class="row" style="margin-top: 20px;">
				<div class="col-md-3">
					<div class="todo">
						<div class="todo-search">
							<input class="todo-search-field" type="search" value=""
								placeholder="Search" />
						</div>
						<ul>
							<li  id="tuopu_id">
								<div class="todo-icon fui-location"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>节点拓扑</strong>
									</h4>
									查看租户同步任务
								</div>
							</li>
							<li id="deploy_id">
								<div class="todo-icon fui-plus"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>触发器同步配置</strong>
									</h4>
									增加新的同步实例
								</div>
							</li>
							<li class="" id="deploy_logmnr_id">
								<div class="todo-icon fui-plus"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>日志同步配置</strong>
									</h4>
									增加新的同步实例
								</div>
							</li>
							<li id="jiankong_id">
								<div class="todo-icon fui-eye"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>同步监控</strong>
									</h4>
									实时监控实例运行状态
								</div>
							</li>
							
							<li class="" id="yinqing_id">
						       <div class="todo-icon fui-list"></div>
						       <div class="todo-content">
									<h4 class="todo-name">
										<strong>引擎性能</strong>
									</h4>
									当前节点处理能力
								</div>
						     </li>
						     
						     <li class="todo-done" id="wangluo_id">
						       <div class="todo-icon fui-list"></div>
						       <div class="todo-content">
									<h4 class="todo-name">
										<strong>网络延迟</strong>
									</h4>
									反映当前网络速度
								</div>
						   </li>
							
							<li class="" id="rizhi_id">
								<div class="todo-icon fui-time"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>同步日志</strong>
									</h4>
									查看同步日志
								</div>
							</li>
						</ul>
					</div>
				</div>
				<!-- /todo list -->
				<div class="col-md-9">
				  <div style="width: 100%;margin-top:30px;" id="line_id" >
					     <canvas id="canvas" width="900" height="480"></canvas>
				    </div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
