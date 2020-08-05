//var HOST = "http://192.168.0.101:8080/qus/";
var HOST = "http://129.28.85.6:8084/qus/";
$(function () {
    $('#loginBtn').on('click',doLogin);
    $("#uptPwdBtn").on('click',doUptPwd);
});
function doLogin(){
    if(!checkLogin()) return false;
    var url = "tower/staffinfo/login?number="+Math.random();
    $.ajax({
        //与服务器交互
        url:HOST+url,
        dataType: 'json',
        data: {"userName" : $.trim($("#userName").val()),
            "userPwd":$.trim($("#userPwd").val())
        },
        complete: function(XMLHttpRequest, textStatus){

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            alert("发生错误："+errorThrown);
        },
        success: function(res) {
            if(res.msg!=undefined){
                alert(res.msg);
                return;
            }
            window.sessionStorage.setItem("token",res.token);
            window.sessionStorage.setItem("tokenTime",new Date().getTime());
            window.sessionStorage.setItem("userName",res.userName);
            window.sessionStorage.setItem("staffName",res.staffName);
            window.sessionStorage.setItem("isSelSubmit",res.isSelSubmit);
            window.sessionStorage.setItem("isSelection",res.isSelection);
            //跳转到填写问卷的页面
            setSession("qusInfo",res.qusInfo);
            setSession("examStaffInfo",res.examStaffInfo);
            setSession("examItemInfo",res.examItemInfo);
            debugger;
            setSession("examScoreInfo",res.examScoreInfo);
            setSession("selStaffInfo",res.selStaffInfo);
            setSession("selDefInfo",res.selDefInfo);

            window.location.href = HOST + "html/tower/qus.html";
        }
    });
}
function checkLogin(){
    if($.trim($("#userName").val())==''){
        $("#msgSpan").html("请填写用户名");
        return false;
    }
    if($.trim($("#userPwd").val())==''){
        $("#msgSpan").html("请填写用户名");
        return false;
    }
    return true;

}
function checkUptPwd(){
    if($.trim($("#uptUserName").val())==''){
        $("#uptMsgSpan").html("请填写用户名");
        return false;
    }
    if($.trim($("#oldPwd").val())==''){
        $("#uptMsgSpan").html("请填写旧密码");
        return false;
    }
    if($.trim($("#newPwd").val())==''){
        $("#uptMsgSpan").html("请填写新密码");
        return false;
    }
    if($.trim($("#newConfirmPwd").val())==''){
        $("#uptMsgSpan").html("请填写确认旧密码");
        return false;
    }
    if($.trim($("#newPwd").val())!=$.trim($("#newConfirmPwd").val())){
        $("#uptMsgSpan").html("两次新密码输入不一致");
        return false;
    }
    return true;

}
function doUptPwd(){
    if(!checkUptPwd()) return false;
    var url = "tower/staffinfo/uptPwd?number="+Math.random();
    $.ajax({
        //与服务器交互
        url:HOST+url,
        dataType: 'json',
        data: {"userName" : $.trim($("#uptUserName").val()),
            "oldPwd":$.trim($("#oldPwd").val()),
            "newPwd":$.trim($("#newPwd").val()),
            "newConfirmPwd":$.trim($("#newConfirmPwd").val())},
        complete: function(XMLHttpRequest, textStatus){

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            alert("发生错误："+errorThrown);
        },
        success: function(res) {
            debugger;
            if(res.msg!=undefined){
                alert(res.msg);
                return;
            }else{
                alert("修改成功");
                $("#userName").val($("#uptUserName").val());
                $("#userPwd").val("");
                $("#uptForm").hide();
                $("#loginForm").show();

            }
        }
    });
}