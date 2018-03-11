var prefix = "/gen"
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
                {field: 'tableName', title: '表名称'},
                {field: 'engine', title: 'engine'},
                {field: 'tableComment', title: '表描述'},
                {field: 'createTime', title: '创建时间'},
                {title: '操作', align: 'center', toolbar: '#operationTpl', width: 200}
            ]]
        });

        //编辑时间
        table.on('tool(measitetoolbarfiter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'eye') {
                view(data.tableName);
            } else if (obj.event === 'download') {
                download(data.tableName);
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
function view(tableName) {

    layer.open({
        type: 2,
        title: '查看表详细',
        maxmin: true,
        fixed: true,
        area: ['100%', '100%'],
        shadeClose: true, // 点击遮罩关闭层
        content: prefix + '/tableColumn?tableName=' + tableName
    });
}

function download(tableName){
    window.location.href = prefix + "/code/" + tableName;
}


