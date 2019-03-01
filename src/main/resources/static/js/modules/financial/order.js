var detail = '<a v-if="" class="info btn btn-info">查看</a>';
var confirmReceiptShow = '<a v-if="" class="confirmReceiptShow btn btn-info">确认收款</a>';
var receiptErrorShow = '<a v-if="" class="receiptErrorShow btn btn-info">收款失败</a>';
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'financial/order/list',
        datatype: "json",
        colModel: [
            {label: '订单编号', name: 'orderCode', width: 60, key: true},
            {label: '拍品编号/拍品包编号', name: 'goodsCode', width: 60},
            {label: '包编号', name: 'truckPackCode', width: 60},
            {
                label: '车型', name: 'truckModel', width: 45,
            },
            {label: '品牌车系', name: 'truckBrandSeries', width: 75},
            {label: '买家名称', name: 'carDealerName', width: 75},
            {label: '保留价(元)', name: 'retainPrice', width: 40},
            {label: '应收金额(元)', name: 'duePrice', width: 40},
            {label: '实收金额(元)', name: 'realPrice', width: 40},
            {label: '应收交易服务费(元)', name: 'tradePrice', width: 40},
            {label: '实收交易服务费(元)', name: 'realTradePrice', width: 40},
            {label: '交付服务费(元)', name: 'deliverPrice', width: 40},
            {
                label: '状态', name: 'status', width: 40, formatter: function (value, options, row) {
                    if (value == '0') {
                        return '待收款';
                    }
                    if (value == '1') {
                        return '收款完成';
                    }
                    if (value == '2') {
                        return '收款失败';
                    }
                }
            },
            {label: '操作', name: 'status', width: 80, formatter: operatButtons}
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
    //自定义按钮
    function operatButtons(value, options, row) {
        var result = detail;
        if (value == '0') {
            result = result + " " + confirmReceiptShow;
            result = result + " " + receiptErrorShow;
        }
        return result;
    }
    /*状态切换*/
    $('.carUl li').on("click", function () {
        $('.carUl li').removeClass("activeUl");
        $(this).addClass("activeUl");
        vm.o.status = $(this).attr("data-value");
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
        o: {
            packCode: null,
            truckModel: null,
            truckBrand: null,
            truckSeries: null,
            goodsCode: null,
            minPrice: null,
            maxPrice: null,
            status: null
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
        car_source: {},
        financial_order: {
            orderCode: null,
            goodsCode: null,
            carDealerName: null,
            realPrice: null
        },
        good: {},
        aut_goods: [
            {truckInfoId: null},
            {truckBrandSeries: null},
            {truckModel: null},
            {truckAddress: null},
            {carSourceContactName: null}
        ],
        bankInfo: [],
        models: [],
        brands: [],
        series: [],
        title: "新增",
        status: null,
        partnerId: null,
        partnerType: null,
        receiptError: true,
        breaks: true,
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
                    'packCode': vm.o.packCode,
                    'truckModel': vm.o.truckModel,
                    'truckBrand': vm.o.truckBrand,
                    'truckSeries': vm.o.truckSeries,
                    'goodsCode': vm.o.goodsCode,
                    'minPrice': vm.o.minPrice,
                    'maxPrice': vm.o.maxPrice,
                    'status': vm.o.status
                },
                page: page
            }).trigger("reloadGrid");
        },
        /*------------------------------------------------确认收款----------------------------------------------*/
        confirmReceiptShow: function (id) {//显示确认收款页面
            this.getFinancialOrderById(id);
            vm.title = "确认收款";
            vm.showList = false;
            $(":input").attr("readonly", "readonly");
        },
        confirmReceipt: function () {//确认收款页面确认收款按钮
            var param = {
                totalAmountReceived: $("#totalAmountReceived").text(),
                type: '0',
                orderCode: vm.financial_order.orderCode,
            };
            $.post(baseURL + "financial/order/receipt", jQuery.param(param), function (result) {
                if (result.code == 0) {
                    vm.reload();
                } else {
                    alert(result.msg);
                }
            }, "json");
        },
        /*----------------------------------------------收款失败-------------------------------------------------*/
        receiptErrorShow: function (id) {//弹出收款失败模态框
            this.getFinancialOrderById(id);
            $(".showPage").css("display", "block");
            vm.title = "收款失败";
            vm.showList = true;
            vm.receiptError = false;
        },
        receiptClose: function () {//收款失败模态框取消按钮
            vm.receiptError = true;
            $(".showPage").css("display", "none");
        },
        receiptSub: function () {//收款失败模态框确定按钮
            var remark = $("#remark").val();
            var param = {
                remark: remark,
                type: '1',
                orderCode: vm.financial_order.orderCode,
            };
            $.post(baseURL + "financial/order/receipt", jQuery.param(param), function (result) {
                if (result.code == 0) {
                    vm.reload();
                } else {
                    alert(result.msg);
                }
            }, "json");
            vm.receiptError = true;
            $(".showPage").css("display", "none");
            $("#remark").val("");
        },
        /*------------------------------------------------减免----------------------------------------------*/
        breaksShow: function () {
            $("#rebateAmount").attr("readonly", false);
            $(".showPage").css("display", "block");
            vm.breaks = false;
        },
        breaksClose: function () {
            vm.breaks = true;
            $(".showPage").css("display", "none");
            $("#rebateAmount").val("");
        },
        breaksSub: function () {
            $("#breaksPrice").text($("#rebateAmount").val());
            var breaksPrice = $("#rebateAmount").val();//减免金额
            $("#paidTradePrice").text(Number(vm.good.tradePrice) - Number(vm.good.tradePrice - vm.car_dealer.rebate * vm.good.tradePrice / 10) - Number(breaksPrice))
            var paidTradePrice = $("#paidTradePrice").text();//交易服务费实收金额
            $("#totalAmountReceived").text(vm.good.actualPrice + vm.good.deliverPrice + Number(paidTradePrice))
            vm.breaks = true;
            $(".showPage").css("display", "none");
            $("#rebateAmount").val("");
        },
        /*------------------------------------------------导出----------------------------------------------*/
        excel: function () {
            var ids = getSelectedRows();
            vm.showList = true;
            var param = jQuery.param(vm.o);
            excel(baseURL + "financial/order/excel", param, ids)
        },
        /*------------------------------------------------详情----------------------------------------------*/
        getFinancialOrderById: function (id) {
            $.get(baseURL + "financial/order/detail?order_code=" + id, function (r) {
                if (r.code == 0) {
                    vm.financial_order = r.result.financialOrder;
                    vm.car_source = r.result.carSource;
                    vm.car_dealer = r.result.carDealer;
                    vm.good = r.result.good;
                    vm.aut_goods = r.result.autGoods
                    vm.bankInfo = r.result.bankInfo != null ? r.result.bankInfo : null;
                } else {
                    alert(r.msg)
                }
            })
        }
    }
});

