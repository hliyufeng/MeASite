/**
 * Created by lenovo on 2018/3/7.
 */
/**
 * Created by lenovo on 2018/3/5.
 */
"use strict";
var prefix = "/sys/user";
$(function () {
    MeA.selectTree.init();

});

layui.use(['form', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer;


    form.verify({
        loginname: function (value, item) {
            var oldName = $("#oldLoginName").val();
            var result = "";
            $.ajax({
                cache: true,
                type: "POST",
                url: prefix + "/checkLoginName",
                data: {oldLoginName: oldName, loginName: value},
                async: false,
                success: function (data) {
                    result = "";
                    if (data.code == 200) {
                        if (!data.data) {
                            result = "登录名已存在！"
                        }
                    }

                }
            });
            if (MeA.isNotEmpty(result)) {
                return result;
            }
        },
        confirmNewPassword: function (value, item) {
            var newPassword = $("#newPassword").val();
            var confirmNewPassword = $("#confirmNewPassword").val();
            var result = "";
            if (newPassword != confirmNewPassword) {
                return "密码与确认密码不一致!"
            }
        }
    })
    //创建一个编辑器
    form.on("submit(form)", function (data) {
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        var roleIds = MeA.form.getChecked("roleIds");
        $("#roleIdList").val(roleIds);
        submit01(getFormParams("sbForm"), index);
        return false;
    })

    form.on('select(dataScopeFilter)', function (data) {
        refreshOfficeTree(data.value);
    });

})
function submit01(data, index1) {
    $.ajax({
        cache: true,
        type: "POST",
        url: prefix + "/save",
        data: data,
        async: false,
        error: function (request) {
            top.layer.close(index1);
            layer.alert("Connection error");
        },
        success: function (data) {
            top.layer.close(index1);
            if (data.code == 200) {
                parent.layer.msg(data.msg);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
            } else {
                parent.layer.msg(data.msg)
            }
        }
    });
}

