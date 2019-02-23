var toUpdate = '<a  id="toUpdate" data-value=""  class="toUpdate btn btn-success" >修改</a>';
var toFrozen = '<a  id="toFrozen" data-value=""  class="toFrozen btn btn btn-danger" >冻结</a>';
var toUnFrozen = '<a id="toUnFrozen" data-value=""  class="toUnFrozen btn btn-success">解冻</a>';
var toDetail = '<a id="toDetail" data-value=""  class="toDetail btn btn-success" >查看</a>';
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [
            {label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true},
            {label: '账号', name: 'username', width: 75},
            {label: '姓名', name: 'name', width: 75},
            {label: '联系电话', name: 'mobile', width: 80},
            {label: '所属角色', name: 'roleName', width: 90},
            {label: '创建时间', name: 'createTime', width: 90},
            {
                label: '状态', name: 'status', width: 25,
                formatter: function (value, options, row) {
                    if (value == '1') {
                        return "正常";
                    }
                    else if (value == '0') {
                        return "禁用";
                    }
                    else {
                        return "-";
                    }
                }
            },
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
        reslut = reslut + (" "+toDetail);
        if (row['status'] == 0) {
            //冻结状态
            reslut = reslut + (" "+toUnFrozen);
        } else if (row['status'] == 1) {
            //正常状态
            reslut = reslut + (" "+toFrozen);
        }
        return reslut;
    }

//给行内按钮绑定事件-查看
    $("#jqGrid").on("click", ".toDetail", function () {
        var userId = $(this).parents('tr').attr("id");
        vm.detail(userId);
    });
//给行内按钮绑定事件-修改
    $("#jqGrid").on("click", ".toUpdate", function () {
        var userId = $(this).parents('tr').attr("id");
        vm.update(userId);
    });
//给行内按钮绑定事件-恢复
    $("#jqGrid").on("click", ".toUnFrozen", function () {
        var userId = $(this).parents('tr').attr("id");
        vm.updateStatus(1, userId);
    });
//给行内按钮绑定事件-冻结
    $("#jqGrid").on("click", ".toFrozen", function () {
        var userId = $(this).parents('tr').attr("id");
        vm.updateStatus(0, userId);
    });
});

var setting = {
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
var ztree;

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        q: {
            username: null
        },
        showList: true,
        title: null,
        roleList: {},
        flag: 0,
        user: {
            status: null,
            username: null,
            name: null,
            // password:null,
            cleanPassword: null,
            mobile: null,
            roleIdList: [],
            roleId: null,
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.user = {status: null, username: null, name: null, cleanPassword: "123456", mobile: null, roleIdList: [], roleId: null,};
            //获取角色信息
            this.getRoleList();
        },
        detail: function (userId) {
            vm.showList = false;
            vm.title = "查看";
            vm.flag = 1
            //获取角色信息
            this.getRoleList();
        },
        updateStatus: function (status, userId) {
            $.get(baseURL + "sys/user/updateStatus?userId=" + userId + "&status=" + status, function (r) {
                if (r.code == 0) {
                    alert('操作成功', function () {
                        vm.reload();
                    });
                } else {
                    alert(r.msg);
                }
            });
        },
        update: function (userId) {

            vm.showList = false;
            vm.title = "修改";

            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
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
        saveOrUpdate: function () {
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
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
        getUser: function (userId) {
            $.get(baseURL + "sys/user/info/" + userId, function (r) {
                vm.user = r.user;
                vm.user.status = r.user.status;
                vm.user.username = r.user.username;
                vm.user.name = r.user.name;
                vm.user.mobile = r.user.mobile;
                vm.user.cleanPassword = r.user.cleanPassword;
                vm.user.userId = r.user.userId;
                vm.user.roleId = r.user.roleId;
            });
        },
        exportUser:function () {
            //导出用户
            var url="sys/user/export";
            var params = jQuery.param(vm.q);
            excel(baseURL+url,params);
        },
        getRoleList: function () {
            $.get(baseURL + "sys/role/select", function (r) {
                vm.roleList = r.list;
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'username': vm.q.username, 'name': vm.q.name, 'mobile': vm.q.mobile, 'status': vm.q.status},
                page: page
            }).trigger("reloadGrid");
        }
    }
});