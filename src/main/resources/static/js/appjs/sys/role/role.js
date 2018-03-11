var prefix = "/sys/role"
$(document).ready(function () {
    load();
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
            , page: false
            , loading: true
            , cols: [[ //表头
                {title: '序号', width: 80, type: 'numbers'},
                {field: 'name', title: '角色名称'},
                {field: 'enname', title: '英文名称'},
                {
                    field: 'officeName', title: '归属机构', templet: function (d) {
                    return d.office.name;
                }
                },
                {field: 'dataScopeLable', title: '数据范围'},
                {
                    title: '操作', align: 'center', width: 200, templet: function (d) {
                     var e = '';
                    if (d.isAuth == '1') {
                        e = '<a class="layui-btn  layui-btn-sm  '
                            + s_role
                            + '"  lay-event="role"><i class="layui-icon">&#xe612;</i></a> ';
                    }
                    var p = '<a  class="layui-btn layui-btn-normal  layui-btn-sm '
                        + s_edit_h
                        + '" lay-event="edit"><i class="layui-icon">&#xe642;</i></a> ';
                    var d = '<a class="layui-btn  layui-btn-danger layui-btn-sm '
                        + s_remove_h
                        + '" lay-event="del"><i class="layui-icon">&#xe640;</i></a> ';
                    return e +  p + d;
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
                remove(data.id, table);
            } else if (obj.event === 'role') {
                distributionRole(data.id);
            }

        });
    });
}
function reLoad() {
    layui.use('table', function () {
        var table = layui.table;
        table.reload("measitetable")
    })

}
function add(pId) {
    if (typeof (pId) == 'undefined') {
        pId = '';
    }
    layer.open({
        type: 2,
        title: '新增角色',
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
        title: '修改角色',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['100%', '100%'],
        content: prefix + '/form?id=' + id// iframe的url
    });
}

/**
 * 分配角色
 * @param id
 */
function distributionRole(id) {
    layer.open({
        type: 2,
        title: '分配角色',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['100%', '100%'],
        content: prefix + '/assign?id=' + id// iframe的url
    });
}




