var toUpdate = '<a  id="toUpdate" data-value=""  class="toUpdate btn btn-success" >修改</a>';
var toFrozen = '<a  id="toFrozen" data-value=""  class="toFrozen btn btn btn-danger" >禁用</a>';
var toUnFrozen = '<a id="toUnFrozen" data-value=""  class="toUnFrozen btn btn-success">恢复</a>';
var toDetail = '<a id="toDetail" data-value=""  class="toDetail btn btn-success" >查看</a>';
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/menu/list',
        datatype: "json",
        colModel: [
            {label: '权限ID', name: 'menuId', width: 45, key: true},
            {label: '权限名称', name: 'name', width: 75},
            {
                label: '资源类型', name: 'type', width: 100,
                formatter: function (value, options, row) {
                    if (value == 0) {
                        return "主菜单";
                    }
                    else if (value == 1) {
                        return "子菜单";
                    }
                    else if (value == 2) {
                        return "按钮";
                    }
                }
            },
            {label: '权限标识', name: 'perms', width: 80},
            {label: '上级权限', name: 'parentName', width: 80},
            {
                label: '是否可用', name: 'status', width: 80,
                formatter: function (value, options, row) {
                    if (value == '0') {
                        return "正常";
                    }
                    else if (value == '1') {
                        return "禁用";
                    }
                    else {
                        return "-";
                    }
                }
            },
            {label: '创建时间', name: 'createtime', width: 80},
            {
                label: '管理', name: 'clueId', width: 150, formatter: operateFormatter

            }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: false,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function operateFormatter(value, options, row) {
        var reslut = toUpdate;
        reslut = reslut + (" " + toDetail);
        if (row['status'] == 0) {
            //正常状态
            reslut = reslut + (" " + toFrozen);
        } else if (row['status'] == 1) {
            //冻结状态
            reslut = reslut + (" " + toUnFrozen);
        }
        return reslut;
    }

//给行内按钮绑定事件-查看
    $("#jqGrid").on("click", ".toDetail", function () {
        var menuId = $(this).parents('tr').attr("id");
        vm.detail(menuId);
    });
//给行内按钮绑定事件-修改
    $("#jqGrid").on("click", ".toUpdate", function () {
        var menuId = $(this).parents('tr').attr("id");
        vm.update(menuId);
    });
//给行内按钮绑定事件-恢复
    $("#jqGrid").on("click", ".toUnFrozen", function () {
        var menuId = $(this).parents('tr').attr("id");
        vm.updateStatus(0, menuId);
    });
//给行内按钮绑定事件-冻结
    $("#jqGrid").on("click", ".toFrozen", function () {
        var menuId = $(this).parents('tr').attr("id");
        vm.updateStatus(1, menuId);
    });
});
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        q: {
            name: null,
            type: null,
            status: null
        },
        showList: true,
        title: null,
        flag: 0,
        firstMenuList: [],
        secondMenuList: [],
        menu: {
            menuId: null,
            status: null,
            name: null,
            perms: null,
            url: null,
            parentName: null,
            parentId: 0,
            type: null,
            firstMenu: null,
            secondMenu: null,
            orderNum: 0
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        getMenu: function () {
            //加载菜单树
            $.get(baseURL + "sys/menu/select", function (r) {
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
                ztree.selectNode(node);

                vm.menu.parentName = node.name;
            })
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.flag = 0;
            vm.menu = {
                menuId: null, status: null, name: null,
                perms: null, url: null, parentName: null, parentId: 0,
                type: null, firstMenu: null, secondMenu: null, orderNum: 0
            };
            //vm.getMenu();
            $("#url").attr("style", "display:none");
            $("#firstMenu").attr("style", "display:none");
            $("#secondMenu").attr("style", "display:none");
            vm.getFirstMenuList();
            vm.getSecondMenuList();
        },
        getFirstMenuList: function () {
            $.get(baseURL + "sys/menu/getFirstMenuList", function (r) {
                vm.firstMenuList = r.firstMenuList;
            })
        },
        getSecondMenuList: function () {
            $.get(baseURL + "sys/menu/getSecondMenuList", function (r) {
                vm.secondMenuList = r.secondMenuList;
            })
        },
        detail: function (menuId) {
            vm.showList = false;
            $.get(baseURL + "sys/menu/info/" + menuId, function (r) {
                vm.menu = r.menu;
            });
            vm.title = "查看";
            vm.getFirstMenuList();
            vm.getSecondMenuList();
            vm.flag = 1;
        },
        updateStatus: function (status, menuId) {
            $.get(baseURL + "sys/menu/updateStatus?menuId=" + menuId + "&status=" + status, function (r) {
                if (r.code == 0) {
                    alert('操作成功', function () {
                        vm.reload();
                    });
                } else {
                    alert(r.msg);
                }
            });
        },
        update: function (menuId) {
            $.get(baseURL + "sys/menu/info/" + menuId, function (r) {
                vm.showList = false;
                vm.title = "修改";
                vm.menu = r.menu;
                //vm.getMenu();
            });
            vm.getFirstMenuList();
            vm.getSecondMenuList();
        },
        del: function () {
            var menuId = getMenuId();
            if (menuId == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/menu/delete",
                    data: "menuId=" + menuId,
                    success: function (r) {
                        if (r.code === 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.menu),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        menuTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.menu.parentId = node[0].menuId;
                    vm.menu.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name, 'type': vm.q.type, 'status': vm.q.status},
                page: page
            }).trigger("reloadGrid");
        }
    }
});


var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '菜单ID', field: 'menuId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '菜单名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级菜单', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {
            title: '图标',
            field: 'icon',
            align: 'center',
            valign: 'middle',
            sortable: true,
            width: '80px',
            formatter: function (item, index) {
                return item.icon == null ? '' : '<i class="' + item.icon + ' fa-lg"></i>';
            }
        },
        {
            title: '类型',
            field: 'type',
            align: 'center',
            valign: 'middle',
            sortable: true,
            width: '100px',
            formatter: function (item, index) {
                if (item.type === 0) {
                    return '<span class="label label-primary">目录</span>';
                }
                if (item.type === 1) {
                    return '<span class="label label-success">菜单</span>';
                }
                if (item.type === 2) {
                    return '<span class="label label-warning">按钮</span>';
                }
            }
        },
        {title: '排序号', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '菜单URL', field: 'url', align: 'center', valign: 'middle', sortable: true, width: '160px'},
        {title: '授权标识', field: 'perms', align: 'center', valign: 'middle', sortable: true}]
    return columns;
};


function getMenuId() {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
        return selected[0].id;
    }
}


$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "sys/menu/list", colunms);
    table.setExpandColumn(2);
    table.setIdField("menuId");
    table.setCodeField("menuId");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.init();
    Menu.table = table;
});
