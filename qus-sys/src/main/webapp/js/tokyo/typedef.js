$(function () {
    $("#jqGrid").Grid({
        url: '../typedef/list',
        colModel: [
			{label: 'typeId', name: 'typeId', index: 'type_id', key: true, hidden: true},
			{label: '类型名称', name: 'typeName', index: 'type_name', width: 80},
			{label: '在小程序中的展示顺序，从左到右。', name: 'showSeq', index: 'show_seq', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
			{label: '创建人名字', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '0:上架；1:下架', name: 'typeStatus', index: 'type_status', width: 80},
			{label: '图片路径', name: 'picPath', index: 'pic_path', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		typeDef: {},
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
			vm.typeDef = {};
		},
		update: function (event) {
            let typeId = getSelectedRow("#jqGrid");
			if (typeId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(typeId)
		},
		saveOrUpdate: function (event) {
            let url = vm.typeDef.typeId == null ? "../typedef/save" : "../typedef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.typeDef),
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
            let typeIds = getSelectedRows("#jqGrid");
			if (typeIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../typedef/delete",
                    params: JSON.stringify(typeIds),
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
		getInfo: function(typeId){
            Ajax.request({
                url: "../typedef/info/"+typeId,
                async: true,
                successCallback: function (r) {
                    vm.typeDef = r.typeDef;
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