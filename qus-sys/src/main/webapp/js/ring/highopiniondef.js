$(function () {
    $("#jqGrid").Grid({
        url: '../ring/highopiniondef/list',
        colModel: [
			{label: 'opinionId', name: 'opinionId', index: 'opinion_id', key: true, hidden: true},
			{
				label: '创建时间', name: 'createTime', index: "create_time", width: 30, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '虚拟用户名', name: 'userName', index: 'user_name', width: 20},
			{label: '虚拟好评内容', name: 'highOpinion', index: 'high_opinion', width: 100}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		highOpinionDef: {},
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
			vm.highOpinionDef = {};
		},
		update: function (event) {
            var opinionId = getSelectedRow("#jqGrid");
			if (opinionId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(opinionId)
		},
		saveOrUpdate: function (event) {
            var url = vm.highOpinionDef.opinionId == null ? "../ring/highopiniondef/save" : "../ring/highopiniondef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.highOpinionDef),
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
            var opinionIds = getSelectedRows("#jqGrid");
			if (opinionIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../ring/highopiniondef/delete",
                    params: JSON.stringify(opinionIds),
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
		getInfo: function(opinionId){
            Ajax.request({
                url: "../ring/highopiniondef/info/"+opinionId,
                async: true,
                successCallback: function (r) {
                    vm.highOpinionDef = r.highOpinionDef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {},
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