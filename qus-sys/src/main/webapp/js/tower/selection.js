$(function () {
    $("#jqGrid").Grid({
        url: '../tower/selectiondef/list',
        colModel: [
			{label: 'id', name: 'selDefId', index: 'sel_def_id', key: true, hidden: true},
			{label: '选举表格名称', name: 'selName', index: 'sel_name', width: 80},
			{label: '选举的职务名称', name: 'selStation', index: 'sel_station', width: 80},
			{label: '需选举的数量', name: 'selCount', index: 'sel_count', width: 80},
			{label: '归属单位/地市', name: 'selCity', index: 'sel_city', width: 80}]
    });
	vm.qrySelCity();
	vm.qrySelStation();
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
		selectionDef: {},
		ruleValidate: {
			selStationId: [
				{required: true, message: '选举的职务不能为空', trigger: 'blur'}
			],
			selCount: [
				{required: true, message: '请输入数字', validator: isInteger, trigger: 'blur'}
			],
			selCityId: [
				{required: true, message: '归属单位/地市不能为空', trigger: 'blur'}
			],
			selName: [
				{required: true, message: '选举表格名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
		selCityList:{},
		selStationList:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.selectionDef = {};
		},
		update: function (event) {
            var code = getSelectedRow("#jqGrid");
			if (code == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(code)
		},
		saveOrUpdate: function (event) {
            var url = vm.selectionDef.selDefId == null ? "../tower/selectiondef/save" : "../tower/selectiondef/update";
			vm.selectionDef.selCity = vm.getSelCity(vm.selectionDef.selCityId);
			vm.selectionDef.selStation = vm.getSelStation(vm.selectionDef.selStationId);
			var params = JSON.stringify(vm.selectionDef);
            Ajax.request({
			    url: url,
                params: params,
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
            var codes = getSelectedRows("#jqGrid");
			if (codes == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/selectiondef/delete",
                    params: JSON.stringify(codes),
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
		getInfo: function(code){
            Ajax.request({
                url: "../tower/selectiondef/info/"+code,
                async: true,
                successCallback: function (r) {
                    vm.selectionDef = r.selectionDef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'selCityId': vm.q.sel_city_id},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				sel_city_id: ''
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
		qrySelCity:function(){
			vm.selCityList={};
			//var url = "../tower/selectiondef/querySelCity";
			var url = "../tower/dictdef/list";
			Ajax.request({
				url: url,
				params: {'typeCode':'CITY'},
				// type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					vm.selCityList = r.page.list;
				}
			});
		},
		qrySelStation:function(){
			vm.selStationList={};
			var url = "../tower/dictdef/list";
			Ajax.request({
				url: url,
				params: {'typeCode':'SEL_STATION'},
				// type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					vm.selStationList = r.page.list;
				}
			});
		},
		getSelCity:function (selCityId) {
			for(var k in vm.selCityList){
				if(vm.selCityList[k].itemValue == selCityId){
					return vm.selCityList[k].itemName;
				}
			}
		},
		getSelStation:function (selStationId) {
			for(var k in vm.selStationList){
				if(vm.selStationList[k].itemValue == selStationId){
					return vm.selStationList[k].itemName;
				}
			}
		}
	}
});