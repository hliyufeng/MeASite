/**
 * Created by MeA on 2018/3/1.
 * version 1.0.0
 */
"use strict";
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == "") {
        url = window.location.search;
    } else {
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]);
    return null;
}


// 引入js和css文件
function include(id, path, file){
    if (document.getElementById(id)==null){
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++){
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
            var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
            document.write("<" + tag + (i==0?" id="+id:"") + attr + link + "></" + tag + ">");
        }
    }
}

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
        url = window.location.search;
    }else{
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}


function urlparam(url, param) {
    url += (url.indexOf('?') == -1 ? '?' : '&');
    var parms = $.param(param);
    url = url + parms;
    return url;
}
function getFormParams(id) {
    var d = {};
    var t = $('#' + id).serializeArray();
    $.each(t, function () {
        d[this.name] = this.value;
    });
    return d;
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
var MeA = {
    isNotEmpty: function (val) {
        if (val != null && val != "" && (typeof (val) != undefined) && val.length > 0) {
            return true;
        }
        return false;
    },
    isEmpty: function (val) {
        return !this.isNotEmpty(val);
    },
    clearForm: function (formId) {
        $(':input', '#' + formId).val('').removeAttr('checked').removeAttr('selected');
    },
    selectTree: {
        laytree: null,
        elemetsParam: [],
        defaultParam: {
            url: "",
            id: "",
            value: "",
            name: '',
            checked: "",
            isAll: false,
            labelName: "",
            labelValue: "",
            title: "",
            extId: "",
            isOpen:false,
            placeholder: "请选择",
            area: ["300px", "400px"],
            style: "width:250px",
            callbcak: "",
            notAllowSelectParent: false,
            notAllowSelectRoot: false
        },
        preeTree: function (dom, attr, i) {
            var _this = this;
            $("#" + attr.id + "Name").click(function () {
                var url1 = '/common/tree?url=' + encodeURIComponent(attr.url) +
                    '&checked=' + attr.checked + '&extId=' + attr.extId + '&isAll=' + attr.isAll + "&idN=" + attr.id + "&index=" + i+"&isOpen="+attr.isOpen;
                _this.laytree = layer.open({
                    type: 2,
                    title: attr.title,
                    maxmin: false,
                    area: attr.area,
                    shadeClose: false, // 点击遮罩关闭层
                    content: url1
                });
            });
        }
        ,
        prodom: function (dom, attr, i) {
            var html = "<input id='" + attr.id + "Id' value='" + attr.value + "' name='" + attr.name + "'  type='hidden'/>";
            html += " <input type='text' id='" + attr.id + "Name' readonly='readonly' name='" + attr.labelName + "' value='" + attr.labelValue + "'   class='layui-input newsName' placeholder='" + attr.placeholder + "' style='" + attr.style + "'>"
            $(dom).after(html);
            $(dom).remove();
        }
        ,
        getInputTreeParam: function () {
            var elemetsPara1 = [];
            var _this = this;
            var meaTree = $("[mea-input='tree']");
            $.each(meaTree, function (i, dom) {
                var empty = {};
                var param = $(dom).attr("mea-input-param");
                param = new Function('return ' + param)();
                $.extend(empty, _this.defaultParam, param);
                var obj = {dom: dom, attr: empty};
                elemetsPara1.push(obj);
            });
            this.elemetsParam = elemetsPara1;
        },
        render: function () {
            this.getInputTreeParam();
            var _this = this;
            $.each(this.elemetsParam, function (i, v) {
                var domattr = v;
                var dom = domattr.dom;
                var attr = domattr.attr;
                _this.prodom(dom, attr, i);
                _this.preeTree(dom, attr, i);
            });
        },
        init: function () {
            this.render();
        },
        oktree: function (tree, idN, checked, index) {
            var ids = [], names = [], nodes = [];
            if (checked == "true") {
                nodes = tree.getCheckedNodes(true);
            } else {
                nodes = tree.getSelectedNodes();
            }
            for (var i = 0; i < nodes.length; i++) {
                if (checked) {
                    if (nodes[i].isParent) {
                        continue;
                    }
                }
                if (this.elemetsParam[index].attr.notAllowSelectRoot) {
                    if (nodes[i].level == 0) {
                        parent.layer.msg("不能选择根节点（" + nodes[i].name + "）请重新选择。");
                    }
                }
                if (this.elemetsParam[index].attr.notAllowSelectParent) {
                    if (nodes[i].isParent) {
                        parent.layer.msg("不能选择父节点（" + nodes[i].name + "）请重新选择。");
                    }
                }
                ids.push(nodes[i].id);
                names.push(nodes[i].name);
                if (!checked) {
                    break;
                }
            }

            $("#" + idN + "Id").val(ids.join(",").replace(/u_/ig, ""));
            $("#" + idN + "Name").val(names.join(","));
            if (this.laytree != null) {
                layer.close(this.laytree);
                this.laytree = null;
            }

        },
        canceltree: function () {
            if (this.laytree != null) {
                layer.close(this.laytree);
                this.laytree = null;
            }
        }

    },
    form: {
        getChecked: function (name) {
            var adIds = "";
            $("input:checkbox[name=" + name + "]:checked").each(function (i) {
                if (0 == i) {
                    adIds = $(this).val();
                } else {
                    adIds += ("," + $(this).val());
                }
            });
            return adIds;
        }
    },
    //获取字典标签
    getDictLabel: function (data, value, defaultValue) {

        data = new Function('return ' +data)();

        for (var i = 0; i < data.length; i++) {
            var row = data[i];
            if (row.value == value) {
                return row.label;
            }
        }
        return defaultValue;
    }
}



