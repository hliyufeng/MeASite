var prefix = "/sys/user"
$(document).ready(function () {
    MeA.selectTree.init();
    load();
    $("#search").click(function () {
        reLoad();
    });
    $("#clearseach").click(function () {
        MeA.clearForm("searchForm");
    });

});
function load() {
    layui.use(['table', 'layer', 'laytpl'], function () {
        var table = layui.table, layer = layui.layer, laytpl = layui.laytpl;
        //生成数据表格
        table.render({
            elem: '#measitetable'
            , url: prefix + '/list' //数据接口
            , method: 'post'
            , even: true
            , response: responseParam
            , request: requestParam
            , page: {
                prev: '上一页',
                next: '下一页',
                first: '首页',
                last: '尾页'
            }
            , loading: true
            , cols: [[ //表头
                {title: '序号', width: 80, type: 'numbers'},
                {
                    field: 'companyName', title: '归属公司', templet: function (d) {
                    if (MeA.isNotEmpty(d.company.name)) {
                        return d.company.name;
                    }
                    return "";
                }
                },
                {
                    field: 'officeName', title: '归属部门', templet: function (d) {
                    if (MeA.isNotEmpty(d.office.name)) {
                        return d.office.name;
                    }
                    return ""
                }
                },
                {
                    field: 'loginName', title: '登录名'
                },
                {field: 'name', title: '姓名'},
                {field: 'phone', title: '电话'},
                {field: 'mobile', title: '手机'},
                {
                    title: '操作', align: 'center', width: 200, templet: function (d) {
                    var p = '<a  class="layui-btn layui-btn-normal  layui-btn-sm '
                        + s_edit_h
                        + '" lay-event="edit"><i class="layui-icon">&#xe642;</i></a> ';
                    var d = '<a class="layui-btn  layui-btn-danger layui-btn-sm '
                        + s_remove_h
                        + '" lay-event="del"><i class="layui-icon">&#xe640;</i></a> ';
                    return  p + d;
                }
                }
            ]]
        });

        //编辑时间
        table.on('tool(measitetoolbarfiter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                edit(data.id);
            } else if (obj.event === 'del') {
                remove(data.id);
            }

        });
    });
}
function reLoad() {
    layui.use('table', function () {
        var table = layui.table;
        table.reload("measitetable", {where: getFormParams("searchForm")})
    })

}
function add(pId) {
    if (typeof (pId) == 'undefined') {
        pId = '';
    }
    layer.open({
        type: 2,
        title: '新增用户',
        maxmin: false,
        area: ['100%', '100%'],
        shadeClose: true, // 点击遮罩关闭层
        content: prefix + '/form'
    });
}
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/delete",
            data: {id: id},
            type: "post",
            success: function (data) {
                if (data.code == '200') {
                    layer.msg(data.msg);
                    reLoad();
                }
            }
        });
    })
}
function edit(id) {
    layer.open({
        type: 2,
        title: '修改用户',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['100%', '100%'],
        content: prefix + '/form?id=' + id// iframe的url
    });

}




