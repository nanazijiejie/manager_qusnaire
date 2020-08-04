$(function () {
    $("#jqGrid").Grid({
        url: '../tower/maildef/list',
        colModel: [
			{label: '发件人邮箱', name: 'mailFrom', index: 'mail_from', width: 80},
			{label: '发件人邮箱密码', name: 'passwordMailFrom', index: 'password_mail_from', width: 80},
			{label: '邮件标题', name: 'mailTitle', index: 'mail_title', width: 80},
			{label: '邮件服务器域名', name: 'mailHost', index: 'mail_host', width: 80},
			{label: '问卷系统链接地址', name: 'qusUrl', index: 'qus_url', width: 80},
			{label: '邮件内容', name: 'qusContent', index: 'qus_content', width: 80},
			{label: 'mailId', name: 'mailId', index: 'mail_id', key: true, hidden: true},
			{label: '状态', name: 'delFlag', index: 'del_flag', width: 80,formatter:function(value){
				if(value=='0'){
					return "在用";
				}else{
					return "废弃"
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
		mailDef: {},
		ruleValidate: {
			mailFrom: [
				{required: true, message: '发件人邮箱不能为空', trigger: 'blur'}
			],
			passwordMailFrom: [
				{required: true, message: '发件人邮箱密码不能为空', trigger: 'blur'}
			],
			mailTitle: [
				{required: true, message: '邮件标题不能为空', trigger: 'blur'}
			],
			mailHost: [
				{required: true, message: '邮件服务器域名不能为空', trigger: 'blur'}
			],
			qusUrl: [
				{required: true, message: '问卷系统链接地址不能为空', trigger: 'blur'}
			],
			qusContent: [
				{required: true, message: '邮件内容不能为空', trigger: 'blur'}
			],
			delFlag: [
				{required: true, message: '请选择状态', trigger: 'blur'}
			]
		},
		q: {
			mailTitle: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.mailDef = {};
		},
		update: function (event) {
            var mailId = getSelectedRow("#jqGrid");
			if (mailId == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(mailId)
		},
		saveOrUpdate: function (event) {
            var url = vm.mailDef.mailId == null ? "../tower/maildef/save" : "../tower/maildef/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.mailDef),
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
            var mailIds = getSelectedRows("#jqGrid");
			if (mailIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
               Ajax.request({
				    url: "../tower/maildef/delete",
                    params: JSON.stringify(mailIds),
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
		getInfo: function(mailId){
            Ajax.request({
                url: "../tower/maildef/info/"+mailId,
                async: true,
                successCallback: function (r) {
                    vm.mailDef = r.mailDef;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'mailTitle': encodeURI(vm.q.mailTitle)},
                page: 1
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
				mailTitle: ''
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