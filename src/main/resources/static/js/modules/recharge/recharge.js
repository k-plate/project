$(function () {


    $("#jqGrid").jqGrid({
        url: baseURL + 'recharge/recharge/list',
        datatype: "json",
        colModel: [
            {name:'id',index:'id', hidden:true,},
            {label: '编号', name: 'rechargeCode', width: 45,},
            {label: '交易账号', name: 'userId', width: 75},
            {label: '交易姓名', name: 'userName', width: 75},
            {label: '操作时间', name: 'updateTime', width: 110},
            {label: '金额', name: 'rechargeMoney', width: 60},
            {label: '会员账户余额', name: 'rechargeBalance', width: 80},
            {label: '备注', name: 'remark', width: 90},
            {label: '审核时间', name: 'auditTime', width: 110},
            {label: '审核状态', name: 'auditStatus', width: 90,
                formatter: function (value, options, row) {
                            if (value == '0') {
                                return "拒绝";
                            }
                            else if (value == '1') {
                                return "通过";
                            }
                            else if(value=='2'){
                                return "充值中";
                            }else{
                                return '- -';
                            }
                        }},
            {label: '操作', name: 'cz', width: 50,formatter:operateFormatter}
            // {
            //     label: '状态', name: 'status', width: 25,
            //     formatter: function (value, options, row) {
            //         if (value == '1') {
            //             return "正常";
            //         }
            //         else if (value == '0') {
            //             return "禁用";
            //         }
            //         else {
            //             return "-";
            //         }
            //     }
            // },
            // {
            //     label: '管理', name: 'clueId', width: 150, formatter: operateFormatter
            //
            // }
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
        var reslut = "<a id='audit' data-value='' class='btn btn-success'>审核</a>";
        return reslut;
    }

//给行内按钮绑定事件-审核
    $("#jqGrid").on("click", "#audit", function () {
        var rechargeId = $(this).parents('tr').attr("id");
        vm.detail(rechargeId);
    });

    laydate.render({
        elem: '#starttime',
        type:'datetime',//指定元素
        done: function(value, date, endDate) {
                vm.params.starttime = value;
        }
    });

    laydate.render({
        elem:'#endtime',
        type:'datetime',
        done: function(value, date, endDate) {
            vm.params.endtime = value;
        }
    })
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
            username: null,
            starttime:null,
            endtime:null
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
        detail: function (rechargeId) {
            vm.showList = false;
            vm.title = "审核";
            vm.flag = 1
            //获取角色信息
            this.getRoleList();
        },
        saveOrUpdate:function(){

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