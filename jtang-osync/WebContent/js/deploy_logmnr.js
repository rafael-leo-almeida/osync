$(document).ready(function() {

	// $(".todo li").click(function() {
	// $(".todo li").removeClass("todo-done");
	// //$(this).toggleClass("todo-done");
	// $(this).addClass("todo-done");
	// });
	$("#tuopu_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("tuopu.jsp", "_self");
	});
	$("#deploy_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("deploy.jsp", "_self");
	});

	$("#deploy_logmnr_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("logmnr.jsp", "_self");
	});
	
	$("#jiankong_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("jiankong.jsp", "_self");
	});
	$("#rizhi_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("rizhi.jsp", "_self");
	});
	$("#yinqing_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("yinqing.jsp", "_self");
	});

	$("#wangluo_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("wangluo.jsp", "_self");
	});

	$("#template_container").css("height", "570px");
	$("#test_btn").bind('click', testDB);
	$("#second_step").hide();
	$("#next_step").bind('click', function() {
		$("#first_step").hide();
		$("#second_step").show();
	});
	$("#next_step").bind('click', nextStep);
	$("#syn_set_btn").bind('click', synSet);
	$("#new_map").bind('click', function() {

		$("#map_list").hide();
		$("#first_step").show();
		$("#second_step").hide();

	});
	$("#prev_step").bind('click', function() {
		$("#map_list").hide();
		$("#first_step").show();
		$("#second_step").hide();
	});
	getTableMapWebs();
});

var testDB = function() {

	var sIp = $("#sIp").val();
	var sPort = $("#sPort").val();
	var sDBName = $("#sDBName").val();
	var sTableName = $("#sTableName").val();
	var sUserName = $("#sUserName").val();
	var sPassword = $("#sPassword").val();

	var dIp = $("#dIp").val();
	var dPort = $("#dPort").val();
	var dDBName = $("#dDBName").val();
	var dTableName = $("#dTableName").val();
	var dUserName = $("#dUserName").val();
	var dPassword = $("#dPassword").val();

	if (sIp == null || sIp == undefined || $.trim(sIp).length == 0
			|| sPort === null || sPort == undefined
			|| $.trim(sPort).length == 0 || sDBName == null
			|| sDBName == undefined || $.trim(sDBName).length == 0
			|| sTableName == null || sTableName == undefined
			|| $.trim(sTableName).length == 0 || sUserName == null
			|| sUserName == undefined || $.trim(sUserName).length == 0
			|| sPassword == null || sPassword == undefined
			|| $.trim(sPassword).length == 0) {
		alert("源数据库信息填写不完整！");
		return;
	}
	if (dIp == null || dIp == undefined || $.trim(dIp).length == 0
			|| dPort == null || dPort == undefined || $.trim(dPort).length == 0
			|| dDBName == null || dDBName == undefined
			|| $.trim(dDBName).length == 0 || dTableName == null
			|| dTableName == undefined || $.trim(dTableName).length == 0
			|| dUserName == null || dUserName == undefined
			|| $.trim(dUserName).length == 0 || dPassword == null
			|| dPassword == undefined || $.trim(dPassword).length == 0) {
		alert("目的数据库信息填写不完整！");
		return;
	}

	TanentBean.testBoth(sIp, sPort, sDBName, sTableName, sUserName, sPassword,
			dIp, dPort, dDBName, dTableName, dUserName, dPassword,
			function(res) {

				if (res != null) {
					if (res.sFlag == true && res.dFlag == true) {
						alert("源数据库连接成功，目的数据库连接成功！");
						return;
					} else if (res.sFlag == true && res.dFlag == false) {
						alert("源数据可连接成功，目的数据库连接失败！");
						return;
					} else if (res.sFlag == false && res.dFlag == true) {
						alert("源数据库连接失败,目的数据库连接成功!");
						return;
					} else if (res.sFlag == false && res.dFlag == false) {
						alert("源数据库连接失败，目的数据库连接失败!");
						return;
					}
				}

			});
};

