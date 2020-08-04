$(function () {
    $("#jqGrid").Grid({
        url: '../ring/orderinfo/list',
        colModel: [
            {label: '订单号', name: 'orderId', index: 'order_id', key: true, hidden: false, width: 120},
            {
                label: '状态', name: 'orderStatus', index: 'spec_status', width: 120,
                formatter: function (value) {
                    if (value == '0') {
                        return "预下单待支付";
                    } else if (value = '1') {
                        return "支付成功";
                    } else if (value = '2') {
                        return "支付失败";
                    }
                }
            },
            {
                label: '支付金额',
                name: 'totalPayAmount',
                index: 'total_pay_amount',
                width: 80,
                formatter: function (value) {
                    return value / 100 + "元";
                }
            },
            {
                label: '下单时间', name: 'createTime', index: "create_time", width: 80, formatter: function (value) {
                    return transDate(value);
                }
            },
            {
                label: '支付时间', name: 'payTime', index: "create_time", width: 80, formatter: function (value) {
                    return transDate(value);
                }
            },
            {label: '商品名称', name: 'goodsName', index: 'goods_name', width: 80},
            {label: '用户微信昵称', name: 'nickName', index: 'nick_name', width: 110},
            //{label: '微信用户ID', name: 'openId', index: 'open_id', width: 80},
            {label: '收货人', name: 'receiver', index: 'receiver', width: 80},
            {label: '联系电话', name: 'contactPhone', index: 'contact_phone', width: 120},
            {label: '收货地址', name: 'receiveAddr', index: 'receive_addr', width: 200},
            {label: '商品规格', name: 'specParam', index: 'spec_param', width: 120},
            {label: '数量', name: 'totalCount', index: 'total_count', width: 50},
            {label: '物流单号', name: 'expressId', index: 'express_id', width: 160}]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        orderInfo: {},
        ruleValidate: {
            receiver: [
                {required: true, message: '收货人不能为空', trigger: 'blur'}
            ],
            contactPhone: [
                {required: true, message: '联系电话不能为空', trigger: 'blur'}
            ],
            receiveAddr: [
                {required: true, message: '收货地址不能为空', trigger: 'blur'}
            ],
            contactPhone: [
                {required: true, message: '联系电话不能为空', trigger: 'blur'}
            ]
        },

        q: {
            orderStatus: '',
            orderId: '',
            contactPhone: '',
            expressId: '',
            createTimeStar: '',
            createTimeEnd: ''

        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.orderInfo = {};
        },
        update: function (event) {
            var orderId = getSelectedRow("#jqGrid");
            if (orderId == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(orderId)
        },
        saveOrUpdate: function (event) {
            var url = vm.orderInfo.orderId == null ? "../ring/orderinfo/save" : "../ring/orderinfo/update";
            vm.orderInfo.createTime = null;
            vm.orderInfo.payTime = null;
            Ajax.request({
                url: url,
                params: JSON.stringify(vm.orderInfo),
                type: "POST",
                contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            var orderIds = getSelectedRows("#jqGrid");
            if (orderIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                Ajax.request({
                    url: "../ring/orderinfo/delete",
                    params: JSON.stringify(orderIds),
                    type: "POST",
                    contentType: "application/json",
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (orderId) {
            Ajax.request({
                url: "../ring/orderinfo/info/" + orderId,
                async: true,
                successCallback: function (r) {
                    vm.orderInfo = r.orderInfo;
                    vm.orderInfo.createTime = vm.dateForm(vm.orderInfo.createTime);
                    vm.orderInfo.payTime = vm.dateForm(vm.orderInfo.payTime);
                    vm.orderInfo.totalPayAmount = vm.orderInfo.totalPayAmount/100;

                }
            });
        },
        dateForm: function (timeInt) {
            var unixTimestamp = new Date(timeInt );
            return unixTimestamp.getFullYear() + "/" + (unixTimestamp.getMonth() + 1) + "/" + unixTimestamp.getDate() + "   " + unixTimestamp.getHours() + ":" + unixTimestamp.getMinutes() + ":" + unixTimestamp.getSeconds();
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'orderStatus': vm.q.orderStatus,
                    'orderId': vm.q.orderId,
                    'contactPhone': vm.q.contactPhone,
                    'expressId': vm.q.expressId,
                    'createTimeStar': vm.dateToString(vm.q.createTimeStar),
                    'createTimeEnd': vm.dateToString(vm.q.createTimeEnd)
                },
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        dateToString: function (date) {
            if (date == null || date == "") {
                return null;
            }
            var year = date.getFullYear();
            var month = (date.getMonth() + 1).toString();
            var day = (date.getDate()).toString();
            if (month.length == 1) {
                month = "0" + month;
            }
            if (day.length == 1) {
                day = "0" + day;
            }
            var dateTime = year + "" + month + "" + day;
            return dateTime;
        },
        reloadSearch: function () {
            vm.q = {
                orderStatus: '',
                orderId: '',
                contactPhone: '',
                expressId: '',
                createTimeStar: '',
                createTimeEnd: '',
            }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        exportOrderInfo: function () {
            exportFile('#rrapp', '../ring/orderinfo/export',
                {
                    'orderStatus': vm.q.orderStatus,
                    'orderId': vm.q.orderId,
                    'contactPhone': vm.q.contactPhone,
                    'expressId': vm.q.expressId,
                    'createTimeStar': vm.dateToString(vm.q.createTimeStar),
                    'createTimeEnd': vm.dateToString(vm.q.createTimeEnd)
                });
        },

    }
});