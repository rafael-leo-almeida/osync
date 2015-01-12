
var selectDay = new Date();
var dPick = null;
var lineChartData = {
			labels : ["01","02","03","04","05","06","07","08","09",
			          "10","11","12","13","14","15","16","17","18","19","20",
			          "21","22","23","24"],
			datasets : [
//				{
//					fillColor : "rgba(220,220,220,0.5)",
//					strokeColor : "rgba(220,220,220,1)",
//					pointColor : "rgba(220,220,220,1)",
//					pointStrokeColor : "#fff",
//					data : [65,59,90,81,56,55,40,65,59,90,81,56,55,40,65,59,90,81,56,55,40,56,57,80]
//				},
				{
					fillColor : "rgba(151,187,205,0.5)",
					strokeColor : "rgba(151,187,205,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					data : [28,48,40,19,96,27,100,28,48,40,19,96,27,100,28,48,40,19,96,27,100,28,48,40]
				}
			]
			
		};

var barChartData = {
		labels : ["总量","错误数量","正确总量"],
		datasets : [
			{
				fillColor : "rgba(220,220,220,0.5)",
				strokeColor : "rgba(220,220,220,1)",
				data : [65,59,90]
			},
			{
				fillColor : "rgba(151,187,205,0.5)",
				strokeColor : "rgba(151,187,205,1)",
				data : [28,48,40]
			}
		]
		
	};

$(document).ready(function() {

//	$(".todo li").click(function() {
//		$(".todo li").removeClass("todo-done");
//		//$(this).toggleClass("todo-done");
//		$(this).addClass("todo-done");
//	});
	$("#tuopu_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("tuopu.jsp","_self");
	});
	$("#deploy_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("deploy.jsp","_self");
	});
	
	$("#deploy_logmnr_id").click(function() {
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("logmnr.jsp", "_self");
	});
	
	$("#jiankong_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("jiankong.jsp","_self");
	});
	$("#rizhi_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("rizhi.jsp","_self");
	});
	
	$("#yinqing_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("yinqing.jsp","_self");
	});
	
	$("#wangluo_id").click(function(){
		$(".todo li").removeClass("todo-done");
		$(this).addClass("todo-done");
		window.open("wangluo.jsp","_self");
	});
	
	$("#template_container").css("height", "570px");
	bindDatePick();
	$("#qushi_id").bind('click',function(){
		$("#qushi_id").parent().addClass("active");
		$("#cuowu_id").parent().removeClass("active");
		qushishow();
	});
	$("#cuowu_id").bind('click',function(){
		$("#qushi_id").parent().removeClass("active");
		$("#cuowu_id").parent().addClass("active");
		cuowushow();
	});
	initJianKong();	
	$("#search_btn").bind('click',getMapData);
});



var bindDatePick = function(){
	
	 dPick = new JsDatePick({
		useMode:2,
		target:"inputField",
		dateFormat:"%d-%M-%Y"
		/*selectedDate:{				This is an example of what the full configuration offers.
			day:5,						For full documentation about these settings please see the full version of the code.
			month:9,
			year:2006
		},
		yearsRange:[1978,2020],
		limitToToday:false,
		cellColorScheme:"beige",
		dateFormat:"%m-%d-%Y",
		imgPath:"img/",
		weekStartDay:1*/
	});
	dPick.setOnSelectedDelegate(function(){
		var obj = dPick.getSelectedDay();
		selectDay.setFullYear(obj.year);
		selectDay.setMonth(obj.month-1);
		selectDay.setDate(obj.day);
		$("#inputField").attr("placeholder",obj.day+"/"+obj.month+"/"+obj.year);
	});
	
};


var drawLine = function(){
	
    var width = document.getElementById("line_id").offsetWidth;
	document.getElementById("canvas").width=width;
	var myLine = new Chart(document.getElementById("canvas").getContext("2d")).Line(lineChartData);
	
	
};

var drawBar = function(){
	
	var width = document.getElementById("bar_id").offsetWidth;
	document.getElementById("barCanvas").width=width;
	var myBar = new Chart(document.getElementById("barCanvas").getContext("2d")).Bar(barChartData);
	
};

var qushishow= function(){
	
	$("#line_id").show();
	$("#bar_id").hide();
	drawLine();
	
};

var cuowushow = function(){
	
	$("#line_id").hide();
	$("#bar_id").show();
	drawBar();
	
};


