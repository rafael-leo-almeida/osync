
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
});
