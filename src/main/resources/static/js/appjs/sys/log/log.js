var prefix = "/sys/log"
$(document).ready(function () {

    load();
    $("#search").click(function () {
        reLoad();
    });
    $("#clearseach").click(function () {
        MeA.clearForm("searchForm");
    });

});
function load() {
    layui.use(['table', 'layer', 'laydate'], function () {
        var table = layui.table, layer = layui.layer,laydate = layui.laydate;

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
                {field: 'operation', title: '日志标题'},
                {field: 'requestUri', title: '访问地址'},
                {field: 'userAgent', title: '请求信息'},
                {field: 'remoteAddr', title: '请求IP'},
                {field: 'method', title: '请求方式'},
                {field: 'methodName', title: '请求方法'},
                {
                    field: 'requestTime', title: '请求时间', templet: function (d) {
                        return new Date(d.requestTime).Format("yyyy-MM-dd hh:mm:ss");;
                }
                },
                {field: 'responseTime', title: '作答时间', templet: function (d) {
                    return new Date(d.responseTime).Format("yyyy-MM-dd hh:mm:ss");
                }},
                {field: 'time', title: '响应时间'}

            ]]
        });

        laydate.render({
            elem: '#beginDate'
            ,type: 'datetime'
        });
        laydate.render({
            elem: '#endDate'
            ,type: 'datetime'
        });
    });
}
function reLoad() {
    layui.use('table', function () {
        var table = layui.table;
        table.reload("measitetable", {where: getFormParams("searchForm")})
    })

}






