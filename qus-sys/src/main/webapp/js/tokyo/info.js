$(function () {
    $("#jqGrid").Grid({
        url: '../info/list',
        colModel: [
			{label: 'infoId', name: 'infoId', index: 'info_id', key: true, hidden: true},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
			{label: '图片路径1', name: 'pic1Path', index: 'pic1_path', width: 80},
			{label: '图片路径2', name: 'pic2Path', index: 'pic2_path', width: 80},
			{label: '图片路径3', name: 'pic3Path', index: 'pic3_path', width: 80},
			{label: '二维码图片路径', name: 'weixinCodePath', index: 'weixin_code_path', width: 80},
			{label: '审核时间', name: 'examTime', index: 'exam_time', width: 80},
			{label: '审核人名称', name: 'examOperator', index: 'exam_operator', width: 80},
			{label: '类别ID', name: 'typeId', index: 'type_id', width: 80},
			{label: '类别名称', name: 'typeName', index: 'type_name', width: 80},
			{label: '标题', name: 'title', index: 'title', width: 80},
			{label: '信息状态：0待审核，1审核通过，2审核不通过', name: 'infoStatus', index: 'info_status', width: 80},
			{label: '简短描述', name: 'shortDesc', index: 'short_desc', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		info: {},
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
			vm.info = {};
		},
		update: function (event) {
            let infoId = getSelectedRow("#jqGrid");
			if (infoId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(infoId)
		},
		saveOrUpdate: function (event) {
            let url = vm.info.infoId == null ? "../info/save" : "../info/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.info),
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
            let infoIds = getSelectedRows("#jqGrid");
			if (infoIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../info/delete",
                    params: JSON.stringify(infoIds),
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
		getInfo: function(infoId){
            Ajax.request({
                url: "../info/info/"+infoId,
                async: true,
                successCallback: function (r) {
                    vm.info = r.info;
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