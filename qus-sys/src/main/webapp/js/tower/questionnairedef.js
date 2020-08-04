$(function () {
    $("#jqGrid").Grid({
        url: '../tower/questionnairedef/list',
        colModel: [
			{label: 'qusNaireId', name: 'qusNaireId', index: 'qus_naire_id', key: true, hidden: true},
			{label: '问卷填写人职务', name: 'qusNaireStation', index: 'qus_naire_station', width: 70},
			{label: '问卷填写人职务ID', name: 'qusNaireStationId', index: 'qus_naire_station_id', hidden: true},
			{label: '问卷名称', name: 'qusNaireName', index: 'qus_naire_name', width: 250},
			{label: '问卷考核抬头说明', name: 'headDesc', index: 'head_desc', width: 80},
			{label: '问卷考核末尾说明', name: 'bottomDesc', index: 'bottom_desc', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '创建人', name: 'createOperator', index: 'create_operator', width: 50},
			{label: '修改时间', name: 'updateTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '修改人', name: 'updateOperator', index: 'create_operator', width: 50}]
    });
    vm.qryDictDef();
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		questionnaireDef: {},
		ruleValidate: {
			qusNaireStationId: [
				{required: true, message: '请选择问卷填写人职务', trigger: 'blur'}
			],
			qusNaireName: [
				{required: true, message: '问卷名称不能为空', trigger: 'blur'}
			],
			headDesc: [
				{required: true, message: '问卷考核抬头说明', trigger: 'blur'}
			],
			bottomDesc: [
				{required: true, message: '问卷考核末尾说明', trigger: 'blur'}
			],
		},
		q: {
		    name: ''
		},
		stationDefList:{},
		examItemList:{},
		examItemArr:[],
		qusItemRels:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.questionnaireDef = {};
			vm.qryExamItem();
		},
		update: function (event) {
            var qusNaireId = getSelectedRow("#jqGrid");
			if (qusNaireId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(qusNaireId);

		},
		saveOrUpdate: function (event) {
            var url = vm.questionnaireDef.qusNaireId == null ? "../tower/questionnairedef/save" : "../tower/questionnairedef/update";
            if(vm.examItemArr.length==0){
				alert("考核项不能为空，请选择后再提交！");
				return;
			}
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.questionnaireDef),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
					vm.saveOrUpdateRels(r.qusNaireId);
                }
			});
		},
		saveOrUpdateRels:function(qusNaireId){
			var examItemListStr = "";
			for(var i=0;i<vm.examItemArr.length;i++){
				examItemListStr += vm.examItemArr[i]+",";
			}
			if(examItemListStr!=""){
				examItemListStr = examItemListStr.substring(0,examItemListStr.length-1);
			}else{
				alert("考核项不能为空，请选择后再提交！");
				return;
			}
			$.ajax({
				//与服务器交互
				url:"../tower/qusitemrel/saveBatch",
				dataType: 'json',
				data: {"qusNaireId":qusNaireId,
				"examItemList":examItemListStr},//
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
		},
		del: function (event) {
            var qusNaireIds = getSelectedRows("#jqGrid");
			if (qusNaireIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/questionnairedef/delete",
                    params: JSON.stringify(qusNaireIds),
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
		getInfo: function(qusNaireId){
			debugger;
            Ajax.request({
                url: "../tower/questionnairedef/info/"+qusNaireId,
                async: true,
                successCallback: function (r) {
                    vm.questionnaireDef = r.questionnaireDef;
					vm.qryExamItem(qusNaireId);
                }
            });
		},
		//查询问卷的考核项有哪些
		getRels: function(qusNaireId){
			Ajax.request({
				url: "../tower/qusitemrel/info/"+qusNaireId,
				async: true,
				successCallback: function (r) {
					vm.qusItemRels = r.list;
					vm.examItemArr=[];
					var index = 0;
					for(var i=0;i<vm.examItemList.length;i++){
						for(var j=0;j<vm.qusItemRels.length;j++){
							if(vm.examItemList[i].examItemId==vm.qusItemRels[j].examItemId){
								vm.examItemArr[index++] = vm.examItemList[i].examItemId;
								vm.examItemList[i].isChecked = true;
								break;
							}
						}
					}
				}
			});
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
						if(r.list[i].typeCode=="QUS_STATION"){
							vm.stationDefList[count++] = r.list[i];
						}
					}
				}
			});

		},
		qryExamItem:function(qusNaireId){
			var url = "../tower/examitemdef/queryAll";
			Ajax.request({
				url: url,
				type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					var count = 0;
					vm.examItemList = r.list;
					if(qusNaireId!=null&&qusNaireId!=undefined){
						vm.getRels(qusNaireId);
					}
				}
			});
		}
	}
});