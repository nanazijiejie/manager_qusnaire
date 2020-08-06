$(function () {
    $("#jqGrid").Grid({
        url: '../tower/examitemdef/list',
        colModel: [
			{label: 'examItemId', name: 'examItemId', index: 'exam_item_id', key: true, hidden: true},
			{label: '考核项名称', name: 'examItemName', index: 'exam_item_name', width: 80},
			{label: '考核对象职务', name: 'examStation', index: 'exam_station', width: 80},
			{label: '考核对象职务ID', name: 'examStationId', index: 'exam_station_id', width: 80, hidden: true},
			{label: '得A人数限制', name: 'excellentCount', index: 'excellent_count', width: 80},
			{label: '得B人数限制', name: 'goodCount', index: 'good_count', width: 80},
			{label: '得C人数限制', name: 'normalCount', index: 'good_count', width: 80},
			{label: '考核项描述', name: 'examItemDesc', index: 'exam_item_desc', width: 80},
			{label: '显示顺序', name: 'examSeq', index: 'exam_seq', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '创建人', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '修改时间', name: 'updateTime', index: 'update_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '修改人', name: 'updateOperator', index: 'update_operator', width: 80}
			]
    });
    vm.qryDictDef();
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		examItemDef: {},
		ruleValidate: {
			/*examStationId: [
				{required: true, message: '请选择考核对象职务', trigger: 'blur'}
			],*/
			/*excellentCount: [
				{required: true, message: '优秀人数限制不能为空，不限制请填写0', trigger: 'blur'}
			],
			goodCount: [
				{required: true, message: '优良人数限制不能为空，不限制请填写0', trigger: 'blur'}
			],*/
			examItemName: [
				{required: true, message: '考核项名称不能为空', trigger: 'blur'}
			],
			/*examItemDesc: [
				{required: true, message: '考核项描述不能为空', trigger: 'blur'}
			],*/

		},
		q: {
		    name: ''
		},
		stationDefList:{},
		indexItemList:{},
		percentAll:''
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.examItemDef = {};
			vm.percentAll='';
			vm.qryIndexItem();
		},
		update: function (event) {
            var examItemId = getSelectedRow("#jqGrid");
			if (examItemId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";
			vm.examItemDef = {};
			vm.percentAll='';
            vm.getInfo(examItemId)
		},
		saveOrUpdate: function (event) {
			debugger;
			console.log(vm.indexItemList);
            var url = vm.examItemDef.examItemId == null ? "../tower/examitemdef/save" : "../tower/examitemdef/update";
            debugger;
            if(vm.examItemDef.excellentCount==null||vm.examItemDef.excellentCount==""){
				vm.examItemDef.excellentCount = 0;
			}
			if(vm.examItemDef.goodCount==null||vm.examItemDef.goodCount==""){
				vm.examItemDef.goodCount = 0;
			}
			if(vm.examItemDef.normalCount==null||vm.examItemDef.normalCount==""){
				vm.examItemDef.normalCount = 0;
			}

            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.examItemDef),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
					vm.saveOrUpdateRels(r.examItemId);
                }
			});

		},
		calPercentAll:function(){
			var allPercent = 0;
			for(var i=0;i<vm.indexItemList.length;i++){
				if(vm.indexItemList[i].percent!='0'&&vm.indexItemList[i].percent!=''){
					allPercent += new Number(vm.indexItemList[i].percent);
				}
			}
			vm.percentAll = allPercent;
		},
		setStationName:function(obj){
			debugger;
			var arr = Object.keys(vm.stationDefList);
			var len = arr.length;
			var stationId = vm.examItemDef.examStationId;
			for(var i=0;i<len;i++){
				if(vm.stationDefList[i].itemValue==stationId&&
					(vm.examItemDef.examItemName==null||
						vm.examItemDef.examItemName==undefined||
						vm.examItemDef.examItemName=='')){
					vm.examItemDef.examItemName = vm.stationDefList[i].itemName;
					break;
				}
			}
		},
		saveOrUpdateRels:function(examItemId){
			var url = vm.examItemDef.examItemId == null ? "../tower/examindexrel/save" : "../tower/examindexrel/update";
			debugger;
			for(var i=0;i<vm.indexItemList.length;i++){
				vm.indexItemList[i].examItemId = examItemId;
				if(vm.indexItemList[i].percent!='0'&&vm.indexItemList[i].percent!=''){
					vm.indexItemList[i].isChecked=true;
				}else{
					vm.indexItemList[i].isChecked=false;
					vm.indexItemList[i].percent=0;
				}
			}
			if(vm.percentAll!=100){
				alert("指标项占比总和不等于100，请重新输入");
				return;
			}
			$.ajax({
				//与服务器交互
				url:url,
				dataType: 'json',
				data: {"indexItemListStr":JSON.stringify(vm.indexItemList)},//
				complete: function (XMLHttpRequest, textStatus) {

				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					alert("发生错误："+errorThrown);
				},
				success: function(res){
					alert('操作成功', function (index) {
						vm.reload();
					});
				}
			});
			/*Ajax.request({
				url: url,
				data:{
					"indexItemList":JSON.stringify(vm.indexItemList)
				},
				type: "POST",
				dataType:"JSON",
				successCallback: function (r) {
					alert("操作成功");
				}
			});*/
		},
		del: function (event) {
            var examItemIds = getSelectedRows("#jqGrid");
			if (examItemIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/examitemdef/delete",
                    params: JSON.stringify(examItemIds),
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
		getInfo: function(examItemId){
            Ajax.request({
                url: "../tower/examitemdef/info/"+examItemId,
                async: true,
                successCallback: function (r) {
                    vm.examItemDef = r.examItemDef;
                }
            });
            vm.qryIndexItem(examItemId);
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
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
		qryDictDef:function(){
			var url = "../tower/dictdef/queryAll";
			Ajax.request({
				url: url,
				type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					//vm.cityDefList = r.list;
					var count = 0;
					for(var i =0;i<r.list.length;i++){
						if(r.list[i].typeCode=="EXAM_STATION"){
							vm.stationDefList[count++] = r.list[i];
						}
					}
				}
			});

		},
		qryIndexItem:function(examItemId){
			var url = "../tower/indexitemdef/indexList";
			Ajax.request({
				url: url,
				params:{
					"examItemId":examItemId
				},
				contentType: "application/json",
				successCallback: function (r) {
					vm.indexItemList = r.list;
					vm.calPercentAll();
				}
			});
		}
	}
});