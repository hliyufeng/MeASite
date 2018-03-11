var prefix = "/sys/dict"
$(document).ready(function () {
    selectType();
    load();
    $("#search").click(function(){
        reLoad();
    });
});
function load() {
    layui.use(['table', 'layer', 'laytpl'], function () {
        var table = layui.table, layer = layui.layer, laytpl = layui.laytpl;
        //生成数据表格
        table.render({
            elem: '#measiteTable'
            , url: prefix + '/list' //数据接口
            , method: 'get'
            , response: responseParam
            , request: requestParam
            , page: {
                prev: '上一页',
                next: '下一页',
                first: '首页',
                last: '尾页'
            }
            , even: true
            , loading: true
            , cols: [[ //表头
                {title: '序号', width: 80, type: 'numbers'},
                {field: 'value', title: '键值'},
                {field: 'label', title: '标签'},
                {field: 'type', title: '类型'},
                {field: 'description', title: '描述'},
                {field: 'sort', title: '排序',sort:true},
                {title: '操作', align: 'center', width: 200, templet: function (d) {
                    var e = '<a class="layui-btn layui-btn-normal layui-btn-sm  '
                        + s_edit_h
                        + '" lay-event="edit"><i class="layui-icon">&#xe642;</i></a> ';
                    var p = '<a class="layui-btn layui-btn-warm layui-btn-sm '
                        + s_add_h
                        + '" lay-event="add"><i class="layui-icon">&#xe654;</i></a> ';
                    var d = ' <a class="layui-btn  layui-btn-danger layui-btn-sm '
                        + s_remove_h
                        + '" lay-event="del"><i class="layui-icon">&#xe640;</i></a> ';
                    return e +  p + d;
                }}
            ]]
        });

        //编辑时间
        table.on('tool(measitetoolbarfiter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                edit(data.id);
            } else if (obj.event === 'del') {
                remove(data.id);
            }else if(obj.event === 'add'){
                add(data.type,Number(Number(data.sort)+10));
            }

        });
    });
}
function reLoad() {
    layui.use('table', function () {
        var table = layui.table;
        table.reload("measiteTable",{where:getFormParams("searchForm")});
    })

}
function add(type,sort) {

    if(typeof (type) == 'undefined'){
        type = '';
    }
    if(typeof (sort) == 'undefined'){
        sort = 0;
    }
    layer.open({
        type: 2,
        title: '新增',
        maxmin: true,
        fixed: true,
        area: ['800px', '520px'],
        shadeClose: true, // 点击遮罩关闭层
        content: prefix + '/add?type='+type+'&sort='+sort
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
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit?id=' + id// iframe的url
    });
}

function selectType(){
    $.ajax({
        url: prefix + "/getType",
        type: "get",
        success: function (data) {
            var html =  "<option value=''>全部</option>";
            if (data.code == '200') {
                for (var i = 0;i<data.data.length;i++){
                    html +="<option value='"+data.data[i]+"'>"+data.data[i]+"</option>";
                }

            }
            $("#type").empty();
            $("#type").append(html);
        }
    });
}


