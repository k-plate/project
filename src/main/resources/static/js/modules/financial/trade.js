$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/trade/list',
        datatype: "json",
        colModel: [
            {label: '拍品编号', name: 'goodsCode', width: 20, key: true},
            {label: '包编号', name: 'truckPackCode', width: 45},
            {label: '车型', name: 'truckModel', width: 75},
            {label: '品牌车系', name: 'truckBrandSeries', width: 75},
            {label: '买家姓名', name: 'carDealerName', width: 90},
            {label: '买家联系人姓名', name: 'contactName', width: 80},
            {label: '买家联系方式', name: 'contactMobile', width: 90},
            {label: '金额(元)', name: 'price', width: 90},
            {label: '创建时间', name: 'createtime', width: 90}
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
        t: {
            goodsCode: null,
            carDealerName: null,
            contactName: null,
            contactMobile: null
        },
        car_dealer: {
            name: null,
            contactName: null,
            contactMobile: null,
            code: null,
            address: null,
            merchantId: null,
            source: null,
            status: null,
            rebate: null
        },
        bankInfo: [
            // {businessOrPersonnel: null},
            // {bankNumber: null},
            // {bankName: null},
            // {subBranchName: null},
            // {openAccountName: null}
        ],
        car_source_record_logs: [
            {createtime: null},
            {userId: null},
            {status: null},
            {remarks: null}
        ],
        title: "新增",
        showList: true
    },
    methods: {
        query: function () {
            vm.reload();
        },
        info: function () {
            var id = getSelectedRow();
            if (id == null) {
                return null;
            }

            vm.showList = false;
            vm.title = "详情";

            this.getCarSourceById(id);
            $(":input").attr("readonly", true);
        },
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.t);
            excel(baseURL + "financial/trade/excel", param);
        },
        getCarSourceById: function (id) {
            $.get(baseURL + "financial/trade/detail/" + id, function (r) {
                vm.car_dealer = r.carDealer;
                vm.bankInfo = r.bankInfo;
            });
        },
        reload: function () {
            $(":input").attr("readonly", false);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'goodsCode': vm.d.goodsCode,
                    'contactName': vm.d.contactName,
                    'contactMobile': vm.d.contactMobile,
                    'carDealerName': vm.d.carDealerName
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});