$(function () {
    $("#jqGrid").Grid({
        url: '../tower/finalexamscoreinfo/list',
        colModel: [
			{label: 'examResultId', name: 'examResultId', index: 'exam_result_id', key: true, hidden: true},
			//{label: '填写问卷人职务', name: 'qusNaireStation', index: 'qus_naire_station', width: 80},
			{label: '填写问卷人职务', name: 'qusNaireStationName', index: 'qus_naire_station_name', width: 80},
			//{label: '考核职务', name: 'examStation', index: 'exam_station', width: 80},
			{label: '被考核人姓名', name: 'examName', index: 'exam_name', width: 80},
			{label: '被考核人归属单位', name: 'examCity', index: 'exam_city', width: 100},
			{label: '被考核人归属部门', name: 'examDept', index: 'exam_dept', width: 100},
			{label: '被考核人职务', name: 'examStationName', index: 'exam_station_name', width: 80},
			//{label: '考核员工ID', name: 'examStaffId', index: 'exam_staff_id', width: 80},
			//{label: '考核人归属部门ID', name: 'examDeptId', index: 'exam_dept_id', width: 80},
			//{label: '考核人归属地市ID', name: 'examCityId', index: 'exam_city_id', width: 80},
			{label: '胜任力考核得分', name: 'finalScore', index: 'final_score', width: 200}]
    });
	vm.getUser();
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		finalExamScoreInfo: {},
		stationDefList:{},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			examName: '',
			station_id:''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.finalExamScoreInfo = {};
		},
		getUser: function () {
			$.getJSON("../sys/user/info?_" + $.now(), function (r) {
				vm.user = r.user;
				if(vm.user.userId!='1'&&vm.user.cityId!='Z'){
					vm.qryDictDef(vm.user.cityId);
				}else{
					vm.qryDictDef();
				}
			});
		},
		qryDictDef:function(cityId){
			vm.stationDefList={};
			vm.cityDefList={};
			var url = "../tower/dictdef/queryAll";
			Ajax.request({
				url: url,
				params: {
					"codeType":"CITY"
				},
				type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					//vm.cityDefList = r.list;
					var count1 = 0;
					debugger;
					for(var i =0;i<r.list.length;i++){
						if(r.list[i].typeCode=="STATION"){
							if(cityId!=undefined){
								if(cityId!="Z"&&r.list[i].itemName.indexOf("地市")!=-1){
									vm.stationDefList[count1++] = r.list[i];
								}
								if(cityId=="Z"&&r.list[i].itemName.indexOf("省")!=-1){
									vm.stationDefList[count1++] = r.list[i];
								}
							}else{
								if(vm.user.cityId=='Z'||vm.user.userId=='1'){
									vm.stationDefList[count1++] = r.list[i];
								}else{
									if(r.list[i].itemName.indexOf("地市")!=-1){
										vm.stationDefList[count1++] = r.list[i];
									}
								}
							}
						}
					}
				}
			});

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
            var url = vm.finalExamScoreInfo.examResultId == null ? "../tower/finalexamscoreinfo/save" : "../tower/finalexamscoreinfo/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.finalExamScoreInfo),
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
				    url: "../tower/finalexamscoreinfo/delete",
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
                url: "../tower/finalexamscoreinfo/info/"+examResultId,
                async: true,
                successCallback: function (r) {
                    vm.finalExamScoreInfo = r.finalExamScoreInfo;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'examName': encodeURI(vm.q.examName),
					'examStation': vm.q.station_id},
                page: 1
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				examName: '',
				station_id:''
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
		/**
		 * 判断是否所有人都提交了问卷
		 */
		checkAllSubmit:function(){
			Ajax.request({
				url: "../tower/examscoreinfo/checkAllSubmit",
				type: "POST",
				contentType: "application/json",
				timeout: 5,
				successCallback: function () {
					vm.calcFinal();
				}
			});
		},
		calcFinal:function(){
			alert("考核计算完成后将以邮件的形式发送到您的邮箱，收到通知后，请在本页面导出结果！");
			$.ajax({
				url: '../tower/examscoreinfo/calcFinal',                      //url地址
				type: 'POST',                 //上传方式
				dataType: 'JSON',
				timeout: 500
			});

			/*Ajax.request({
				url: "../tower/examscoreinfo/calcFinal",
				type: "POST",
				contentType: "application/json",
				timeout: 5,
				successCallback: function () {
                    alert('计算成功，请导出结果！', function (index) {
                        vm.reload();
                    });
                }
			});*/

		}
	}
});