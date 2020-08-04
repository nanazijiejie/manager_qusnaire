$(function () {
    $("#jqGrid").Grid({
        url: '../ring/specparamdef/list',
        colModel: [
			{label: 'specId', name: 'specId', index: 'spec_id', key: true, hidden: true},
			{
				label: '创建时间', name: 'createTime', index: "create_time", width: 40, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '规格组名称', name: 'specName', index: 'spec_name', width: 30},
			{label: '参数名称', name: 'paramName', index: 'param_name', width: 30},
			{label: '图片', name: 'paramPicPath', index: 'param_pic_path', width: 80,
				formatter: function (value) {
					return transImg(value);
				}
			},
			{
				label: '状态', name: 'specStatus', index: 'spec_status', width: 30,
				formatter: function (value) {
					if(value=='0'){
						return "上架";
					}else if(value='1'){
						return "下架";
					}
				}
			}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		specParamDef: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			specName: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.specParamDef = {};
		},
		update: function (event) {
            var specId = getSelectedRow("#jqGrid");
			if (specId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(specId)
		},
		saveOrUpdate: function (event) {
            var url = vm.specParamDef.specId == null ? "../ring/specparamdef/save" : "../ring/specparamdef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.specParamDef),
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
            var specIds = getSelectedRows("#jqGrid");
			if (specIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../ring/specparamdef/delete",
                    params: JSON.stringify(specIds),
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
		getInfo: function(specId){
            Ajax.request({
                url: "../ring/specparamdef/info/"+specId,
                async: true,
                successCallback: function (r) {
                    vm.specParamDef = r.specParamDef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'specName': encodeURI(vm.q.specName)},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				specName: ''
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
		handleSuccessPicUrl: function (res, file) {
			vm.specParamDef.paramPicPath = file.response.url;
		},
		handleFormatError: function (file) {
			this.$Notice.warning({
				title: '文件格式不正确',
				desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
			});
		},
		handleMaxSize: function (file) {
			this.$Notice.warning({
				title: '超出文件大小限制',
				desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
			});
		},
		eyeImagePicUrl: function () {
			var url = vm.specParamDef.paramPicPath;
			eyeImage(url);
		},
		eyeImageListPicUrl: function () {
			var url = vm.specParamDef.paramPicPath;
			eyeImage(url);
		},
		eyeImage: function (e) {
			eyeImage($(e.target).attr('src'));
		}
	}
});