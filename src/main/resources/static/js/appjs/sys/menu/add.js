/**
 * Created by lenovo on 2018/3/1.
 */
"use strict";
var prefix = "/sys/menu"
$(function(){
    MeA.selectTree.init();
    //打开图标列表
    $("#ico-btn").click(function () {

        layer.open({
            type: 2,
            title: '图标列表',
            content: '/FontIcoList.html',
            area: ['480px', '90%'],
            success: function (layero, index) {
            }
        });
    });
});
layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer;
    //创建一个编辑器
    form.on("submit(add)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        submit01(data.field,index);
        return false;
    })

})

function submit01(data,index1) {
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