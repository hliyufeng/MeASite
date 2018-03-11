var prefix = "/gen"
$(document).ready(function () {
    load();
});
function load() {
    layui.use(['table', 'layer', 'laytpl'], function () {
        var table = layui.table, layer = layui.layer, laytpl = layui.laytpl;
        var $ = layui.jquery;
        //生成数据表格
        table.render({
            elem: '#measitetable'
            , url: prefix + '/listColumn' //数据接口
            , method: 'post'
            , even: true
            , where: {"tableName": tableName}
            , response: responseParam
            , page: false
            , loading: true
            , cols: [[ //表头
                {field: 'columnName', title: '列名'},
                {field: 'dataType', title: '类型'},
                {field: 'columnComment', title: '描述'},
                {field: 'columnKey', title: '主键类型'},
                {field: 'extra', title: '额外说明'},
                {field: 'columnType', title: '类型'},
                {field: 'isNullable', title: '是否为空'},
                {field: 'columnDefault', title: '默认值'}
            ]]
        });
    });
}





