$(function(){
	$(".login").on('click', function(){
		var name = $(".login-userName").val();
		var password = $(".login-password").val();
		if (name.length == 0 || password.length == 0) {
			$(".error-msg").html("用户名或密码不能为空！");
		} else {
			$(".login-form").submit();
		}
	});
})

 