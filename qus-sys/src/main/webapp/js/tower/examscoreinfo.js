$(function () {
    $("#jqGrid").Grid({
        url: '../tower/examscoreinfo/list',
        colModel: [
			{label: '被考核部门', name: 'examName', index: 'exam_name', width:300},
			{label: '得分', name: 'indexItemScore', index: 'index_item_score', width:300}
		]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		examScoreInfo: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			exam_station: '25'
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		exportInfo: function () {
			exportFile('#rrapp', '../tower/examscoreinfo/export',
				{
					'examStation': encodeURI(vm.q.exam_station)
				});
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.examScoreInfo = {};
		},
		update: function (event) {
            var examResultId = getSelectedRow("#jqGrid");
			if (examResultId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(examResultId)
		},
		saveOrUpdate: function (event) {
            var url = vm.examScoreInfo.examResultId == null ? "../tower/examscoreinfo/save" : "../tower/examscoreinfo/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.examScoreInfo),
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
            var examResultIds = getSelectedRows("#jqGrid");
			if (examResultIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/examscoreinfo/delete",
                    params: JSON.stringify(examResultIds),
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
		getInfo: function(examResultId){
            Ajax.request({
                url: "../tower/examscoreinfo/info/"+examResultId,
                async: true,
                successCallback: function (r) {
                    vm.examScoreInfo = r.examScoreInfo;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'examStation': vm.q.exam_station},
                page: 1
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
        },

	}
});