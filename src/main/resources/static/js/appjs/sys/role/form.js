/**
 * Created by lenovo on 2018/3/5.
 */
"use strict";
var prefix = "/sys/role";
var menuTree = null;
var officeTree = null;


$(function () {
    MeA.selectTree.init();
    initMenuTree();
    initOfficeTree();

});

layui.use(['form', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer;


    form.verify({
        name: function (value, item) {
            var oldName = $("#oldName").val();
            var result = "";
            $.ajax({
                cache: true,
                type: "POST",
                url: prefix + "/checkName",
                data: {oldName: oldName, name: value},
                async: false,
                success: function (data) {
                    result = "";
                    if (data.code == 200) {
                        if (!data.data) {
                            result = "角色名称已存在！"
                        }
                    }

                }
            });
            if (MeA.isNotEmpty(result)) {
                return result;
            }
        },
        enname: function (value, item) {
            var oldEnname = $("#oldEnname").val();
            var result = "";
            $.ajax({
                cache: true,
                type: "POST",
                url: prefix + "/checkEnname",
                data: {oldEnname: oldEnname, enname: value},
                async: false,
                success: function (data) {
                    result = "";
                    if (data.code == 200) {
                        if (!data.data) {
                            result = "角色英文名称已存在！"
                        }
                    }
                }
            });
            if (MeA.isNotEmpty(result)) {
                return result;
            }
        }
    })
    //创建一个编辑器
    form.on("submit(form)", function (data) {
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});

        var ids = [], nodes = menuTree.getCheckedNodes(true);
        for(var i=0; i<nodes.length; i++) {
            ids.push(nodes[i].id);
        }
        $("#menuIds").val(ids);
        var ids2 = [], nodes2 = officeTree.getCheckedNodes(true);
        for(var i=0; i<nodes2.length; i++) {
            ids2.push(nodes2[i].id);
        }
        $("#officeIds").val(ids2);

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

function initMenuTree() {
    var setting = {
        check: {enable: true, nocheckInherit: true}, view: {selectedMulti: false},
        data: {simpleData: {enable: true}}, callback: {
            beforeClick: function (id, node) {
                menuTree.checkNode(node, !node.checked, true, true);
                return false;
            }
        }
    };
    var zNodes = [];
    $.ajax({
        cache: true,
        type: "get",
        url: prefix + "/menuList",
        async: false,
        error: function (request) {
            layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 200) {
                zNodes = data.data;
            } else {
                parent.layer.msg(data.msg)
            }
            menuTree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
            // 不选择父节点
            menuTree.setting.check.chkboxType = {"Y": "ps", "N": "s"};
            var ids = $("#menuIds").val().split(",");
            for (var i = 0; i < ids.length; i++) {
                var node = menuTree.getNodeByParam("id", ids[i]);
                try {
                    menuTree.checkNode(node, true, false);
                } catch (e) {
                }
            }
            // 默认展开全部节点
            menuTree.expandAll(true);
        }
    });
}

function initOfficeTree() {
    var setting = {
        check: {enable: true, nocheckInherit: true}, view: {selectedMulti: false},
        data: {simpleData: {enable: true}}, callback: {
            beforeClick: function (id, node) {
                officeTree.checkNode(node, !node.checked, true, true);
                return false;
            }
        }
    };
    var zNodes = [];
    $.ajax({
        cache: true,
        type: "get",
        url: prefix + "/officeList",
        async: false,
        error: function (request) {
            layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 200) {
                zNodes = data.data;
            } else {
                parent.layer.msg(data.msg)
            }
             officeTree = $.fn.zTree.init($("#officeTree"), setting, zNodes);
            // 不选择父节点
            officeTree.setting.check.chkboxType = {"Y": "ps", "N": "s"};
            var ids = $("#officeIds").val().split(",");
            for (var i = 0; i < ids.length; i++) {
                var node = officeTree.getNodeByParam("id", ids[i]);
                try {
                    officeTree.checkNode(node, true, false);
                } catch (e) {
                }
            }
            // 默认展开全部节点
            officeTree.expandAll(true);
            refreshOfficeTree($("#dataScope").val());
        }
    });
}
function refreshOfficeTree(val) {
    if (val == 9) {
        $("#officeTree").show();
    } else {
        $("#officeTree").hide();
    }
}