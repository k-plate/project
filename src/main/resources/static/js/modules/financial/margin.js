$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/margin/list',
        datatype: "json",
        colModel: [
            {label: '序号', name: 'id', width: 20, key: true},
            {label: '合作方ID', name: 'partnerId', width: 30, hidden: true},
            {label: '合作方名称', name: 'partnerName', width: 45},
            {
                label: '合作方类型', name: 'partnerType', width: 45, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '车商';
                    }
                    if (value == '1') {
                        return '车源方';
                    }
                }
            },
            {label: '联系人姓名', name: 'partnerContactName', width: 75},
            {label: '联系方式', name: 'partnerContactMobile', width: 75},
            {label: '充值总金额(元)', name: 'topUpMoney', width: 90},
            {label: '提现总金额(元)', name: 'withdrawalMoney', width: 80},
            {label: '可用保证金(元)', name: 'usableMoney', width: 90},
            {label: '冻结保证金(元)', name: 'freezeMoney', width: 90},
            {label: '消费总金额(元)', name: 'consumptionMoney', width: 90},
            {label: '违约金额(元)', name: 'defaultMoney', width: 90},
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
})

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        m: {
            name: null,
            contactName: null,
            contactMobile: null
        },
        partner: {
            id: null,
            name: null,
            contactName: null,
            contactMobile: null,
            code: null,
            address: null
            // derectorName: null,
        },
        bankInfo: [
            // {businessOrPersonnel: null},
            // {bankNumber: null},
            // {bankName: null},
            // {subBranchName: null},
            // {openAccountName: null}
        ],
        financialMarginLogs: [
            {createtime: null},
            {adminUserName: null},
            {status: null},
            {money: null},
            {remark: null},
        ],
        title: "新增",
        status: null,
        partnerId: null,
        partnerType: null,
        showList: true
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reload: function () {
            $(":input").attr("readonly", false);
            $("select").attr("disabled", false);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "name": vm.m.name,
                    "contactName": vm.m.contactName,
                    "contactMobile": vm.m.contactMobile
                },
                page: page
            }).trigger("reloadGrid");
        },
        info: function () {
            var id = getSelectedRow();
            var type = isDealerOrSource(id);
            var partnerId = getSelectedColumn(id).partnerId;
            if (id == null) {
                return null;
            }

            this.getFinancialMarginById(partnerId, type);

            vm.title = "详情";
            vm.showList = false;

            $(":input").attr("readonly", "readonly");
        },
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.c);
            excel(baseURL + "financial/margin/excel",param)
        },
        extract: function () {
            var id = getSelectedRow();
            var type = isDealerOrSource(id);
            var partnerId = getSelectedColumn(id).partnerId;
            if (id == null) {
                return null;
            }

            this.getFinancialMarginById(partnerId, type);

            vm.title = "提现";
            vm.showList = false;

            $(":input").attr("readonly", "readonly");
        },
        top_up: function () {
            var id = getSelectedRow();
            var type = isDealerOrSource(id);
            var partnerId = getSelectedColumn(id).partnerId;
            if (id == null) {
                return null;
            }

            this.getFinancialMarginById(partnerId, type);

            vm.title = "充值";
            vm.showList = false;

            $(":input").attr("readonly", "readonly");
        },
        confirmTopUp: function () {
            $.ajax({
                url: baseURL + "financial/margin/topUp",
                type: "post",
                data: {
                    "partnerId": vm.partnerId,
                    "partnerType": vm.partnerType,
                    "price": $("#top_price").val()
                },
                dataType: "json",
                success: function (result) {
                    if (result.count > 0) {
                        alert("充值成功!");
                        vm.reload();
                    } else {
                        alert("充值失败!");
                    }
                }
            })
        },
        confirmWithdrawal: function () {
            $.ajax({
                url: baseURL + "financial/margin/withdrawal",
                type: "post",
                data: {
                    "partnerId": vm.partnerId,
                    "partnerType": vm.partnerType,
                    "price": $("#with_price").val(),
                    "remark": $("#remark").val()
                },
                dataType: "json",
                success:

                    function (result) {
                        if (result.count > 0) {
                            alert("提现成功!");
                            vm.reload();
                        } else {
                            alert("提现失败!");
                        }
                    }
            })
        },
        getFinancialMarginById: function (id, type) {
            $.get(baseURL + "financial/margin/detail?partnerId=" + parseInt(id) + "&partnerType=" + type, function (r) {
                if (r.code == 0) {
                    if (type == 0) {
                        vm.partner = r.marginInfo.carDealer;
                        vm.partnerType = 0;
                        vm.partnerId = r.marginInfo.carDealer.id;
                    }
                    if (type == 1) {
                        vm.partner = r.marginInfo.carSource;
                        vm.partnerType = 1;
                        vm.partnerId = r.marginInfo.carSource.id;
                    }
                    vm.bankInfo = r.marginInfo.bankInfo;
                    vm.financialMarginLogs = r.marginInfo.financialMarginLogs;
                } else {
                    alert(r.msg)
                }
            })
        }
    }
});

/**
 * 判断当前选中的是车源方还是车商
 * @param id
 * @returns {*}
 */
function isDealerOrSource(id) {
    var type = getSelectedColumn(id);
    var partnerType = type.partnerType;
    if (partnerType == '车商') {
        return 0;
    } else if (partnerType == '车源方') {
        return 1;
    } else {
        return null;
    }
}