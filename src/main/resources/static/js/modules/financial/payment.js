var detail = '<a class="info btn btn-info">查看</a>';
var confirmPaymentShow = '<a class="confirmPaymentShow btn btn-info">确认付款</a>';
var paymentErrorShow = '<a class="paymentErrorShow btn btn-info">付款失败</a>';
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/payment/list',
        datatype: "json",
        colModel: [
            {label: '编号', name: 'paymentCode', width: 60, key: true},
            {label: '拍品编号/拍品包编号', name: 'goodsCode', width: 60},
            {label: '包编号', name: 'truckPackCode', width: 60},
            {label: '车型', name: 'truckModel', width: 45},
            {label: '品牌车系', name: 'truckBrandSeries', width: 75},
            {label: '车源方委托方', name: 'carSourceName', width: 75},
            {label: '车源委托方联系人', name: 'contactName', width: 80},
            {label: '车源委托方联系电话', name: 'contactMobile', width: 80},
            {label: '付款金额(万元)', name: 'paymentPrice', width: 40},
            {label: '付款时间', name: 'paymentTime', width: 40},
            {
                label: '状态', name: 'status', width: 40, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '待付款';
                    }
                    if (value == '1') {
                        return '付款完成';
                    }
                    if (value == '2') {
                        return '付款失败';
                    }
                }
            },
            {label: '操作', name: 'status', width: 80, formatter: operatButtons}
        ],
        viewrecords: true,
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

    //自定义按钮
    function operatButtons(value, options, row) {
        var result = detail;
        if (value == '0') {
            result = result + " " + confirmPaymentShow;
            result = result + " " + paymentErrorShow;
        }
        return result;
    }
    /*状态切换*/
    $('.carUl li').on("click", function () {
        $('.carUl li').removeClass("activeUl");
        $(this).addClass("activeUl");
        vm.p.status = $(this).attr("data-value");
        vm.reload();
    });
    //加载时获取车型列表
    $.get(baseURL + "dictionary/getAllModels", function (result) {
        vm.models = result.data;
    })
    //加载时获取品牌列表
    $.get(baseURL + "dictionary/getAllBrands", function (result) {
        vm.brands = result.data;
    })
    //通过品牌id获取车系
    $("#brand").on("change", function () {
        var brandId = $(this).val();
        $.get(baseURL + "dictionary/getSeriesByBrandId", {"brandId": brandId}, function (result) {
            vm.series = result.data;
        }, "json")
    })
})

var vm = new Vue({
    el: '#honghu_cloud',
    data: {
        p: {
            paymentCode: null,
            truckModel: null,
            truckBrand: null,
            truckSeries: null,
            goodsCode: null,
            minPrice: null,
            maxPrice: null,
            status: null,
            carSourceName: null,
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
        financial_payment: {},
        car_source: {},
        good: {},
        bankInfo: [],
        paymentLog: {},
        models: [],
        brands: [],
        series: [],
        aut_goods: [
            {truckInfoId: null},
            {truckBrandSeries: null},
            {truckModel: null},
            {truckAddress: null},
            {carSourceContactName: null}
        ],
        title: "新增",
        status: null,
        partnerId: null,
        partnerType: null,
        paymentModel: true,
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
                    "paymentCode": vm.p.paymentCode,
                    "truckModel": vm.p.truckModel,
                    "truckBrand": vm.p.truckBrand,
                    "truckSeries": vm.p.truckSeries,
                    "goodsCode": vm.p.goodsCode,
                    "minPrice": vm.p.minPrice,
                    "maxPrice": vm.p.maxPrice,
                    "status": vm.p.status,
                    "carSourceName": vm.p.carSourceName,
                    "contactName": vm.p.contactName,
                    "contactMobile": vm.p.contactMobile
                },
                page: page
            }).trigger("reloadGrid");
        },
        confirmPayment: function () {//确认付款页面确认付款按钮
            var param = {
                paymentId: vm.financial_payment.id,
                type: 0
            };
            $.post(baseURL + "financial/payment/payment", jQuery.param(param), function (result) {
                if (result.code == 0) {
                    vm.reload();
                } else {
                    alert(result.msg);
                }
            }, "json");
        },
        paymentClose: function () {//付款失败页面取消按钮
            $(".showPage").css("display", "none");
            vm.paymentModel = true;
        },
        paymentSub: function () {//付款失败页面确认按钮
            var param = {
                paymentId: vm.financial_payment.id,
                type: 1,
                remark: $('#remark').val()
            };
            $.post(baseURL + "financial/payment/payment", jQuery.param(param), function (result) {
                if (result.code == 0) {
                    vm.reload();
                } else {
                    alert(result.msg);
                }
            }, "json");
            $(".showPage").css("display", "none");
            vm.paymentModel = true;
        },
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.p);
            excel(baseURL + "financial/payment/excel", param)
        },
        getFinancialPaymentById: function (id) {
            $.get(baseURL + "financial/payment/detail?payment_code=" + id, function (r) {
                if (r.code == 0) {
                    vm.financial_payment = r.result.financialPayment;
                    vm.good = r.result.good;
                    vm.car_dealer = r.result.carDealer;
                    vm.car_source = r.result.carSource;
                    vm.aut_goods = r.result.autGoods;
                    vm.paymentLog = r.result.paymentLog;
                    vm.bankInfo = r.result.bankInfo;
                } else {
                    alert(r.msg)
                }
            })
        }
    }
});
//给行内按钮绑定事件-详情
$("#jqGrid").on("click", ".info", function () {
    var id = $(this).parents('tr').attr("id");
    vm.getFinancialPaymentById(id);
    vm.title = "详情";
    vm.showList = false;
    $(":input").attr("readonly", "readonly");
});
//给行内按钮绑定事件-确认收款
$("#jqGrid").on("click", ".confirmPaymentShow", function () {
    var id = $(this).parents('tr').attr("id");
    vm.getFinancialPaymentById(id);
    vm.title = "确认付款";
    vm.showList = false;
    $(":input").attr("readonly", "readonly");
});
//给行内按钮绑定事件-收款失败
$("#jqGrid").on("click", ".paymentErrorShow", function () {
    var id = $(this).parents('tr').attr("id");
    vm.getFinancialPaymentById(id)
    vm.paymentModel = false;
    $(".showPage").css("display", "block");
});