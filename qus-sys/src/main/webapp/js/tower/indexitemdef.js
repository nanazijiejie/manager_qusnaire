$(function () {
    $("#jqGrid").Grid({
        url: '../tower/indexitemdef/list',
        colModel: [
			{label: 'indexItemId', name: 'indexItemId', index: 'index_item_id', key: true, hidden: true},
			{label: '指标项名称', name: 'indexItemName', index: 'index_item_name', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '创建人', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '修改时间', name: 'updateTime', index: 'update_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '修改人', name: 'updateOperator', index: 'update_operator', width: 80},
			{label: '顺序', name: 'itemSeq', index: 'item_seq', width: 80}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		indexItemDef: {},
		ruleValidate: {
			indexItemName: [
				{required: true, message: '指标项名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			indexItemName: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.indexItemDef = {};
		},
		update: function (event) {
            var indexItemId = getSelectedRow("#jqGrid");
			if (indexItemId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(indexItemId)
		},
		saveOrUpdate: function (event) {
            var url = vm.indexItemDef.indexItemId == null ? "../tower/indexitemdef/save" : "../tower/indexitemdef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.indexItemDef),
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
            var indexItemIds = getSelectedRows("#jqGrid");
			if (indexItemIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/indexitemdef/delete",
                    params: JSON.stringify(indexItemIds),
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
		getInfo: function(indexItemId){
            Ajax.request({
                url: "../tower/indexitemdef/info/"+indexItemId,
                async: true,
                successCallback: function (r) {
                	debugger;
                    vm.indexItemDef = r.indexItemDef;
                    console.log(vm.indexItemDef);
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'indexItemName': encodeURI(vm.q.indexItemName)},
                page: 1
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				indexItemName: ''
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