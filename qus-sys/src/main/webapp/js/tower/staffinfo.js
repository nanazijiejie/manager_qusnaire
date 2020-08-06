var fileName = null;
var fileName2 = null;
$(function () {
    $("#jqGrid").Grid({
        url: '../tower/staffinfo/list',
        colModel: [
			{label: 'staffId', name: 'staffId', index: 'staff_id', key: true, hidden: true},
			{label: 'cityId', name: 'cityId', index: 'city_id', hidden: true},
			{label: 'deptId', name: 'deptId', index: 'dept_id', hidden: true},
			{label: 'riceDeptId', name: 'riceDeptId', index: 'rice_dept_id', hidden: true},
			{label: '姓名', name: 'staffName', index: 'staff_name', width: 100},
			{label: '归属单位', name: 'city', index: 'city', width: 90},
			{label: '归属部门', name: 'dept', index: 'dept', width: 90},
			//{label: '分管部门', name: 'riceDept', index: 'rice_dept', width: 90},
			{label: '职务', name: 'station', index: 'station', width: 120},
			{label: '邮箱', name: 'email', index: 'email', width: 120},
			{label: '手机号码', name: 'phone', index: 'phone', width: 120},

			//{label: '密码，作为登录问卷系统用', name: 'pwd', index: 'pwd', width: 80},
			{label: '邮件发送状态', name: 'sendStatus', index: 'send_status', width: 100,
				formatter: function (value) {
					if (value == '1') {
						return "发送成功";
					}else if (value == '2') {
						return "发送失败";
					}else{
						return "未发送";
					}
			    }
			},
			/*{label: '是否员工代表', name: 'isRepresent', index: 'is_represent', width: 100,
				formatter: function (value) {
					if (value == '1') {
						return "是";
					}else{
						return "否";
					}
				}
			},
			{label: '是否总监', name: 'isChief', index: 'is_chief', width: 80,
				formatter: function (value) {
					if (value == '1') {
						return "是";
					}else{
						return "否";
					}
				}
			},*/
			{label: '考核问卷是否提交', name: 'isSubmit', index: 'is_submit', width: 100,
				formatter: function (value) {
					if (value == '1') {
						return "已提交";
					}else{
						return "未提交";
					}
				}
			},
			{label: '密码是否修改', name: 'isUptPwd', index: 'is_upt_pwd', width: 100,
				formatter: function (value) {
					if (value == '1') {
						return "是";
					}else{
						return "否";
					}
				}
			},
			{label: '最近一次登录', name: 'lastLoginTime', index: 'last_login_time', width: 100, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '邮件发送时间', name: 'sendTime', index: 'create_time', width: 100, formatter: function (value) {
					return transDate(value);
				}
			}
			/*,
			{label: '创建人', name: 'createOperator', index: 'create_operator', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			},
			{label: '修改人', name: 'updateOperator', index: 'create_operator', width: 80},
			{label: '修改时间', name: 'updateTime', index: 'create_time', width: 80, formatter: function (value) {
					return transDate(value);
				}
			}*/
		],
		width:"2000",
		//autowidth:true
    });
	// $(window).resize(function(){
	// 	debugger;
	// 	$("#jqGrid").setGridWidth($(window).width());
	// });
	$("#uploadFile").click();
	$('#uploadFile').change(function (e) {
		fileName = e.target.files[0];//js 获取文件对象
		if (fileName !== undefined) {
			var file_typename = fileName.name.substring(fileName.name.lastIndexOf('.'));
			if (file_typename === '.xlsx' || file_typename === '.xls') {
				$("#filename").css("display", "block");
				$("#filename").val(fileName.name);
				//UpladFile(fileName);
			} else {
				console.log("请选择正确的文件类型！")
			}
		} else {
			console.log("请选择正确的文件！")
		}
	});
	$("#uploadFile2").click();
	$('#uploadFile2').change(function (e) {
		fileName2 = e.target.files[0];//js 获取文件对象
		if (fileName2 !== undefined) {
			var file_typename = fileName2.name.substring(fileName2.name.lastIndexOf('.'));
			if (file_typename === '.xlsx' || file_typename === '.xls') {
				$("#filename2").css("display", "block");
				$("#filename2").val(fileName2.name);
				//UpladFile(fileName);
			} else {
				console.log("请选择正确的文件类型！")
			}
		} else {
			console.log("请选择正确的文件！")
		}
	});
	//vm.qryDictDef();
	vm.getUser();
	//vm.qryDeptDef();
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
		showUpload :false,
		showUpload2 :false,
		showDept : true,
		showViceDept : true,
		showAdd: false,
        title: null,
		staffInfo: {},
		city:'',
		isUpdateInitial:false,
		ruleValidate: {
			staffName: [
				{required: true, message: '姓名不能为空', trigger: 'blur'}
			],
			cityId: [
				{required: true, message: '归属单位不能为空', trigger: 'blur'}
			],
			/*dept: [
				{required: true, message: '归属部门不能为空', trigger: 'blur'}
			],*/
			stationId: [
				{required: true, message: '职务不能为空', trigger: 'blur'}
			],
			email: [
				{required: true, message: '邮箱不能为空', trigger: 'blur'}
			],
			phone: [
				{required: true, message: '手机号码不能为空', trigger: 'blur'}
			]
		},
		q: {
			staff_name: '',
			staff_status:'',
			phone: '',
			email:'',
			city_id:'',
			is_submit:'',
			is_represent:'',
			is_chief:'',
			station_id:''
		},
		cityDefList:{},
		stationDefList:{},
		deptDefList:{},
		riceDeptDefList:{},
		user: {},
		showRicedept:false,
		deptIdArr:[],
		riceDeptIdArr:[],
		showIsChief:false,
		showIsRepresent:false,

	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.showUpload = false;
			vm.showUpload2 = false;
			vm.showAdd = true;
			vm.title = "新增";
			vm.staffInfo = {};
			vm.deptIdArr =[];
			vm.riceDeptIdArr =[];
			vm.showDept=true;
			vm.showViceDept=true;
		},
		batchAdd: function () {
			vm.showList = false;
			vm.showUpload = true;
			vm.showUpload2 = false;
			vm.showAdd = false;
		},
		batchAdd2: function () {
			vm.showList = false;
			vm.showUpload = false;
			vm.showUpload2 = true;
			vm.showAdd = false;
		},
		update: function (event) {
            var staffId = getSelectedRow("#jqGrid");
			if (staffId == null) {
				return;
			}
			vm.showList = false;
			vm.showUpload = false;
			vm.showUpload2 = false;
			vm.showAdd = true;
			vm.batchUpload = true;
            vm.title = "修改";
			vm.isUpdateInitial = true;
			vm.deptIdArr =[];
			vm.riceDeptIdArr =[];
            vm.getInfo(staffId);
		},
		saveOrUpdate: function (event) {
			if(!vm.staffInfo.email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/))
			{
				alert("邮箱格式不正确！请重新输入");
				return;
			}
			debugger;
            var url = vm.staffInfo.staffId == null ? "../tower/staffinfo/save" : "../tower/staffinfo/update";
            /**归属部门**/
            var deptId = vm.deptIdArr;
            if(deptId!=null&&deptId!=""){
				var deptIdStr = "";
				var dept = "";
				for(var i=0;i<deptId.length;i++){
					dept += vm.getDeptName(deptId[i])+",";
					deptIdStr += deptId[i] +",";
				}
				if(dept!=""){
					dept = dept.substring(0,dept.length-1);
				}
				if(deptIdStr!=""){
					deptIdStr = deptIdStr.substring(0,deptIdStr.length-1);
				}
				vm.staffInfo.deptId = deptIdStr;
				vm.staffInfo.dept = dept;
			}else{
				vm.staffInfo.deptId = "";
				vm.staffInfo.dept = "";
			}
			debugger;
            //正职或者副职不能选归属部门
            if(vm.staffInfo.stationId=='00'||vm.staffInfo.stationId=='01'||vm.staffInfo.stationId=='10'||vm.staffInfo.stationId=='11'){//00 01 10 11
				if(vm.staffInfo.deptId != ""){
					vm.staffInfo.deptId = "";
					vm.staffInfo.dept = "";
				}
			}else{
                if(vm.staffInfo.deptId == ""){
                	alert("请选择归属部门");
                	return ;
				}
			}
			/**归属部门**/
			/**分管部门**/
            var riceDeptId = vm.riceDeptIdArr;
            var riceDeptIdStr = "";
			var riceDept = "";
			if(riceDeptId!=null&&riceDeptId!=""){
				for(var i=0;i<riceDeptId.length;i++){
					riceDept += vm.getDeptName(riceDeptId[i])+",";
					riceDeptIdStr += riceDeptId[i] +",";
				}
				if(riceDept!=""){
					riceDept = riceDept.substring(0,riceDept.length-1);
				}
				if(riceDeptIdStr!=""){
					riceDeptIdStr = riceDeptIdStr.substring(0,riceDeptIdStr.length-1);
				}
				vm.staffInfo.riceDeptId = riceDeptIdStr;
				vm.staffInfo.riceDept = riceDept;
			}else{
				vm.staffInfo.riceDeptId = "";
				vm.staffInfo.riceDept = "";
			}
			debugger;
			if(vm.staffInfo.stationId=='00'||vm.staffInfo.stationId=='01'||vm.staffInfo.stationId=='10'||vm.staffInfo.stationId=='11'){//00 01 10 11
			}else{
				if(vm.staffInfo.riceDeptId != ""){
					vm.staffInfo.riceDeptId = "";
					vm.staffInfo.riceDept = "";
				}
			}
			if(vm.staffInfo.isChief==undefined){
				vm.staffInfo.isChief = "0";
			}
			if(vm.staffInfo.isRepresent==undefined){
				vm.staffInfo.isRepresent = "0";
			}
			/**分管部门**/
			Ajax.request({
			    url: url,
                params: JSON.stringify(vm.staffInfo),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		getDeptName:function(deptId){
			debugger;
			var arr = Object.keys(vm.deptDefList);
			var len = arr.length;
			for(var i=0;i<len;i++){
				if(vm.deptDefList[i].deptId == deptId){
					return  vm.deptDefList[i].name;
				}
			}
		},
		del: function (event) {
            var staffIds = getSelectedRows("#jqGrid");
			if (staffIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/staffinfo/delete",
                    params: JSON.stringify(staffIds),
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
        isPresent:function(event){
            var staffIds = getSelectedRows("#jqGrid");
            if (staffIds == null){
                return;
            }
            confirm('确定要将选中的记录设置为员工代表？', function () {
                Ajax.request({
                    url: "../tower/staffinfo/ispresent",
                    params: JSON.stringify(staffIds),
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
		sendMailById: function(event){
			var staffIds = getSelectedRows("#jqGrid");
			if (staffIds == null){
				return;
			}

			confirm('确定要发送登录问卷系统的邮件给选中的记录？', function () {
				Ajax.request({
					url: "../tower/staffinfo/sendMailById",
					params: JSON.stringify(staffIds),
					type: "POST",
					contentType: "application/json",
					successCallback: function (res) {
						if(res.failPhone==null||res.failPhone.length==0){
							alert('发送成功');
						}else{
							alert(res.failPhone+"发送失败，请检查邮箱地址是否有误！")
						}
						vm.reload();
					}
				});
			});
		},
		sendMailAll: function(event){
			confirm('确定要给所有员工发送登录问卷系统的邮件吗？', function () {
				alert("邮件发送结果稍后将发至您的邮箱，请注意查收！");
				$.ajax({
					url: '../tower/staffinfo/sendMailAll',                      //url地址
					type: 'POST',                 //上传方式
					dataType: 'JSON',
					timeout: 500
				});
				/*Ajax.request({
					url: "../tower/staffinfo/sendMailAll",
					type: "POST",
					contentType: "application/json",
					successCallback: function (res) {
						if(res.failPhone==null||res.failPhone.length==0){
							alert('发送成功');
						}else{
							alert(res.failPhone+"发送失败，请检查邮箱地址是否有误！")
						}
						vm.reload();
					}
				});*/
			});
		},
		getInfo: function(staffId){
            Ajax.request({
                url: "../tower/staffinfo/info/"+staffId,
                async: true,
                successCallback: function (r) {
                	debugger;
                    vm.staffInfo = r.staffInfo;
					if(vm.staffInfo.stationId=='00'||vm.staffInfo.stationId=='01'||vm.staffInfo.stationId=='10'||vm.staffInfo.stationId=='11'){
						vm.showDept = false;
					}else{
						vm.showViceDept = false;
					}
                    if(vm.staffInfo.deptId!=undefined&&vm.staffInfo.deptId!=null&&vm.staffInfo.deptId!=''){
						vm.deptIdArr = vm.staffInfo.deptId.split(",");
						for(var i=0;i<vm.deptDefList.length;i++){
							for(var j=0;j<vm.deptIdArr.length;j++){
								if(vm.deptDefList[i].deptId==vm.deptIdArr[j]){
									vm.deptDefList[i].isChecked = true;
									break;
								}
							}
						}
					}
					if(vm.staffInfo.riceDeptId!=undefined&&vm.staffInfo.riceDeptId!=null&&vm.staffInfo.riceDeptId!=''){
						vm.riceDeptIdArr = vm.staffInfo.riceDeptId.split(",");
						for(var i=0;i<vm.riceDeptDefList.length;i++){
							for(var j=0;j<vm.riceDeptIdArr.length;j++){
								if(vm.riceDeptDefList[i].deptId==vm.riceDeptIdArr[j]){
									vm.riceDeptDefList[i].isChecked = true;
									break;
								}
							}
						}
					}
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.showUpload = false;
			vm.showUpload2 = false;
			vm.showAdd = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'staffName': encodeURI(vm.q.staff_name),
					       'sendStatus': vm.q.send_status,
				           'phone':vm.q.phone,
					       'email':vm.q.email,
					       'cityId':vm.q.city_id,
					       'isSubmit':vm.q.is_submit,
				           'isRepresent':vm.q.is_represent,
					       'isChief':vm.q.is_chief,
					       'stationId':vm.q.station_id},

                page: 1
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
			vm.getUser();
		},
        reloadSearch: function() {
            vm.q = {
				staff_name: '',
				send_status:'',
				phone:'',
				email:'',
				city_id:'',
				is_submit:'',
				is_represent:'',
				is_chief:'',
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
		exportStaffInfo: function () {
			exportFile('#rrapp', '../tower/staffinfo/export',
				{
					'staffName': encodeURI(vm.q.staff_name),
					'sendStatus': vm.q.send_status,
					'phone':vm.q.phone,
					'email':vm.q.email,
					'cityId':vm.q.city_id,
					'isSubmit':vm.q.is_submit,
					'isRepresent':vm.q.is_represent,
					'isChief':vm.q.is_chief,
					'stationId':vm.q.station_id
				});
		},
		UpladFile: function () {
			if((vm.user.userId=='1'||vm.user.cityId=='Z')&&vm.city==""){
				alert("请选择归属单位！");
				return;
			}
			if(fileName==null){
				alert("请选择上传的文件！");
				return;
			}
			var form = new FormData(); // FormData 对象
			form.append("tempfile", fileName); // 文件对象
			form.append("city", encodeURI(vm.city));
			$.ajax({
				url: '../tower/staffinfo/batchSave',                      //url地址
				type: 'POST',                 //上传方式
				data: form,                   // 上传formdata封装的数据
				dataType: 'JSON',
				cache: false,                  // 不缓存
				processData: false,        // jQuery不要去处理发送的数据
				contentType: false,         // jQuery不要去设置Content-Type请求头
				success: function (data) {           //成功回调
					console.log(data);
					if(data.msg==undefined){
						alert("导入成功！");
					}else{
						alert(data.msg);
					}
				},
				error: function (data) {           //失败回调
					console.log(data);
					alert(data.msg);

				}
			});
		},
		UpladFile2: function () {
			if(fileName2==null){
				alert("请选择上传的文件！");
				return;
			}
			var form = new FormData(); // FormData 对象
			form.append("tempfile", fileName2); // 文件对象
			$.ajax({
				url: '../tower/staffinfo/batchSave2',                      //url地址
				type: 'POST',                 //上传方式
				data: form,                   // 上传formdata封装的数据
				dataType: 'JSON',
				cache: false,                  // 不缓存
				processData: false,        // jQuery不要去处理发送的数据
				contentType: false,         // jQuery不要去设置Content-Type请求头
				success: function (data) {           //成功回调
					console.log(data);
					if(data.msg==undefined){
						alert("修改成功！");
					}else{
						alert(data.msg);
					}
				},
				error: function (data) {           //失败回调
					console.log(data);
					alert(data.msg);

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
					var count = 0;
					var count1 = 0;
					debugger;
					for(var i =0;i<r.list.length;i++){
						if(r.list[i].typeCode=="CITY"){
							vm.cityDefList[count++] = r.list[i];
						}
						if(r.list[i].typeCode=="STATION"){
							if(cityId!=undefined){
								if(cityId!="Z"&&r.list[i].itemName.indexOf("市")!=-1){
									vm.stationDefList[count1++] = r.list[i];
								}
								if(cityId=="Z"&&r.list[i].itemName.indexOf("省")!=-1){
									vm.stationDefList[count1++] = r.list[i];
								}
							}else{
								if(vm.user.cityId=='Z'||vm.user.userId=='1'){
									vm.stationDefList[count1++] = r.list[i];
								}else{
									if(r.list[i].itemName.indexOf("市")!=-1){
										vm.stationDefList[count1++] = r.list[i];
									}
								}
							}
						}
					}
				}
			});

		},
		qryDeptDef:function(cityId){
			var url = "../sys/dept/list";
			Ajax.request({
				url: url,
				params: {
					"cityId":cityId
				},
				type: "POST",
				contentType: "application/json",
				successCallback: function (r) {
					var count1 = 0;
					var count2 = 0;
					vm.deptDefList = {};
                    vm.riceDeptDefList = {};
					if(vm.isUpdateInitial==false){
						vm.deptIdArr =[];
						vm.riceDeptIdArr =[];
					}else{
						vm.isUpdateInitial = false;
					}
					debugger;
					for(var i=0;i<r.list.length;i++){
						if(cityId==r.list[i].cityId){
							vm.riceDeptDefList[count1] = r.list[i];
							vm.riceDeptDefList[count1].isChecked = false;
							vm.deptDefList[count2] = r.list[i];
							vm.deptDefList[count2].isChecked = false;
							count1++;
							count2++;
						}
					}
				}
			});
		},
		getUser: function () {
			$.getJSON("../sys/user/info?_" + $.now(), function (r) {
				vm.user = r.user;
				vm.qryDictDef();
				if(vm.user.userId!='1'&&vm.user.cityId!='Z'){
					vm.qryDeptDef(vm.user.cityId);
				}
			});
		},
		stationChange:function () {
			if(vm.staffInfo.stationId=='11'||vm.staffInfo.stationId=='01'){
				vm.showRicedept = true;
			}
		},
		cityOnchange:function(){
			debugger;
			vm.qryDeptDef(vm.staffInfo.cityId);
			//选择地市时移除省职务，选择省时移除地市职务
			vm.qryDictDef(vm.staffInfo.cityId);
		},
		setIsRepresent:function(){
			debugger;
			if(vm.staffInfo.isChief=="1"){
				vm.showIsRepresent = true;
			}else{
				if(vm.staffInfo.stationId!='04'&&vm.staffInfo.stationId!='13'){
					vm.staffInfo.isRepresent = "0";
				}
				vm.showIsRepresent = false;
			}
		},
		setIsChief:function(){
			if(vm.staffInfo.stationId=='03'){
				vm.showIsChief = true;
			}else{
				vm.showIsChief = false;
			}
			if(vm.staffInfo.stationId=='04'||vm.staffInfo.stationId=='13'){
				//vm.showIsRepresent = false;
				vm.staffInfo.isRepresent = "1";
			}else{
				//vm.showIsRepresent = true;
			}
			if(vm.staffInfo.stationId=='00'||vm.staffInfo.stationId=='01'||vm.staffInfo.stationId=='10'||vm.staffInfo.stationId=='11'){
				vm.showDept = false;
				vm.showViceDept = true;
			}else{
				vm.showViceDept = false;
				vm.showDept = true;
			}
		},
		downloadExcel:function(){
			debugger;
			if(vm.city==""){
				alert("请先选择归属单位！");
				return;
			}
			Ajax.request({
				url: "../tower/staffinfo/downloadExcel",
				params: {
					"cityId":vm.city
				}
			});
		},
		downloadExcel2:function(){
			Ajax.request({
				url: "../tower/staffinfo/downloadExcel2"
			});
		}
},
});