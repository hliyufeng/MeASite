/**
 * Created by lenovo on 2018/3/6.
 */
var officeTree;
var selectedTree;//zTree已选择对象
var officeNodes = [];
var pre_selectedNodes = [];
var selectedNodes = [];
var pre_ids = "";
var ids = "";
var prefix = "/sys/role"
var idsArr = "";
// 初始化
$(document).ready(function () {
    initUserOfficeList();
});
var setting = {
    view: {selectedMulti: false, nameIsHTML: true, showTitle: false, dblClickExpand: false},
    data: {simpleData: {enable: true}},
    callback: {onClick: treeOnClick}
};
//点击选择项回调
function treeOnClick(event, treeId, treeNode, clickFlag) {
    $.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
    if ("officeTree" == treeId) {
        $.get(prefix + "/users?officeId=" + treeNode.id, function (data) {
            if (data.code == 200) {
                $.fn.zTree.init($("#userTree"), setting, data.data);
            }

        });
    }
    if ("userTree" == treeId) {
        if ($.inArray(String(treeNode.id), ids) < 0) {
            selectedTree.addNodes(null, treeNode);
            ids.push(String(treeNode.id));
        }
    }
    ;
    if ("selectedTree" == treeId) {
        if ($.inArray(String(treeNode.id), pre_ids) < 0) {
            selectedTree.removeNode(treeNode);
            ids.splice($.inArray(String(treeNode.id), ids), 1);
        } else {
            layer.msg("角色原有成员不能清除！");
        }
    }
};
function clearAssign() {

    layer.confirm("确定清除角色【"+roleName +"】下的已选人员？", {
        btn: ['确认', '取消'] //按钮

    }, function () {
        var tips = "";
        if (pre_ids.sort().toString() == ids.sort().toString()) {
            tips = "未给角色【"+roleName +"】分配新成员！";
        } else {
            tips = "已选人员清除成功！";
        }
        ids = pre_ids.slice(0);
        selectedNodes = pre_selectedNodes;
        $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
        layer.msg(tips);
    }, function () {
        layer.msg("取消清除操作!");
    });

};

/**
 * 初始化数据
 */
function initUserOfficeList() {
    $.ajax({
        url: prefix + "/userOfficeList",
        data: {id: idp},
        type: "post",
        success: function (data) {
            if (data.code == '200') {
                officeList = data.data.officeList;
                userList = data.data.userList;
                selectIds1 = data.data.selectIds;
                officeNodes = officeList;
                if (MeA.isNotEmpty(selectIds1)) {
                    pre_ids = selectIds1.split(",");
                    ids = selectIds1.split(",");
                }
                if (MeA.isNotEmpty(userList)) {
                    for (var i = 0; i < userList.length; i++) {
                        var s = {
                            id: userList[i].id,
                            pId: "0",
                            name: "<font color='red' style='font-weight:bold;'>" + userList[i].name + "</font>"
                        }
                        pre_selectedNodes.push(s);
                        selectedNodes.push(s);
                    }

                }

            }
            officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
            selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
        }
    });
}
function enter() {
    // 删除''的元素
    if (ids[0] == '') {
        ids.shift();
        pre_ids.shift();
    }
    if (pre_ids.sort().toString() == ids.sort().toString()) {
        layer.msg("未给角色"+roleName +"分配新成员！");
        return false;
    }
    ;
    // 执行保存
    for (var i = 0; i < ids.length; i++) {
        idsArr = (idsArr + ids[i]) + (((i + 1) == ids.length) ? '' : ',');
    }
    var index = layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
    $.ajax({
        url: prefix + "/assignrole",
        data: {id: idp, idsArr: idsArr},
        type: "post",
        success: function (data) {
            layer.close(index);
            if (data.code == '200') {
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
                parent.reLoad();
                parent.layer.msg(data.msg);
            } else {
                layer.msg(data.msg);
            }

        }
    });
}