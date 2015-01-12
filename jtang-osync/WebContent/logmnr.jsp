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
<script src="js/deploy_logmnr.js"></script>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript"
	src="/jtang-console/dwr/interface/TanentBean.js">
</script>
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
							<li class="" id="tuopu_id">
								<div class="todo-icon fui-location"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>节点拓扑</strong>
									</h4>
									查看租户同步任务
								</div>
							</li>
							<li class="" id="deploy_id">
								<div class="todo-icon fui-plus"></div>
								<div class="todo-content">
									<h4 class="todo-name">
										<strong>触发器同步配置</strong>
									</h4>
									增加新的同步实例
								</div>
							</li>
							<li class="todo-done" id="deploy_logmnr_id">
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
						     
						     <li class="" id="wangluo_id">
						       <div class="todo-icon fui-list"></div>
						       <div class="todo-content">
									<h4 class="todo-name">
										<strong>网络延迟</strong>
									</h4>
									反映当前网络速度
								</div>
						   </li>
						     
							
							<li id="rizhi_id">
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
                   <div id="map_list">
                      <div>
                      <a class="btn btn-large btn-block btn-primary" href="javascript:void(0);" >
						 日志同步映射列表 
					  </a>
					  <a id="new_map" class="btn  btn-middle btn-danger" style="font-weight:bolder; margin-right:0px;float:right;margin-top:-41px;">新建映射</a>
					  </div>
                      <div>
                        <table class="table table-striped table-bordered" style="border-color: red;">
                           <thead>
                              <tr style="text-align: center;">
                                <td style="width: 20%;">源表地址</td>
                                <td style="width: 20%;">目的表地址</td>
                                <td style="width: 15%;">同步总数</td>
                                <td style="width: 15%;">错误总数</td>
                                <td style="width: 15%;">当前状态</td>
                                <td style="width: 15%;">操作</td>
                              </tr>
                           </thead>
                           <tbody id="map_container">
                             <tr style="text-align: center;">
                               <td style="width: 30%;">127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>运行中</td>
                                <td><a class="btn btn-danger" style="font-weight: bolder;">停止</a></td>
                             </tr>
                             <tr style="text-align: center;">
                               <td>127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>已停止</td>
                                <td><a class="btn btn-primary" style="font-weight: bolder;">启动</a></td>
                             </tr>
                             <tr style="text-align: center;">
                               <td style="width: 30%;">127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>运行中</td>
                                <td><a class="btn btn-danger" style="font-weight: bolder;">停止</a></td>
                             </tr>
                             <tr style="text-align: center;">
                               <td>127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>已停止</td>
                                <td><a class="btn btn-primary" style="font-weight: bolder;">启动</a></td>
                             </tr>
                            
                             <tr style="text-align: center;">
                               <td style="width: 30%;">127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>运行中</td>
                                <td><a class="btn btn-danger" style="font-weight: bolder;">停止</a></td>
                             </tr>
                             <tr style="text-align: center;">
                               <td>127.0.0.1:3306<br/>:orcl:teacher</td>
                                <td>127.0.0.1:3306<br/>:orcl:worker</td>
                                <td>34567</td>
                                <td style="color: red;">45</td>
                                <td>已停止</td>
                                <td><a class="btn btn-primary" style="font-weight: bolder;">启动</a></td>
                             </tr>
                           </tbody>
                        </table>
                      </div>
                   </div> 
					<div id="first_step" style="display:none;">
						<a class="btn btn-large btn-block btn-primary" href="#">
							第一步、指定原数据库和目的数据库 </a>
						<div class="row" style="margin-top: 10px;">
							<div class="col-md-6">
								<div
									style="margin-bottom: 5px; font-size: 16px; font-weight: bolder;">源数据库：</div>
								<form class="form-horizontal" role="form">
									<div class="form-group">
										<label for="sIp" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">IP</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sIp"
												placeholder="ip">
										</div>
									</div>
									<div class="form-group">
										<label for="sPort" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">端口</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sPort"
												placeholder="端口">
										</div>
									</div>
									<div class="form-group">
										<label for="sDBName" style="padding-left: 0px;padding-right: 0px;" class="col-sm-2 control-label">数据库</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sDBName"
												placeholder="数据库名称">
										</div>
									</div>
									
									<div class="form-group">
										<label for="sTableName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">源表空间</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sTableSpace"
												placeholder="源表空间">
										</div>
									</div>
									
									<div class="form-group">
										<label for="sTableName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">表名称</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sTableName"
												placeholder="表名称">
										</div>
									</div>

									<div class="form-group">
										<label for="sUserName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">DBA</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="sUserName"
												placeholder="DBA用户名">
										</div>
									</div>

									<div class="form-group">
										<label for="sPassword" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">密码</label>
										<div class="col-sm-10">
											<input type="password" class="form-control" id="sPassword"
												placeholder="密码">
										</div>
									</div>
								</form>
							</div>
							<div class="col-md-6">
								<div
									style="margin-bottom: 5px; font-size: 16px; font-weight: bolder;" >目的数据库：</div>
								<form class="form-horizontal" role="form">
									<div class="form-group">
										<label for="dIp" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">IP</label>
										<div class="col-sm-10">
											<input type="email" class="form-control" id="dIp"
												placeholder="ip">
										</div>
									</div>
									<div class="form-group">
										<label for="dPort" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">端口</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="dPort"
												placeholder="端口">
										</div>
									</div>

									<div class="form-group">
										<label for="dDBName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">数据库</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="dDBName"
												placeholder="数据库名称">
										</div>
									</div>
                                    
                                    <div class="form-group">
										<label for="sTableName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">表空间</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="dTableSpace"
												placeholder="表空间">
										</div>
									</div>
									
									<div class="form-group">
										<label for="dTableName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">表名称</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="dTableName"
												placeholder="表名称">
										</div>
									</div>

									<div class="form-group">
										<label for="dUserName" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">DBA</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="dUserName"
												placeholder="DBA用户名">
										</div>
									</div>

									<div class="form-group">
										<label for="dPassword" class="col-sm-2 control-label" style="padding-left: 0px;padding-right: 0px;">密码</label>
										<div class="col-sm-10">
											<input type="password" class="form-control" id="dPassword"
												placeholder="密码">
										</div>
									</div>

								</form>
							</div>
						</div>

						<div style="text-align: center;">
							<input type="button" class="btn btn-danger" value="测试连接"
								style="display: inline-block;" id="test_btn" /> <input
								type="button" class="btn btn-primary" value="下一步"
								style="display: inline-block; width: 200px;" id="next_step" />
						</div>

					</div>

					<div id="second_step">
						<a class="btn btn-large btn-block btn-primary" href="#">
							第二步、配置同步字段映射 </a>
						<div class="row" style="margin-top: 10px;">
						   <div class="col-md-12">
						    <form class="form-horizontal" role="form">
						     <div class="form-group">
								<label for="logsPath" class="col-sm-3 control-label">日志文件(*用;分隔)</label>
								    <div class="col-sm-9">
										<input type="text" class="form-control" id="logsPath"
											placeholder="日志文件">
									</div>
							 </div>
							 <div class="form-group">
								<label for="dictPath" class="col-sm-3 control-label">数据字典文件</label>
								    <div class="col-sm-9">
										<input type="text" class="form-control" id="dictPath"
											placeholder="数据字典文件">
									</div>
							 </div>
	
						    </form>
						   </div> 
						</div>	
							
						<div class="row" style="margin-top: 10px;">
							<div class="col-md-6">
								<div
									style="margin-bottom: 5px; font-size: 16px; font-weight: bolder;">源数据库字段</div>
								<form class="form-horizontal" role="form"
									id="s_columns_container"></form>
							</div>

							<div class="col-md-6">
								<div
									style="margin-bottom: 5px; font-size: 16px; font-weight: bolder;">目的数据库字段：</div>
								<form class="form-horizontal" role="form"
									id="d_columns_container"></form>
							</div>
						</div>

						<div style="text-align: center;">
						    <input type="button" class="btn btn-danger" value="上一步"
								style="display: inline-block;" id="prev_step" />
							<input type="button" class="btn btn-primary" value="提交同步配置"
								style="display: inline-block; width: 200px;" id="syn_set_btn" />
						</div>

					</div>

				</div>
			</div>
		</div>

	</div>

</body>
</html>
