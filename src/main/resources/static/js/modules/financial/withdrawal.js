$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/withdrawal/list',
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
            {label: '可用金额(元)', name: 'usableMoney', width: 90},
            {label: '申请提现金额(元)', name: 'withdrawalMoney', width: 90},
            {label: '申请时间', name: 'createtime', width: 90},
            {
                label: '状态', name: 'status', width: 90, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '待处理';
                    }
                    if (value == '1') {
                        return '提现成功';
                    }
                    if (value == '2') {
                        return '提现失败';
                    }
                }
            },
            {label: '操作时间', name: 'operatingTime', width: 90}
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

    /*状态切换*/
    $('.carUl li').on("click", function () {
        $('.carUl li').removeClass("activeUl");
        $(this).addClass("activeUl");
        vm.w.status = $(this).attr("data-value");
        vm.reload();
    })
})

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        w: {
            name: null,
            contactName: null,
            contactMobile: null,
            status: null
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
        withdrawal: {
            usableMoney: null,
            withdrawalMoney: null
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
        id: null,
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
                    "name": vm.w.name,
                    "contactName": vm.w.contactName,
                    "contactMobile": vm.w.contactMobile,
                    "status": vm.w.status
                },
                page: page
            }).trigger("reloadGrid");
        },
        info: function () {
            var id = getSelectedRow();
            var type = isDealerOrSource(id);
            if (id == null) {
                return null;
            }

            this.getFinancialWithdrawalById(id, type);

            vm.id = id;
            vm.title = "详情";
            vm.showList = false;

            $(":input").attr("readonly", "readonly");
        },
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.w);
            excel(baseURL + "financial/withdrawal/excel", param);
        },
        manage: function () {
            var id = getSelectedRow();
            var type = isDealerOrSource(id);
            var status = withdrawalStatus(id);
            if (id == null) {
                return null;
            } else if (status != 0) {
                return alert("该申请已处理!");
            }

            this.getFinancialWithdrawalById(id, type);

            vm.id = id;
            vm.title = "处理";
            vm.showList = false;

            $(":input").attr("readonly", "readonly");
        },
        confirmWithdrawal: function () {
            var type = $("input[name='withdrawal']:checked").val();
            $.ajax({
                url: baseURL + "financial/withdrawal/manage",
                type: "post",
                data: {
                    "id": vm.id,
                    "withdrawalType": type
                },
                dataType: "json",
                success: function (result) {
                    if (result.count > 0) {
                        alert("提现成功!");
                        vm.reload();
                    } else {
                        alert("提现失败!");
                    }
                }
            })
        },
        getFinancialWithdrawalById: function (id, type) {
            $.get(baseURL + "financial/withdrawal/detail?id=" + id, function (r) {
                if (r.code == 0) {
                    if (type == 0) {
                        vm.partner = r.withdrawal.carDealer;
                        vm.partnerType = 0;
                        vm.partnerId = r.withdrawal.carDealer.id;
                    }
                    if (type == 1) {
                        vm.partner = r.withdrawal.carSource;
                        vm.partnerType = 1;
                        vm.partnerId = r.withdrawal.carSource.id;
                    }
                    vm.bankInfo = r.withdrawal.bankInfos;
                    vm.withdrawal = r.withdrawal.financialwithdrawal;
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

function withdrawalStatus(id) {
    var status = getSelectedColumn(id).status;
    if (status == '待处理') {
        return 0;
    } else if (status == '提现成功') {
        return 1;
    } else if (status == '提现失败') {
        return 2;
    } else {
        return null;
    }
}