var nextStep = function() {

	var sIp = $("#sIp").val();
	var sPort = $("#sPort").val();
	var sDBName = $("#sDBName").val();
	var sTableSpace = $("#sTableSpace").val();
	var sTableName = $("#sTableName").val();
	var sUserName = $("#sUserName").val();
	var sPassword = $("#sPassword").val();

	var dIp = $("#dIp").val();
	var dPort = $("#dPort").val();
	var dDBName = $("#dDBName").val();
	var dTableSpace = $("#dTableSpace").val();
	var dTableName = $("#dTableName").val();
	var dUserName = $("#dUserName").val();
	var dPassword = $("#dPassword").val();
	if (sIp == null || sIp == undefined || $.trim(sIp).length == 0
			|| sPort === null || sPort == undefined
			|| $.trim(sPort).length == 0 || sDBName == null
			|| sDBName == undefined || $.trim(sDBName).length == 0
			|| sTableSpace == null || sTableSpace == undefined
			|| sTableName == null || sTableName == undefined
			|| $.trim(sTableName).length == 0 || sUserName == null
			|| sUserName == undefined || $.trim(sUserName).length == 0
			|| sPassword == null || sPassword == undefined
			|| $.trim(sPassword).length == 0) {
		alert("源数据库信息填写不完整！");
		return;
	}
	if (dIp == null || dIp == undefined || $.trim(dIp).length == 0
			|| dPort == null || dPort == undefined || $.trim(dPort).length == 0
			|| dDBName == null || dDBName == undefined
			|| $.trim(dDBName).length == 0 || dTableName == null
			|| dTableName == undefined || $.trim(dTableName).length == 0
			|| dTableSpace == null || dTableSpace == undefined
			|| dUserName == null || dUserName == undefined
			|| $.trim(dUserName).length == 0 || dPassword == null
			|| dPassword == undefined || $.trim(dPassword).length == 0) {
		alert("目的数据库信息填写不完整！");
		return;
	}
	TanentBean.getBothSchemas(sIp, sPort, sDBName, sTableName, sUserName,
			sPassword, dIp, dPort, dDBName, dTableName, dUserName, dPassword,
			function(res) {

				if (res == null) {
					alert("数据库连接不正确！");
					return;
				}
				var dataMap = res;
				var sContainer = $("#s_columns_container");
				var dContainer = $("#d_columns_container");
				sContainer.empty();
				dContainer.empty();
				var num = 0;
				for ( var key in dataMap) {

					var sId = "s_column_" + num;
					var dId = "d_column_" + num;
					num++;
					var sGroupNode = $('<div class="form-group" />').appendTo(
							sContainer);
					var sColNode = $('<div class="col-sm-10" />').appendTo(
							sGroupNode);
					$('<input type="text" class="form-control" />').val(key)
							.attr("readonly", "readonly").attr("id", sId)
							.appendTo(sColNode);

					var dGroupNode = $('<div class="form-group" />').appendTo(
							dContainer);
					var dColNode = $('<div class="col-sm-10" />').appendTo(
							dGroupNode);
					var selectNode = $('<select class="form-control" />').attr(
							"id", dId).appendTo(dColNode);
					var dCols = dataMap[key];
					for ( var index in dCols) {
						$('<option />').val(dCols[index]).text(dCols[index])
								.appendTo(selectNode);
					}
					$('<option />').val(" ").text("无").appendTo(selectNode);

				}
				$("#first_step").hide();
				$("#second_step").show();
			});

};

