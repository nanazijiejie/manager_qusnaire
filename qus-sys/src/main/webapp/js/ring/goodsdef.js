
$(function () {
    $("#jqGrid").Grid({
        url: '../ring/goodsdef/list',
        colModel: [
			{label: 'goodsId', name: 'goodsId', index: 'goods_id', key: true, hidden: true},
			{label: '商品名称', name: 'goodsName', index: 'goods_name', width: 80},
			{label: '商品主图', name: 'goodsPicPath', index: 'goods_pic_path', width: 100,
				formatter: function (value) {
					return transImg(value);
				}
			},
			{label: '商品描述', name: 'goodsDesc', index: 'goods_desc', width: 100},
			{label: '创建人', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 100, formatter: function (value) {
					return transDate(value);
			}},
			{label: '生效时间', name: 'startDate', index: 'start_date', width: 100, formatter: function (value) {
					return transDate(value);
			}},
			{label: '失效时间', name: 'endDate', index: 'end_date', width: 100, formatter: function (value) {
					return transDate(value);
			}},
			{label: '商品状态', name: 'goodsStatus', index: 'goods_status', width: 80},
			//{label: '商品详情图', name: 'goodsPicDtlPath', index: 'goods_pic_dtl_path', width: 80},
			{label: '商品价格', name: 'goodsPrice', index: 'goods_price', width: 80, formatter: function (value) {
					return value/100+"元";
			}},
			{label: '商品原价', name: 'preGoodsPrice', index: 'pre_goods_price', width: 80, formatter: function (value) {
					return value/100+"元";
			}},
			{label: '界面特别说明', name: 'specialDesc', index: 'special_desc', width: 100},
			{label: '联系客服二维码图片', name: 'contactCodePath', index: 'contact_code_path', width: 120,
				formatter: function (value) {
					return transImg(value);
				}
			},
			{label: '商家联系电话', name: 'contactPhone', index: 'contact_phone', width: 100}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		goodsDef: {},
		uploadList: [],
		imgName: '',
		visible: false,
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    goodsName: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.goodsDef = {};
		},
		update: function (event) {
            var goodsId = getSelectedRow("#jqGrid");
			if (goodsId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(goodsId)
		},
		saveOrUpdate: function (event) {
            var url = vm.goodsDef.goodsId == null ? "../ring/goodsdef/save" : "../ring/goodsdef/update";
            var uploadPathStr="";
            for(var i=0;i<vm.uploadList.length;i++){
				uploadPathStr += vm.uploadList[i].imgUrl+",";
			}
            if(uploadPathStr!=""){
				uploadPathStr = uploadPathStr.substring(0,uploadPathStr.length-1);
			}
			vm.goodsDef.goodsPicDtlPath = uploadPathStr;
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goodsDef),
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
            var goodsIds = getSelectedRows("#jqGrid");
			if (goodsIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../ring/goodsdef/delete",
                    params: JSON.stringify(goodsIds),
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
		getInfo: function(goodsId){
            Ajax.request({
                url: "../ring/goodsdef/info/"+goodsId,
                async: true,
                successCallback: function (r) {
                    vm.goodsDef = r.goodsDef;
					vm.uploadList = r.uploadList;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'goodsName': vm.q.goodsName},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                goodsName: ''
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
			vm.goodsDef.goodsPicPath = file.response.url;
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
			var url = vm.goodsDef.goodsPicPath;
			eyeImage(url);
		},
		eyeImage: function (e) {
			eyeImage($(e.target).attr('src'));
		},
        //联系客服二维码图片
		handleSuccessPicUrl2: function (res, file) {
			vm.goodsDef.contactCodePath = file.response.url;
		},
		handleFormatError2: function (file) {
			this.$Notice.warning({
				title: '文件格式不正确',
				desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
			});
		},
		handleMaxSize2: function (file) {
			this.$Notice.warning({
				title: '超出文件大小限制',
				desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
			});
		},
		eyeImagePicUrl2: function () {
			var url = vm.goodsDef.contactCodePath;
			eyeImage(url);
		},
		handleView:function(name) {
			this.imgName = name;
			this.visible = true;
		},
		handleRemove:function(file) {
			// 从 upload 实例删除数据
			var fileList = this.uploadList;
			this.uploadList.splice(fileList.indexOf(file), 1);
		},
		handleSuccess:function(res, file) {
			// 因为上传过程为实例，这里模拟添加 url
			file.imgUrl = res.url;
			file.name = res.url;
			vm.uploadList.add(file);
		},
		handleBeforeUpload:function() {
			var check = this.uploadList.length < 10;
			if (!check) {
				this.$Notice.warning({
					title: '最多只能上传 10 张图片。'
				});
			}
			return check;
		}
	}
});