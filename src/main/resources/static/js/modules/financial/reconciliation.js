$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/reconciliation/list',
        datatype: "json",
        colModel: [
            {label: '序号', name: 'id', width: 20, hidden: true},
            {label: '支付编号', name: 'payCode', width: 80, key: true},
            {label: '车商ID', name: 'partnerId', width: 30, hidden: true},
            {label: '车商名称', name: 'partnerName', width: 45},
            // {
            //     label: '合作方类型', name: 'partnerType', width: 45, formatter: function (value, options, row) {
            //         if (value == '0') {
            //             return '车商';
            //         }
            //         if (value == '1') {
            //             return '车源方';
            //         }
            //     }
            // },
            {label: '联系人姓名', name: 'contactName', width: 75},
            {label: '联系方式', name: 'contactMobile', width: 75},
            {label: '充值金额(元)', name: 'topUpMoney', width: 50},
            {label: '银行卡号', name: 'bankCode', width: 80},
            {label: '所属银行', name: 'bankName', width: 90},
            {label: '申请时间', name: 'createtime', width: 90},
            {
                label: '充值渠道', name: 'topUpChannels', width: 50, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '在线充值';
                    }
                    if (value == '1') {
                        return '后台充值';
                    }
                }
            },
            {
                label: '状态', name: 'status', width: 40, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '成功';
                    }
                    if (value == '1') {
                        return '失败';
                    }
                }
            },
        ],
        viewrecords: true,
        height: 385,
        rowNum: 6,
        rowList: [10, 30, 50],
        rownumbers: false,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
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
    laydate.render({
        elem: '#datetimeStart',
        type: 'datetime',
        done: function (value, date, endDate) {//控件选择完毕后的回调---点击日期、清空、现在、确定均会触发。
            vm.r.datetimeStart = value;
        }
    })
    laydate.render({
        elem: '#datetimeEnd',
        type: 'datetime',
        done: function (value, date, endDate) {//控件选择完毕后的回调---点击日期、清空、现在、确定均会触发。
            vm.r.datetimeEnd = value;
        }
    })
})

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        r: {
            payCode: null,
            name: null,
            contactName: null,
            topUpChannels: null,
            datetimeStart: null,
            datetimeEnd: null
        },
        title: "新增",
        showList: true
    },
    methods: {
        query: function () {
            vm.reload();
        },
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.r);
            excel(baseURL + "financial/reconciliation/excel", param);
        },
        reload: function () {
            $(":input").attr("readonly", false);
            $("select").attr("disabled", false);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "payCode": vm.r.payCode,
                    "name": vm.r.name,
                    "contactName": vm.r.contactName,
                    "topUpChannels": vm.r.topUpChannels,
                    "datetimeStart": vm.r.datetimeStart,
                    "datetimeEnd": vm.r.datetimeEnd
                },
                page: page
            }).trigger("reloadGrid");
        },
    }
});