var synSet = function() {

	var sIp = $("#sIp").val();
	var sPort = $("#sPort").val();
	var sDBName = $("#sDBName").val();
	var sTableSpace = $("#sTableSpace").val();
	var sTableName = $("#sTableName").val();
	var sUserName = $("#sUserName").val();
	var sPassword = $("#sPassword").val();
	var dIp = $("#dIp").val();
	var dPort = $("#dPort").val();
	var dDBName = $("#dDBName").val();
	var dTableSpace = $("#dTableSpace").val();
	var dTableName = $("#dTableName").val();
	var dUserName = $("#dUserName").val();
	var dPassword = $("#dPassword").val();
	var logsPath = $("#logsPath").val();
	var dictPath = $("#dictPath").val();
	
	if (sIp == null || sIp == undefined || $.trim(sIp).length == 0
			|| sPort === null || sPort == undefined
			|| $.trim(sPort).length == 0 || sDBName == null
			|| sDBName == undefined || $.trim(sDBName).length == 0
			|| sTableSpace == null || sTableSpace == undefined
			|| sTableName == null || sTableName == undefined
			|| $.trim(sTableName).length == 0 || sUserName == null
			|| sUserName == undefined || $.trim(sUserName).length == 0
			|| sPassword == null || sPassword == undefined
			|| $.trim(sPassword).length == 0) {
		alert("源数据库信息填写不完整！");
		return;
	}
	if (dIp == null || dIp == undefined || $.trim(dIp).length == 0
			|| dPort == null || dPort == undefined || $.trim(dPort).length == 0
			|| dDBName == null || dDBName == undefined
			|| $.trim(dDBName).length == 0 || dTableName == null
			|| dTableSpace == null || dTableSpace == undefined
			|| dTableName == undefined || $.trim(dTableName).length == 0
			|| dUserName == null || dUserName == undefined
			|| $.trim(dUserName).length == 0 || dPassword == null
			|| dPassword == undefined || $.trim(dPassword).length == 0) {
		alert("目的数据库信息填写不完整！");
		return;
	}
	var array = new Array();
	var index = 0;
	while (true) {
		var map = new Object();
		var sNode = $("#s_column_" + index);
		if (sNode == null || sNode == undefined) {
			break;
		}
		var sColumn = sNode.val();
		if (sColumn == null || sColumn == undefined) {
			break;
		}
		var dNode = $("#d_column_" + index);
		var dColumn = dNode.val();
		if (dColumn == undefined) {
			dColumn = null;
		}
		map.sColumn = sColumn;
		map.dColumn = dColumn;
		array.push(map);
		index++;
	}
	TanentBean.addLogmnrBothTableMap(sIp, sPort, sDBName, sTableSpace,sTableName, sUserName,
			sPassword, dIp, dPort, dDBName,dTableSpace,dTableName, dUserName, dPassword,
			array,logsPath,dictPath, function(res) {
				if (res) {
					alert("增加成功!");
					return;
				} else {
					alert("增加失败!");
					return;
				}
			});
};
var getTableMapWebs = function(){
	
	TanentBean.getAllTableMapWebs(function(res){
		if(res==null||res==undefined){
			return;
		}
		var webs = res;
		var container = $("#map_container");
		container.empty();
		for(var index in webs){
			var trNode = $('<tr style="text-align: center;"/>').appendTo(container);
			$('<td/>').html(webs[index].diZhis).appendTo(trNode);
			$('<td/>').html(webs[index].diZhid).appendTo(trNode);
			$('<td/>').text(webs[index].total).appendTo(trNode);
			$('<td/>').text(webs[index].wrong).appendTo(trNode);
            if(webs[index].status==1){
            	$('<td/>').text("运行中").appendTo(trNode);
            }else{
            	$('<td/>').text("已停止").appendTo(trNode);
            }
            var tdNode = $('<td/>').appendTo(trNode);
            if(webs[index].status==1){
                $('<a id="stop_btn" class="btn btn-danger" style="font-weight: bolder;"/>')
                .text("停止")
                .appendTo(tdNode)
                .attr("mapId",webs[index].mapId)
                .click(function(){
                	var id = $(this).attr("mapId");
                	TanentBean.stopTableMapById(id,function(res){
                		if(res==true){
                			alert("同步映射已经成功停止！");
                			$("#stop_btn").hide();
                			$("#start_btn").show();
                		}
                	});
                });
                $('<a id="start_btn" class="btn btn-success" style="font-weight: bolder;display:none;"/>')
                .text("开始")
                .appendTo(tdNode)
                .attr("mapId",webs[index].mapId)
                .click(function(){
                	var id = $(this).attr("mapId");
                	TanentBean.startTableMapById(id,function(res){
                		if(res==true){
                			alert("同步映射已经成功启动止！");
                			$("#start_btn").hide();
                			$("#stop_btn").show();
                		}
                	});
                });
            }else{
            	  $('<a id="stop_btn" class="btn btn-danger" style="font-weight: bolder;display:none;"/>')
                  .text("停止")
                  .appendTo(tdNode)
                  .attr("mapId",webs[index].mapId)
                  .click(function(){
                  	var id = $(this).attr("mapId");
                  	TanentBean.stopTableMapById(id,function(res){
                  		if(res==true){
                  			alert("同步映射已经成功停止！");
                  			$("#stop_btn").hide();
                  			$("#start_btn").show();
                  		}
                  	});
                  });
                  $('<a id="start_btn" class="btn btn-success" style="font-weight: bolder;"/>')
                  .text("开始")
                  .appendTo(tdNode)
                  .attr("mapId",webs[index].mapId)
                  .click(function(){
                  	var id = $(this).attr("mapId");
                  	TanentBean.startTableMapById(id,function(res){
                  		if(res==true){
                  			alert("同步映射已经成功启动！");
                  			$("#start_btn").hide();
                  			$("#stop_btn").show();
                  		}
                  	});
                  });
            }
             
		}
		
	});
	
};

