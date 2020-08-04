$(function () {
    $("#jqGrid").Grid({
        url: '../tower/examperdef/list',
        colModel: [
			{label: 'percentId', name: 'percentId', index: 'percent_id', key: true, hidden: true},
			{label: '考核职务', name: 'examStation', index: 'exam_station', width: 200},
			{label: '考核职务ID', name: 'examStationId', index: 'exam_station_id', width: 80, hidden: true},
			{label: '填写问卷人职务', name: 'qusStation', index: 'qus_station', width: 200},
			{label: '填写问卷人职务ID', name: 'qusStationId', index: 'qus_station_id', width: 80, hidden: true},
			{label: '考核结果占比（单位：%）', name: 'percent', index: 'percent', width: 200}
			/*{label: '省公司总经理占比', name: 'provManagerPer', index: 'prov_manager_per', width: 80},
			{label: '省公司副总经理占比', name: 'provViceManagerPer', index: 'prov_vice_manager_per', width: 80},
			{label: '全省中层正职占比', name: 'provDeptManagerPer', index: 'prov_dept_manager_per', width: 80},
			{label: '省分管副总占比', name: 'provDuptyManagerPer', index: 'prov_dupty_manager_per', width: 80},
			{label: '省其他副总占比', name: 'provOtherViceManagerPer', index: 'prov_other_vice_manager_per', width: 80},
			{label: '全省中层正职副职占比', name: 'provMiddleManagerPer', index: 'prov_middle_manager_per', width: 80},
			{label: '本部门正职占比', name: 'localDeptManagerPer', index: 'local_dept_manager_per', width: 80},
			{label: '本部门副职占比', name: 'localViceDeptManagerPer', index: 'local_vice_dept_manager_per', width: 80},
			{label: '本部门员工占比', name: 'localDeptStaffPer', index: 'local_dept_staff_per', width: 80},
			{label: '全省中层副职占比', name: 'provDeptViceManagerPer', index: 'prov_dept_vice_manager_per', width: 80},
			{label: '地市正职/地市总经理占比', name: 'cityManagerPer', index: 'city_manager_per', width: 80},
			{label: '地市中层/地市部门正职副职占比', name: 'cityDeptManagerPer', index: 'city_dept_manager_per', width: 80},
			{label: '地市员工占比', name: 'cityDeptStaffPer', index: 'city_dept_staff_per', width: 80},
			{label: '地市副职/地市副总经理占比', name: 'cityDeptViceManagerPer', index: 'city_dept_vice_manager_per', width: 80}*/]
    });
    vm.qryDictDef();
});
const isInteger = (rule, value, callback) => {
	const age= /^[0-9]*$/
	if (!age.test(value)) {
		callback(new Error('请输入数字'))
	}else{
		callback()
	}
};
var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		examPerDef: {},
		ruleValidate: {
			examStationId: [
				{required: true, message: '考核职务不能为空', trigger: 'blur'}
			],
			qusStationId: [
				{required: true, message: '填写问卷人职务不能为空', trigger: 'blur'}
			],
			percent: [
				{required: true, validator: isInteger, message: '请输入数字', trigger: 'blur'}
			]
		},
		q: {
			examStationId: ''
		},
		stationDefList:{},
		qusDefList:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.examPerDef = {};
		},
		update: function (event) {
            var percentId = getSelectedRow("#jqGrid");
			if (percentId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(percentId)
		},
		saveOrUpdate: function (event) {
			if(vm.examPerDef.percent>100){
				alert("考核占比不能超过100");
				return;
			}
            var url = vm.examPerDef.percentId == null ? "../tower/examperdef/save" : "../tower/examperdef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.examPerDef),
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
            var percentIds = getSelectedRows("#jqGrid");
			if (percentIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/examperdef/delete",
                    params: JSON.stringify(percentIds),
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
		getInfo: function(percentId){
            Ajax.request({
                url: "../tower/examperdef/info/"+percentId,
                async: true,
                successCallback: function (r) {
                    vm.examPerDef = r.examPerDef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'examStationId': vm.q.examStationId},
                page: 1
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				examStationId: ''
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
					var count1 = 0;
					for(var i =0;i<r.list.length;i++){
						if(r.list[i].typeCode=="EXAM_STATION"){
							if(r.list[i].itemValue!='14'){
								vm.stationDefList[count++] = r.list[i];
							}
						}
						if(r.list[i].typeCode=="EXEL_STATION"){
							vm.qusDefList[count1++] = r.list[i];
						}
					}
				}
			});

		},
	}
});