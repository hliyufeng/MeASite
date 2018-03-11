layui.use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //video背景
    $(window).resize(function(){
        if($(".video-player").width() > $(window).width()){
            $(".video-player").css({"height":$(window).height(),"width":"auto","left":-($(".video-player").width()-$(window).width())/2});
        }else{
            $(".video-player").css({"width":$(window).width(),"height":"auto","left":-($(".video-player").width()-$(window).width())/2});
        }
    }).resize();

    //登录按钮事件
    form.on("submit(login)",function(data){
        login()
        return false;
    })
})
function login() {
    $.ajax({
        type: "POST",
        url: "/login",
        data: $('#signupForm').serialize(),
        success: function (r) {
            if (r.code == 200) {
                parent.location.href = '/index';
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

$(function(){

    $("#code").keyup(function(){
        var code = $("#code").val();
        if(code!=null && code != '' && code.length == 4){
            validCode();
        }
        $("#codee").removeClass("layui-show").addClass("layui-hide");
        $("#codes").removeClass("layui-show").addClass("layui-hide");
    });
});

function validCode(){
    var code = $("#code").val();
    $.ajax({
        type: "POST",
        url: "/common/validateCode",
        data: {code:code},
        success: function (r) {
            if (r.code == 200) {
                $("#codee").removeClass("layui-show").addClass("layui-hide");
                $("#codes").removeClass("layui-hide").addClass("layui-show");
            } else {
                $("#codes").removeClass("layui-show").addClass("layui-hide");
                $("#codee").removeClass("layui-hide").addClass("layui-show");
            }
        }
    });
}