//动态计算总折扣金额
$("#rebateAmount").on("change", function () {
    var breaksAmout = $("#breaksAmout").text();
    var rebateAmount = $("#rebateAmount").val();
    if (breaksAmout != null && breaksAmout != '' && breaksAmout != undefined && rebateAmount != null && rebateAmount != '' && rebateAmount != undefined) {
        $("#totalRebateAmount").text(Number(breaksAmout) + Number(rebateAmount));
    }
});
//给行内按钮绑定事件-详情
$("#jqGrid").on("click", ".info", function () {
    var id = $(this).parents('tr').attr("id");
    vm.getFinancialOrderById(id);
    vm.showList = false;
    vm.title = "详情";
    $(":input").attr("readonly", "readonly");
});
//给行内按钮绑定事件-确认收款
$("#jqGrid").on("click", ".confirmReceiptShow", function () {
    var id = $(this).parents('tr').attr("id");
    vm.showList = false;
    vm.title = "确认收款";
    vm.confirmReceiptShow(id);
});
//给行内按钮绑定事件-收款失败
$("#jqGrid").on("click", ".receiptErrorShow", function () {
    var id = $(this).parents('tr').attr("id");
    vm.showList = false;
    vm.title = "收款失败";
    vm.receiptErrorShow(id);
});