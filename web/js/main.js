function checkUserRole() {
    $.ajax({
        type: "POST",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url:"user/checkUserRole.do",
        dataType: "json",
        success:function(result){
            if(result.flag){
                //登录成功后的功能
                console.log(result.data);
            }
            else{
                eval(result.msg);
            }
        },
        error:function(result){
            alert(result.responseText);
        }
    });
}

function userLogout() {
    $.ajax({
        type: "POST",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url:"user/userLogout.do",
        dataType: "json",
        success:function(result){
            checkUserRole();
        },
        error:function(result){
            alert(result.responseText);
        }
    });
}