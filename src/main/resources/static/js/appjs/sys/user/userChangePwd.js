/**
 * Created by lenovo on 2018/3/11.
 */
"use strict";
layui.use(['form'],function(){
  var  form = layui.form;

    //添加验证规则
    form.verify({
        confirmPwd : function(value, item){
            if(!new RegExp($("#newPassword").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }
        }
    });
    //创建一个编辑器
    form.on("submit(changePwd)", function (data) {
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        submit01(data.field, index);
        return false;
    })

});

function submit01(data, index1) {
    $.ajax({
        cache: true,
        type: "POST",
        url:"/sys/user/modifyPwd",
        data: data,
        async: false,
        error: function (request) {
            top.layer.close(index1);
            layer.alert("Connection error");
        },
        success: function (data) {
            MeA.clearForm("changePwdForm")
            top.layer.close(index1);
            if (data.code == 200) {
               layer.msg(data.msg);
            } else {
                layer.msg(data.msg)
            }

        }
    });
}