var prefix = "/sys/office"
$(document).ready(function () {
    load();
});
var loadTarget = null;
var load = function () {
    loadTarget = $('#officeTable')
        .bootstrapTreeTable(
            {
                id: 'id',
                code: 'id',
                parentCode: 'parentIdt',
                type: "GET", // 请求数据的ajax类型
                url: prefix + '/list', // 请求数据的ajax的url
                ajaxParams: {}, // 请求数据的ajax的data属性
                expandColumn: '0',// 在哪一列上面显示展开按钮
                rootCodeValue: '0',
                striped: true, // 是否各行渐变色
                bordered: true, // 是否显示边框
                expandAll: false, // 是否全部展开
                columns: [
                    {
                        title: '机构名称',
                        field: 'name',
                        visible: false,
                        align: 'center',
                        valign: 'center',
                        width:"200px"
                    },
                    {
                        title: '归属区域',
                        field: 'area.name',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                        formatter: function (item, index) {
                            if(MeA.isNotEmpty(item.area.name)){
                                return item.area.name;
                            }
                            return "";
                        }
                    },
                    {
                        title: '机构编码',
                        field: 'code',
                        align: 'center',
                        valign: 'center'

                    },
                    {
                        title: '排序',
                        field: 'sort',
                        visible: false,
                        align: 'right',
                        valign: 'center',
                    }
                    ,{
                        title: '机构类型',
                        field: 'type',
                        visible: false,
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            if(MeA.isNotEmpty(item.type)){
                                return MeA.getDictLabel(dicJson,item.type);
                            }
                            return "";
                        }
                    },
                    {
                        title: '备注',
                        field: 'remarks',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                    }
                    ,
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (item, index) {

                            var e = '<a class="layui-btn layui-btn-sm layui-btn-normal '
                                + s_edit_h
                                + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + item.id
                                + '\')"><i class="layui-icon">&#xe642;</i></a> ';
                            var p = '<a class="layui-btn layui-btn-warm layui-btn-sm '
                                + s_add_h
                                + '" href="#" mce_href="#" title="添加下级" onclick="add(\''
                                + item.id
                                + '\')"><i class="layui-icon">&#xe654;</i></a> ';
                            var d = '<a class="layui-btn  layui-btn-danger layui-btn-sm '
                                + s_remove_h
                                + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + item.id
                                + '\')"><i class="layui-icon">&#xe640;</i></a> ';
                            return e + d + p;
                        }
                    }
                ]
            });
}
function reLoad() {
    loadTarget.load();

}
function add(pId) {
    if(typeof (pId) == 'undefined'){
        pId = '';
    }
    layer.open({
        type: 2,
        title: '新增机构',
        maxmin: true,
        top:0,
        fixed: true,
        area: ['100%', '100%'],
        shadeClose: true, // 点击遮罩关闭层
        content: prefix + '/form?parent.id='+pId

    });
}
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/delete",
            data:{id:id},
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
        title: '修改机构',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['100%', '100%'],
        content: prefix + '/form?id=' + id // iframe的url
    });
}


