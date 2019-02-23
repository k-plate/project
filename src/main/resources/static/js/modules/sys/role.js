var toUpdate = '<a  id="toUpdate" data-value=""  class="toUpdate btn btn-success" >修改</a>';
var toFrozen = '<a  id="toFrozen" data-value=""  class="toFrozen btn btn btn-danger" >禁用</a>';
var toUnFrozen = '<a id="toUnFrozen" data-value=""  class="toUnFrozen btn btn-success">恢复</a>';
var toDetail = '<a id="toDetail" data-value=""  class="toDetail btn btn-success" >查看</a>';
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/role/list',
        datatype: "json",
        colModel: [
            {label: '角色ID', name: 'roleId', index: "role_id", width: 45, key: true},
            {label: '角色名称', name: 'roleName', index: "role_name", width: 75},
            {label: '角色描述', name: 'remark', width: 100},
            {
                label: '状态', name: 'status', width: 25,
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
            {label: '创建时间', name: 'createTime', index: "create_time", width: 80},
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
        var roleId = $(this).parents('tr').attr("id");
        vm.detail(roleId);
    });
//给行内按钮绑定事件-修改
    $("#jqGrid").on("click", ".toUpdate", function () {
        var roleId = $(this).parents('tr').attr("id");
        vm.update(roleId);
    });
//给行内按钮绑定事件-恢复
    $("#jqGrid").on("click", ".toUnFrozen", function () {
        var roleId = $(this).parents('tr').attr("id");
        vm.updateStatus(0, roleId);
    });
//给行内按钮绑定事件-冻结
    $("#jqGrid").on("click", ".toFrozen", function () {
        var roleId = $(this).parents('tr').attr("id");
        vm.updateStatus(1, roleId);
    });
});

//菜单树
var menu_ztree;
var menu_setting = {
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
    },
    check: {
        enable: true,
        nocheckInherit: true
    }
};

//部门结构树
var dept_ztree;
var dept_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};

//数据树
var data_ztree;
var data_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    },
    check: {
        enable: true,
        nocheckInherit: true,
        chkboxType: {"Y": "", "N": ""}
    }
};

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        q: {
            roleName: null,
            status: null,
        },
        showList: true,
        title: null,
        flag: 0,
        role: {
            roleId: null,
            roleName: null,
            remark: null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.role = {roleId: null, roleName: null, remark: null};
            vm.getMenuTree(null);
        },
        update: function (roleId) {
            vm.showList = false;
            vm.title = "修改";
            vm.getMenuTree(roleId);
            vm.flag = 0;
        },
        detail: function (roleId) {
            vm.showList = false;
            vm.title = "查看";
            vm.getMenuTree(roleId);
            vm.flag = 1;
        },
        updateStatus: function (status, roleId) {
            $.get(baseURL + "sys/role/updateStatus?roleId=" + roleId + "&status=" + status, function (r) {
                if (r.code == 0) {
                    alert('操作成功', function () {
                        vm.reload();
                    });
                } else {
                    alert(r.msg);
                }
            });
        },
        del: function () {
            var roleIds = getSelectedRows();
            if (roleIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/role/delete",
                    contentType: "application/json",
                    data: JSON.stringify(roleIds),
                    success: function (r) {
                        if (r.code == 0) {
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
        getRole: function (roleId) {
            $.get(baseURL + "sys/role/info/" + roleId, function (r) {
                vm.role = r.role;

                //勾选角色所拥有的菜单
                var menuIds = vm.role.menuIdList;
                for (var i = 0; i < menuIds.length; i++) {
                    var node = menu_ztree.getNodeByParam("menuId", menuIds[i]);
                    menu_ztree.checkNode(node, true, false);
                }

                /* //勾选角色所拥有的部门数据权限
                 var deptIds = vm.role.deptIdList;
                 for(var i=0; i<deptIds.length; i++) {
                     var node = data_ztree.getNodeByParam("deptId", deptIds[i]);
                     data_ztree.checkNode(node, true, false);
                 }*/

                /*vm.getDept();*/
            });
        },
        saveOrUpdate: function () {
            //获取选择的菜单
            var nodes = menu_ztree.getCheckedNodes(true);
            var menuIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                menuIdList.push(nodes[i].menuId);
            }
            vm.role.menuIdList = menuIdList;

            /*  //获取选择的数据
              var nodes = data_ztree.getCheckedNodes(true);
              var deptIdList = new Array();
              for(var i=0; i<nodes.length; i++) {
                  deptIdList.push(nodes[i].deptId);
              }
              vm.role.deptIdList = deptIdList;*/

            var url = vm.role.roleId == null ? "sys/role/save" : "sys/role/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.role),
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
        getMenuTree: function (roleId) {
            //加载菜单树
            $.get(baseURL + "sys/menu/listTree", function (r) {
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
                //展开所有节点
                menu_ztree.expandAll(true);

                if (roleId != null) {
                    vm.getRole(roleId);
                }
            });
        },
        /* getDataTree: function(roleId) {
             //加载菜单树
             $.get(baseURL + "sys/dept/list", function(r){
                 data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r);
                 //展开所有节点
                 data_ztree.expandAll(true);
             });
         },*/
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/list", function (r) {
                dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
                var node = dept_ztree.getNodeByParam("deptId", vm.role.deptId);
                if (node != null) {
                    dept_ztree.selectNode(node);

                    vm.role.deptName = node.name;
                }
            })
        },
        deptTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = dept_ztree.getSelectedNodes();
                    //选择上级部门
                    vm.role.deptId = node[0].deptId;
                    vm.role.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'roleName': vm.q.roleName, 'status': vm.q.status},
                page: page
            }).trigger("reloadGrid");
        }
    }
});