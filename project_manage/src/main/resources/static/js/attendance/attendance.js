/* 文件下载 */
function fileDownload(){
	var name = $("#name").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var isOnJob = $("#isOnJob").val();
	console.info(startDate);
	if(startDate==null || startDate=="" || endDate==null || endDate==""){
		alert("请选择起止日期!");
	}else{
		$.download("/clocking/export","GET",name,startDate,endDate,isOnJob);
	}
}

function fileDownloadForMon(){
	var name = $("#name").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var isOnJob = $("#isOnJob").val();
	console.info(startDate);
	if(startDate==null || startDate=="" || endDate==null || endDate==""){
		alert("请选择起止日期!");
	}else{
		$.download("/clocking/exportMonthly","GET",name,startDate,endDate,isOnJob);
	}
}


jQuery.download = function(url, method, name, startDate, endDate, isOnJob){
    jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                '<input type="text" name="name" value="'+name+'"/>' + 
                '<input type="text" name="startDate" value="'+startDate+'"/>' + 
                '<input type="text" name="endDate" value="'+endDate+'"/>' + 
                '<input type="text" name="isOnJob" value="'+isOnJob+'"/>' + 
            '</form>')
    .appendTo('body').submit().remove();
};

/**
 * 异常高亮提示：
 * 	1.打卡记录不完整
 * 	2.迟到、早退
 */
$(function(){
	var trList = $("#att_table").children("tbody").children("tr");
	for (var i=0;i<trList.length;i++) {
		var tdArr = trList.eq(i).find("td");
		var inTime = tdArr.eq(4).html();
		var outTime = tdArr.eq(5).html();
		if(inTime == null || inTime == "" || outTime == null || outTime == ""){
            $(trList.eq(i)).addClass("error");
		}
		var late = tdArr.eq(7).html();
		var leave = tdArr.eq(8).html();
		if((late != null && late != "" && late > 0) || leave != null && leave != "" && leave > 0){
			$(trList.eq(i)).addClass("warn");
		}
	}
});

function modifyAttendanceRecord(att){
	
	var obj = JSON.parse(att);
	
	$("#modify_id").val(obj.id);
	$("#modify_name").val(obj.empName);
	$("#modify_todayWeek").val(obj.todayWeek);
	$("#modify_attendanceDate").val(new Date(obj.attendanceDate).format("yyyy-MM-dd"));
	//$("#modify_attendanceDate").val(obj.attendanceDate);
	$("#modify_signinTime").val(obj.signinTime);
	$("#modify_signoutTime").val(obj.signoutTime);
	$("#modify_vacationHour").val(obj.vacationHour);
	$("#modify_lateTime").val(obj.lateTime);
	$("#modify_leaveTime").val(obj.leaveTime);
	$("#modify_workOuttime").val(obj.workOuttime);
	$("#modify_empId").val(obj.empId);
	$("#modify_flag").find("option[value='"+obj.flag+"']").attr("selected",true);
}

function modifySave() {
	$("#modify_form").submit();
}

Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds() // millisecond
    };
    if (/(y+)/.test(format) || /(Y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};
