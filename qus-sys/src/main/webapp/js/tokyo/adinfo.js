$(function () {
    $("#jqGrid").Grid({
        url: '../adinfo/list',
        colModel: [
			{label: 'addId', name: 'addId', index: 'add_id', key: true, hidden: true},
			{label: '名称', name: 'adName', index: 'ad_name', width: 80},
			{label: '图片路径', name: 'adPicPath', index: 'ad_pic_path', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
			{label: '创建人名字', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '生效时间', name: 'startDate', index: 'start_date', width: 80},
			{label: '失效时间', name: 'endDate', index: 'end_date', width: 80},
			{label: '0:上架；1:下架', name: 'adStatus', index: 'ad_status', width: 80},
			{label: '展示顺序，从左到右', name: 'adSeq', index: 'ad_seq', width: 80},
			{label: '类型：0屏显，1轮播', name: 'adType', index: 'ad_type', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		adInfo: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.adInfo = {};
		},
		update: function (event) {
            let addId = getSelectedRow("#jqGrid");
			if (addId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(addId)
		},
		saveOrUpdate: function (event) {
            let url = vm.adInfo.addId == null ? "../adinfo/save" : "../adinfo/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.adInfo),
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
            let addIds = getSelectedRows("#jqGrid");
			if (addIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../adinfo/delete",
                    params: JSON.stringify(addIds),
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
		getInfo: function(addId){
            Ajax.request({
                url: "../adinfo/info/"+addId,
                async: true,
                successCallback: function (r) {
                    vm.adInfo = r.adInfo;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                name: ''
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
        }
	}
});