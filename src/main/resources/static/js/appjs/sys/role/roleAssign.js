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
            , url: prefix + '/assignList' //数据接口
            , method: 'post'
            , even: true
            , where: {id: idp}
            , response: responseParam
            , page: false
            , loading: true
            , cols: [[ //表头
                {title: '序号', width: 80, type: 'numbers'},
                {
                    field: 'companyName', title: '归属公司', templet: function (d) {
                    return d.company.name;
                }
                },
                {
                    field: 'officeName', title: '归属部门', templet: function (d) {
                    return d.office.name;
                }
                },
                {
                    field: 'loginName', title: '登录名'
                },
                {field: 'name', title: '姓名'},
                {field: 'phone', title: '电话'},
                {field: 'mobile', title: '手机'},
                {title: '操作', align: 'center', width: 200,templet: function (d) {
                         return ' <a class="layui-btn  layui-btn-danger layui-btn-sm '+
                             s_role_assign +'" lay-event="del"><i class="layui-icon">&#xe640;</i></a>'
                }}
            ]]
        });

        //编辑时间
        table.on('tool(measitetoolbarfiter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                remove(data.id);
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
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/outrole",
            data: {roleId: idp, userId: id},
            type: "post",
            success: function (data) {
                if (data.code == '200') {
                    layer.msg(data.msg);
                    reLoad();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    })
}
function add() {
    layer.open({
        type: 2,
        title: '分配角色',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['100%', '100%'],
        content: prefix + '/usertorole?id=' + idp// iframe的url
    });
}