var initJianKong = function(){
	TanentBean.initGetMapData(function(res){
		if(res!=null){
			var maps = res.maps;
			var qushi = res.qushi;
			var cuowu = res.cuowu;
			var qushiA = new Array();
			for(var index in qushi){
				qushiA.push(qushi[index]);
			}
			var totalC = new Array();
			var totalD = new Array();
			for(var index in cuowu){
				if(index<3){
					totalC.push(cuowu[index]);
				}else{
					totalD.push(cuowu[index]);
				}
			}
			var mapC = $("#map_select").empty();
			for(var index in maps){
				$('<option />').val(maps[index].mapId)
				.html(maps[index].diZhis+"->"+maps[index].diZhid)
				.appendTo(mapC);
			}
			lineChartData = {
				labels : ["01","02","03","04","05","06","07","08","09",
				          "10","11","12","13","14","15","16","17","18","19","20",
				          "21","22","23","24"],
				datasets : [
//					{
//						fillColor : "rgba(220,220,220,0.5)",
//						strokeColor : "rgba(220,220,220,1)",
//						pointColor : "rgba(220,220,220,1)",
//						pointStrokeColor : "#fff",
//						data : [65,59,90,81,56,55,40,65,59,90,81,56,55,40,65,59,90,81,56,55,40,56,57,80]
//					},
					{
						fillColor : "rgba(151,187,205,0.5)",
						strokeColor : "rgba(151,187,205,1)",
						pointColor : "rgba(151,187,205,1)",
						pointStrokeColor : "#fff",
						data : qushiA
					}
				]
			};
			barChartData = {
					labels : ["总量","错误数量","正确总量"],
					datasets : [
						{
							fillColor : "rgba(220,220,220,0.5)",
							strokeColor : "rgba(220,220,220,1)",
							data : totalC
						},
						{
							fillColor : "rgba(151,187,205,0.5)",
							strokeColor : "rgba(151,187,205,1)",
							data : totalD
						}
					]
					
				};
			drawLine();
		}else{
			
		}
		
	});
	
};

var getMapData = function(){
	
	var id = $("#map_select").val();
    if(id==null||id==undefined||$.trim(id).length==0){
    	alert("映射不存在！");
    	return;
    }
    var time = selectDay.getTime();
	var dayOrWeek = $("#dayOrWeek").val();
	//alert(time);
    //alert(id+":"+selectDay.getMonth()+"/"+selectDay.getDate()+":"+dayOrWeek);
	TanentBean.getMapData(id,time,dayOrWeek,function(res){
       
		if(res!=null){
			var qushi = res.qushi;
			var cuowu = res.cuowu;
			var qushiA = new Array();
			for(var index in qushi){
				qushiA.push(qushi[index]);
			}
			var totalC = new Array();
			var totalD = new Array();
			for(var index in cuowu){
				if(index<3){
					totalC.push(cuowu[index]);
				}else{
					totalD.push(cuowu[index]);
				}
			}
			if(dayOrWeek==0){
				lineChartData = {
						labels : ["01","02","03","04","05","06","07","08","09",
						          "10","11","12","13","14","15","16","17","18","19","20",
						          "21","22","23","24"],
						datasets : [
//							{
//								fillColor : "rgba(220,220,220,0.5)",
//								strokeColor : "rgba(220,220,220,1)",
//								pointColor : "rgba(220,220,220,1)",
//								pointStrokeColor : "#fff",
//								data : [65,59,90,81,56,55,40,65,59,90,81,56,55,40,65,59,90,81,56,55,40,56,57,80]
//							},
							{
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,1)",
								pointColor : "rgba(151,187,205,1)",
								pointStrokeColor : "#fff",
								data : qushiA
							}
						]
					};
			}else{
				lineChartData = {
						labels : ["01","02","03","04","05","06","07"],
						datasets : [
//							{
//								fillColor : "rgba(220,220,220,0.5)",
//								strokeColor : "rgba(220,220,220,1)",
//								pointColor : "rgba(220,220,220,1)",
//								pointStrokeColor : "#fff",
//								data : [65,59,90,81,56,55,40]
//							},
							{
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,1)",
								pointColor : "rgba(151,187,205,1)",
								pointStrokeColor : "#fff",
								data : qushiA
							}
						]
					};
			}
			barChartData = {
					labels : ["总量","错误数量","正确总量"],
					datasets : [
						{
							fillColor : "rgba(220,220,220,0.5)",
							strokeColor : "rgba(220,220,220,1)",
							data : totalC
						},
						{
							fillColor : "rgba(151,187,205,0.5)",
							strokeColor : "rgba(151,187,205,1)",
							data : totalD
						}
					]
					
				};
			drawLine();
			drawBar();
		}else{
			
		}
		
		
	});
};








