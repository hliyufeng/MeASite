var prefix = "/sys/menu"
$(document).ready(function () {
    load();
});
var loadTarget = null;
var load = function () {
    loadTarget = $('#menuTable')
        .bootstrapTreeTable(
            {
                id: 'id',
                code: 'id',
                parentCode: 'parentId',
                type: "GET", // 请求数据的ajax类型
                url: prefix + '/list', // 请求数据的ajax的url
                ajaxParams: {}, // 请求数据的ajax的data属性
                expandColumn: '0',// 在哪一列上面显示展开按钮
                rootCodeValue: '1',
                striped: true, // 是否各行渐变色
                bordered: true, // 是否显示边框
                expandAll: false, // 是否全部展开
                columns: [
                    {
                        title: '名称',
                        field: 'name',
                        visible: false,
                        align: 'center',
                        valign: 'center',
                        width:"200px"
                    },
                    {
                        title: '链接',
                        field: 'href',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                    },
                    {
                        title: '类型',
                        field: 'type',
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            if (item.type == '1') {
                                return '<span class="label label-primary">目录</span>';
                            }
                            if (item.type == '2') {
                                return '<span class="label label-success">菜单</span>';
                            }
                            if (item.type == '3') {
                                return '<span class="label label-warning">按钮</span>';
                            }
                            if (item.type == '4') {
                                return '<span class="label label-warning">URL</span>';
                            }
                            return "";
                        }
                    },
                    {
                        title: '排序',
                        field: 'sort',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                    }
                    ,{
                        title: '可见',
                        field: 'show',
                        visible: false,
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            var showt= "";
                            if(item.show == '0'){
                                showt = '<span class="label label-primary">隐藏</span>';
                            }else if(item.show == '1'){
                                showt = '<span class="label label-success">显示</span>';
                            }
                            return showt;
                        }
                    },
                    {
                        title: '图标',
                        field: 'icon',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                    },
                    {
                        title: '权限标识',
                        field: 'permission',
                        visible: false,
                        align: 'left',
                        valign: 'center',
                    }
                    ,
                    {
                        title: '操作',
                        field: 'gnid',
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
            title: '增加菜单',
            maxmin: true,
            fixed: true,
            area: ['800px', '520px'],
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
        title: '修改菜单',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/form?id=' + id // iframe的url
    });
}


