function modify(emp){
var obj = JSON.parse(emp);
	
	$(".emp-id").val(obj.id);
	$(".name").val(obj.name);
	$(".startDate").val(timeStamp2String(obj.inductionTime));
	if(typeof(obj.resignationTime)!="undefined"){
		$(".endDate").val(timeStamp2String(obj.resignationTime));
	} else {
		$(".endDate").val('');
	} 
	$(".isOnJob").find("option[value='"+obj.isOnJob+"']").attr("selected",true);
	$("input[name='isOnJob']:eq("+obj.isOnJob+")").attr("checked",'checked'); 
}

function timeStamp2String(time){
    var datetime = new Date();
     datetime.setTime(time);
     var year = datetime.getFullYear();
     var month = datetime.getMonth() + 1;
     var date = datetime.getDate();
     var hour = datetime.getHours();
     var minute = datetime.getMinutes();
     var second = datetime.getSeconds();
     var mseconds = datetime.getMilliseconds();
     return year + "-" + conversion(month) + "-" + conversion(date)+" "+conversion(hour)
     +":"+conversion(minute)+":"+conversion(second)+"."+conversion2(mseconds);
};

function conversion(data) {
	if(data < 10) {
		return '0'+data;
	}
	return data;
}

function conversion2(data) {
	if(data < 100) {
		return '00'+data;
	}
	return data;
}