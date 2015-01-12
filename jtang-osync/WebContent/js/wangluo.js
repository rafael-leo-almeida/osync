
var lineChartData = {
			labels : ["01","02","03","04","05","06","07","08","09",
			          "10","11","12"],
			datasets : [
				
				{
					fillColor : "rgba(255,50,0,0.1)",
					strokeColor : "rgba(255,0,0,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					data : [51,30,45,12,25,27,27,65,45,32,45,20]
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
	drawLine();
});

var drawLine = function(){
	
    var width = document.getElementById("line_id").offsetWidth;
	document.getElementById("canvas").width=width;
	var myLine = new Chart(document.getElementById("canvas").getContext("2d")).Line(lineChartData);
	
};


