$(function () {
    $("#jqGrid").Grid({
        url: '../tower/dictdef/list',
        colModel: [
			{label: 'code', name: 'code', index: 'code', key: true, hidden: true},
			{label: '字典类型编码', name: 'typeCode', index: 'type_code', width: 80},
			{label: '字典名称（展示用）', name: 'itemName', index: 'item_name', width: 80},
			{label: '字典值（使用）', name: 'itemValue', index: 'item_value', width: 80},
			{label: '类型描述', name: 'typeDesc', index: 'type_desc', width: 80}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		dictDef: {},
		ruleValidate: {
			typeCode: [
				{required: true, message: '字典类型编码不能为空', trigger: 'blur'}
			],
			itemName: [
				{required: true, message: '字典名称（展示用）不能为空', trigger: 'blur'}
			],
			itemValue: [
				{required: true, message: '字典值（使用）不能为空', trigger: 'blur'}
			],
			typeDesc: [
				{required: true, message: '类型描述不能为空', trigger: 'blur'}
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
			vm.dictDef = {};
		},
		update: function (event) {
            var code = getSelectedRow("#jqGrid");
			if (code == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(code)
		},
		saveOrUpdate: function (event) {
            var url = vm.dictDef.code == null ? "../tower/dictdef/save" : "../tower/dictdef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.dictDef),
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
            var codes = getSelectedRows("#jqGrid");
			if (codes == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/dictdef/delete",
                    params: JSON.stringify(codes),
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
		getInfo: function(code){
            Ajax.request({
                url: "../tower/dictdef/info/"+code,
                async: true,
                successCallback: function (r) {
                    vm.dictDef = r.dictdef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'typeCode': vm.q.type_code},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				type_code: ''
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