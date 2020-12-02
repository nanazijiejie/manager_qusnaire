var scoreArr = [];
var selArr=[];
//var HOST = "http://localhost:8080/qus/";
var HOST = "";//http://129.28.85.6:8084/qus/
$(function () {
    var endIndex = window.location.href.indexOf("qus");
    HOST = window.location.href.substring(0,endIndex+4);
    toHandle();
    var userName = window.sessionStorage.getItem("userName");
    var staffName = window.sessionStorage.getItem("staffName");
    //$("#user-name-p").html(staffName+"（"+userName+"）");


});
function setReason(reason,selStationId,index){
    for(var j=0;j<selArr.length;j++) {
        if (selArr[j].selStationId == selStationId && selArr[j].selIndex == index) {
            selArr[j].reason = reason;
            break;
        }
    }
}
function setOtherName(otherName,selStationId,index){
    for(var j=0;j<selArr.length;j++) {
        if (selArr[j].selStationId == selStationId && selArr[j].selIndex == index) {
            selArr[j].selName = otherName;
            break;
        }
    }
}
function setCityStation(cityStation,selStationId,index){
    for(var j=0;j<selArr.length;j++) {
        if (selArr[j].selStationId == selStationId && selArr[j].selIndex == index) {
            selArr[j].selDept = cityStation;
            break;
        }
    }
}
function getStationById(staffId,selStationId,selStation,cityId,city,index){
    var selStaffInfo = getSession("selStaffInfo");
    var staffInfo;
    var i = -1;
    for(var j=0;j<selStaffInfo.length;j++){
        if(staffId==selStaffInfo[j].staffId){
            i = j;
            break;
        }
    }
    if(i!=-1) {
        $("#cityStation_" + selStationId + "_" + index).val(selStaffInfo[i].city + "，" + selStaffInfo[i].selNowStation);
    }else{
        $("#cityStation_" + selStationId + "_" + index).val("");
    }
    $("#selReason_" + selStationId + "_" + index).val("");
    var hasValue = false;
    var repeatIndex;
    for (var j = 0; j < selArr.length; j++) {
        if (selArr[j].selStationId == selStationId && selArr[j].selIndex == index) {
            hasValue = true;
            repeatIndex = j;
            break;
        }
    }
    if (hasValue) {
        selArr.splice(repeatIndex, 1);
    }
    var selItem = new Object();
    if(staffId=='99999999'){
        selItem.selStaffId = '99999999';
        $("#cityStation_" + selStationId + "_" + index).removeAttr("readonly");
        $("#otherName_"+selStationId+"_"+index).attr("style","border: 1px solid #d0dafd;");
    }else{
        $("#cityStation_" + selStationId + "_" + index).attr("readonly","readonly");
        $("#otherName_"+selStationId+"_"+index).attr("style","display:none;");
    }
    if(staffId==''){
        selItem.selStaffId = '00000000';
    }
    if(staffId==''||staffId=='99999999'){
        selItem.selStationId = selStationId;
        selItem.selStation = selStation;
        selItem.selName = '';
        selItem.selCityId = cityId;
        selItem.selCity = city;
        selItem.selPreStationId = '';
        selItem.selPreStation = '';
        selItem.selDept = '';
        selItem.reason = '';
        selItem.selIndex = index;
    }else{
        selItem.selStationId = selStaffInfo[i].selStationId;
        selItem.selStation = selStaffInfo[i].selStation;
        selItem.selName = selStaffInfo[i].staffName;
        selItem.selCityId = selStaffInfo[i].cityId;
        selItem.selCity = selStaffInfo[i].city;
        selItem.selPreStationId = selStaffInfo[i].stationId;
        selItem.selPreStation = selStaffInfo[i].station;
        selItem.selDept = selStaffInfo[i].dept;
        selItem.selStaffId = selStaffInfo[i].staffId;
        selItem.selIndex = index;
        selItem.reason = '';
    }
    selArr.push(selItem);
}
//展示候选人信息
function showSelStallInfo(){
    var selStaffInfo = getSession("selStaffInfo");
    var examInfoHtml = "";
    examInfoHtml += "<div class=\"exam-t\">"+"</div>";
    examInfoHtml += "<div class=\"grade-main\" style='font-size:12px'><ul>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"组别"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selStation+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"姓名"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].staffName+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"单位"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].city+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"职务"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selNowStation+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"岗位年限（按文件）"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selTakeOfficeYears+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"最高学历"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selEdu+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\" style='border-right:none'>"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selPerform3+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\" style='border-right:none'>"+"近三年绩效"+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selPerform2+"</div>";
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+""+"</div>";
    for(var i=0;i<selStaffInfo.length;i++){
        examInfoHtml += "<div class=\"grade-b\" >"+selStaffInfo[i].selPerform1+"</div>";
    }
    examInfoHtml += "</li>";


    examInfoHtml+="</ul></div>";

    $("#selStaffInfo").html(examInfoHtml);
}
function showSelTable(){
    selArr=[];
    $(".exam-alt").hide();
    $("#examInfoAll").hide();
    $("#exam").css("height","100%");
    $("#selTable").show();
    var selStaffInfo = getSession("selStaffInfo");
    var selDefInfo = getSession("selDefInfo");
    var selTitle =  getSession("selDefInfo")[0].selName;
    $("#selTitle").html(selTitle);//选举名称
    var examInfoHtml = "";
    examInfoHtml += "<div class=\"exam-t\">"+"</div>";
    examInfoHtml += "<div class=\"grade-main\"><ul>";
    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"推荐职务"+"</div>";
    for(var i=0;i<selDefInfo.length;i++) {
        for (var j = 0; j < selDefInfo[i].selCount; j++) {
            examInfoHtml += "<div class=\"grade-b\" >"+selDefInfo[i].selStation+"</div>";
        }
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"姓名"+"</div>";
    for(var i=0;i<selDefInfo.length;i++) {
        var selStationId = selDefInfo[i].selStationId;
        var selStation = selDefInfo[i].selStation;
        var cityId = selDefInfo[i].selCityId;
        var city = selDefInfo[i].selCity;
        for (var j = 0; j < selDefInfo[i].selCount; j++) {
            var selItem = new Object();
            selItem.selStaffId = '00000000';
            selItem.selStationId = selStationId;
            selItem.selStation = selStation;
            selItem.selName = '';
            selItem.selCityId = cityId;
            selItem.selCity = city;
            selItem.selPreStationId = '';
            selItem.selPreStation = '';
            selItem.selDept = '';
            selItem.selIndex = j;
            selArr.push(selItem);
            var selStaffNameId = "name_"+selStationId+"_"+j;
            examInfoHtml += "<div class=\"grade-b\" ><select id=\""+selStaffNameId+"\" onchange=\"getStationById(this.value,"+selStationId+",'"+selStation+"','"+cityId+"','"+city+"',"+j+")\">" ;
            for(var k=0;k<selStaffInfo.length;k++){
                if(k==0){
                    examInfoHtml +="<option value=''>无</option>" ;
                }
                if(selStaffInfo[k].selStationId==selStationId){
                    examInfoHtml +="<option value="+"\""+selStaffInfo[k].staffId+"\""+">"+selStaffInfo[k].staffName+"</option>" ;
                }
                if(k==selStaffInfo.length-1){
                    examInfoHtml +="<option value='99999999'>其他</option>" ;
                }
            }
            var otherStaffNameId = "otherName_"+selStationId+"_"+j;
            examInfoHtml += "</select><input type='text' placeholder='请输入姓名' style='display:none' class='input' onblur=\"setOtherName(this.value,"+selStationId+","+j+")\" id=\""+otherStaffNameId+"\"></div>";
        }
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"现单位及职务"+"</div>";
    for(var i=0;i<selDefInfo.length;i++) {
        var selStationId = selDefInfo[i].selStationId;
        for (var j = 0; j < selDefInfo[i].selCount; j++) {
            var cityAndStationId = "cityStation_"+selStationId+"_"+j;
            examInfoHtml += "<div class=\"grade-b\" ><input  id=\""+cityAndStationId+"\" type=\"text\"  onblur=\"setCityStation(this.value,"+selStationId+","+j+")\" style=\"outline:none\" readonly=\"readonly\"/></div>";
        }
    }
    examInfoHtml += "</li>";

    examInfoHtml += "<li>";
    examInfoHtml += "<div class=\"grade-a\">"+"推荐理由"+"</div>";
    for(var i=0;i<selDefInfo.length;i++) {
        var selStationId = selDefInfo[i].selStationId;
        for (var j = 0; j < selDefInfo[i].selCount; j++) {
            var selReasonId = "selReason_"+selStationId+"_"+j;
            examInfoHtml += "<div class=\"grade-b\" ><input  id=\""+selReasonId+"\" type=\"text\" onblur=\"setReason(this.value,"+selStationId+","+j+")\" style=\"outline:none\" /></div>";
        }
    }
    examInfoHtml += "</li>";
    examInfoHtml+="</ul></div>";
    //var msgTipId = "msgTip"+"_"+examItemId;
    examInfoHtml += "<div style='margin-bottom: 30px'></div>";// <span id=\""+msgTipId+"\" style='color:red;font-size:16px;'></span>
    $("#selInfo").html(examInfoHtml);
    showSelStallInfo();
}
function goLogin(){
    window.location.href = HOST + "html/tower/login.html";
}
function getResAvg(){
   var totalScore = 0;
   for(var i=1;i<=10;i++){
       if($("#responseSel"+i).val()!=''){
           totalScore += new Number($("#responseSel"+i).val());
       }
   }
   $("#responseTip").html("总分："+(totalScore/3).toFixed(2));
   //return (totalScore/3).toFixed(2);
}
function getClearAvg(){
    var totalScore = 0;
    var validCount = 0;
    for(var i=1;i<=10;i++){
        if($("#clearSel"+i).val()!=''){
            totalScore += new Number($("#clearSel"+i).val());
            validCount++;
        }
    }
    $("#clearTip").html("总分："+((totalScore/(validCount*3))*20).toFixed(2));
    //return (totalScore/3).toFixed(2);
}
function toHandle(){
    scoreArr = [];
    selArr=[];
    $(".load").hide();
    if(window.sessionStorage.getItem("token")==null){
        window.location.href = HOST + "html/tower/login.html";
        return;
    }
    if(!checkTokenTime()) return;
    checkQusLogin(function(){
        var isSubmit = window.sessionStorage.getItem("isSubmit");
        var isSelSubmit = window.sessionStorage.getItem("isSelSubmit");
        var isSelection = window.sessionStorage.getItem("isSelection");
        var isClear = window.sessionStorage.getItem("isClear");
        var isClearSubmit = window.sessionStorage.getItem("isClearSubmit");
        var isResponse = window.sessionStorage.getItem("isResponse");
        var isResponseSubmit = window.sessionStorage.getItem("isResponseSubmit");
        var city = window.sessionStorage.getItem("city");
        $(".exam-alt").show();
        $("#selTable").hide();
        $("#clearTable").hide();
        $("#responseTable").hide();
        $("#examInfoAll").hide();
        $("#exam").height($(window).height()-60);
        //问卷考核已提交&&(民主投票已提交||不需要民主投票)
        if(isSubmit=="1"&&
            (isSelection=='1'&&isSelSubmit=='1'||isSelection=='0')&&
            (isClear=='1'&&isSelSubmit=='1'||isClear=='0')&&
            (isResponse=='1'&&isResponseSubmit=='1'||isResponse=='0')){
            $("#examB").hide();
            $("#examC").hide();
            $("#examD").hide();
            $("#examA").show();
            $("#examA").html("您没有待处理的绩效考评！");
            $("#examA").css("color","#000");
            $("#examA").css("text-decoration","");
            $("#examA").unbind();
        }else{
            if(isSubmit=='0'){
                $("#examA").show();
                $("#examA").html(getSession("qusInfo").qusNaireName);
                $("#examA").css("color","#3c61f8");
                $("#examA").css("text-decoration","underline");
                $("#examA").click(init);
                $(".exam-save").click(function(){
                    doSubmit("0");
                });
            }else{
                $("#examA").hide();
            }
            if(isSelection=='1'&&isSelSubmit=='0'){
                $("#examB").html("民主推荐");
                $("#examB").show();
                $("#examB").click(showSelTable);
                $("#selSubmitBtn").click(doSelSubmit);
            }else{
                $("#examB").hide();
            }

            if(isClear=='1'&&isClearSubmit=='0'){
                $("#clearHead").html("2019党风廉政建设和反腐败工作责任制考核民主测评表");
                $("#examD").html("2019党风廉政建设和反腐败工作责任制考核民主测评表");
                $("#examD").show();
                $("#examD").click(function(){
                    $("#exam").css("height","100%");
                    $("#examB").hide();
                    $("#examC").hide();
                    $("#examD").hide();
                    $("#examA").hide();
                    $("#clearTable").show();
                });
                $("#clearSubmitBtn").click(doClearSubmit);
            }else{
                $("#examD").hide();
            }

            if(isResponse=='1'&&isResponseSubmit=='0'){
                $("#responseHead").html("2019年度"+city+"市分公司党建工作责任制考核民主测评打分表");
                $("#examC").html("2019年度"+city+"市分公司党建工作责任制考核民主测评打分表");
                $("#examC").show();
                $("#examC").click(function(){
                    $("#exam").css("height","100%");
                    $("#examB").hide();
                    $("#examC").hide();
                    $("#examD").hide();
                    $("#examA").hide();
                    $("#responseTable").show();
                });
                $("#resSubmitBtn").click(doResponseSubmit);
            }else{
                $("#examC").hide();
            }
        }

    });
}
function init(){
    scoreArr = [];
    $(".load").hide();
    if(window.sessionStorage.getItem("token")==null){
        window.location.href = HOST + "html/tower/login.html";
        return;
    }
    if(!checkTokenTime()) return;
    checkQusLogin(function(){
        setExamInfo();
    });

}
function setExamInfo(){
    var isSubmit = window.sessionStorage.getItem("isSubmit");
    if(isSubmit=="1"){
        $(".exam-alt").show();
        $("#selTable").hide();
        $("#examInfoAll").hide();
    }else{
        $("#exam").css("height","100%");
        $(".exam-alt").hide();
        $("#selTable").hide();
        $("#examInfoAll").show();
        $("#examSubmit").show();
        $(".exam-abs").show();
    }
    var qusTitle =  getSession("qusInfo").qusNaireName;
    $("#qusTitle").html(qusTitle);
    var headDesc =  getSession("qusInfo").headDesc;
    var bottomDesc =  getSession("qusInfo").bottomDesc;
    $("#bottomDesc").html(showAreaText(bottomDesc));
    $("#headDesc").html(showAreaText(bottomDesc));
    var examStaffInfo = getSession("examStaffInfo");
    var examItemInfo = getSession("examItemInfo");
    var examInfoHtml = "";
    var totalExamItem = 1;
    for(var i=0;i<examItemInfo.length;i++){
        var examIndexRels = examItemInfo[i].examIndexRels;
        var examItemDesc = examItemInfo[i].examItemDesc;
        var examItemId = examItemInfo[i].examItemId;
        var examStationId = examItemInfo[i].examStationId;
        var excellentCount = examItemInfo[i].excellentCount;
        var goodCount = examItemInfo[i].goodCount;
        var normalCount = examItemInfo[i].normalCount;
        var examItemName = examItemInfo[i].examItemName;
        var examStation = examItemInfo[i].examStation;
        var retExamStaffInfo = examStaffInfo;//getStaffInfo(examStaffInfo,examStationId);
        var arr = Object.keys(retExamStaffInfo);
        if(arr.length==0){
            continue;
        }
        examInfoHtml += "<div class=\"exam-t\">"+examItemName+"</div>";
        examInfoHtml += "<div class=\"grade-main\"><ul>";
        examInfoHtml += "<li>";
        examInfoHtml += "<div class=\"grade-a\">"+"姓名"+"</div>";
        for(var k=0;k<arr.length;k++){
            if(examItemId==97||examItemId==98){
                examInfoHtml += "<div class=\"grade-b-special\">"+retExamStaffInfo[k].staffName+"</div>";
            }else{
                examInfoHtml += "<div class=\"grade-b\">"+retExamStaffInfo[k].staffName+"</div>";
            }
        }
        examInfoHtml += "</li>";
        //var retExamIndexRels = getExamIndexRels(examIndexRels,examItemId);
        var setRet = true;
        for(var j=0;j<examIndexRels.length;j++){
            examInfoHtml += "<li>";
            examInfoHtml += "<div class=\"grade-a\">"+examIndexRels[j].indexItemName+"</div>";
            for(m=0;m<arr.length;m++){
                var staffId = retExamStaffInfo[m].staffId;
                var indexItemId = examIndexRels[j].indexItemId;
                var scoreIdName = "score_"+staffId+"_"+examItemId;
                setSession("staff_"+scoreIdName,retExamStaffInfo[m]);
                setSession("index_"+scoreIdName,examIndexRels[j]);
                debugger;
                var lastScore = getLastScore(staffId,examItemId,indexItemId);
                if(lastScore!=undefined&&lastScore!=""){
                    var setRet = setScoreInfo(scoreIdName,lastScore,examItemId,examItemName,excellentCount,goodCount,normalCount);
                    if(setRet==false){
                        break;
                    }
                }else{
                    lastScore = "";
                }
                if(examItemId==97||examItemId==98){
                    examInfoHtml += "<div class=\"grade-b-special\">"+
                        "<textarea style='width:100%;height:100%;border-radius:10px;' id=\""+scoreIdName+"\"  value=\""+lastScore+"\" style=\"outline:none\""+
                        "placeholder='请举例或者重点说明，不得超过500字' onchange= \"setScoreInfo(this.id,this.value,"+examItemId+",'"+examItemName+"',"+excellentCount+","+goodCount+","+normalCount+")\">" +
                        lastScore+"</textarea></div>";
                }else{
                    examInfoHtml += "<div class=\"grade-b\">"+
                        "<select id=\""+scoreIdName+"\" type=\"text\" value=\""+lastScore+"\" style=\"outline:none\""+
                        "onchange= \"setScoreInfo(this.id,this.value,"+examItemId+",'"+examItemName+"',"+excellentCount+","+goodCount+","+normalCount+")\">" +
                        "<option value=''></option>";
                    if(examItemId==94||examItemId==95||examItemId==96){
                        examInfoHtml += getSpecialScoreSelectStr(lastScore);
                    }else{
                        examInfoHtml += getNormalScoreSelectStr(lastScore);
                    }
                    examInfoHtml += "</select></div>";
                }

            }
            examInfoHtml += "</li>";
            if(setRet==false){
                break;
            }
        }
        if(examItemId!=97&&examItemId!=98){
            examInfoHtml += "<li>";
            examInfoHtml += "<div class=\"grade-a\">"+"得分"+"</div>";
            for(m=0;m<arr.length;m++){
                var staffId = retExamStaffInfo[m].staffId;
                var scoreIdName = "totalscore_"+staffId+"_"+examItemId;
                setSession("staff_"+scoreIdName,retExamStaffInfo[m]);
                var lastScore = getLastScore(staffId,examItemId,'99999999');
                if(lastScore==undefined||lastScore==""){
                    lastScore = "";
                }
                examInfoHtml += "<div class=\"grade-b\"><input readonly=\"readonly\" id=\""+scoreIdName+"\" value=\""+lastScore+"\" type=\"text\" style=\"outline:none\" /></div>";
            }
            examInfoHtml += "</li>";
        }
        examInfoHtml+="</ul></div>";
        var msgTipId = "msgTip"+"_"+examItemId;
        examInfoHtml += "<div style='margin-bottom: 30px'> <span id=\""+msgTipId+"\" style='color:red;font-size:16px;'></span></div>";
    }
    $("#examInfo").html(examInfoHtml);
}
function getSpecialScoreSelectStr(lastScore){
    var examInfoHtml = "";
    if(lastScore=='1'){
        examInfoHtml += "<option value='1' selected='selected' >完全同意</option>";
    }else{
        examInfoHtml += "<option value='1'>完全同意</option>";
    }
    if(lastScore=='2'){
        examInfoHtml += "<option value='2' selected='selected' >同意</option>";
    }else{
        examInfoHtml += "<option value='2'>同意</option>";
    }
    if(lastScore=='3'){
        examInfoHtml += "<option value='3' selected='selected' >一般</option>";
    }else{
        examInfoHtml += "<option value='3'>一般</option>";
    }
    if(lastScore=='4'){
        examInfoHtml += "<option value='4' selected='selected' >不同意</option>";
    }else{
        examInfoHtml += "<option value='4'>不同意</option>";
    }
    if(lastScore=='5'){
        examInfoHtml += "<option value='5' selected='selected' >完全不同意</option>";
    }else{
        examInfoHtml += "<option value='5'>完全不同意</option>";
    }
    if(lastScore=='0'){
        examInfoHtml += "<option value='0' selected='selected' >不涉及</option>";
    }else{
        examInfoHtml += "<option value='0'>不涉及</option>";
    }
    return examInfoHtml;
}
function getNormalScoreSelectStr(lastScore){
    var examInfoHtml = "";
    if(lastScore=='5'){
        examInfoHtml += "<option value='5' selected='selected' >完全同意</option>";
    }else{
        examInfoHtml += "<option value='5'>完全同意</option>";
    }
    if(lastScore=='4'){
        examInfoHtml += "<option value='4' selected='selected' >同意</option>";
    }else{
        examInfoHtml += "<option value='4'>同意</option>";
    }
    if(lastScore=='3'){
        examInfoHtml += "<option value='3' selected='selected' >一般</option>";
    }else{
        examInfoHtml += "<option value='3'>一般</option>";
    }
    if(lastScore=='2'){
        examInfoHtml += "<option value='2' selected='selected' >不同意</option>";
    }else{
        examInfoHtml += "<option value='2'>不同意</option>";
    }
    if(lastScore=='1'){
        examInfoHtml += "<option value='1' selected='selected' >完全不同意/未做到</option>";
    }else{
        examInfoHtml += "<option value='1'>完全不同意/未做到</option>";
    }
    if(lastScore=='0'){
        examInfoHtml += "<option value='0' selected='selected' >不涉及</option>";
    }else{
        examInfoHtml += "<option value='0'>不涉及</option>";
    }
    return examInfoHtml;
}
function showAlert(msg){
    $("#alert").show();
    $("#alertMsg").html(msg);
}
function closeAlert(){
    $("#alert").hide();
}
function closeConfirm(){
    $("#confirm").hide();
}
function showConfirm(msg,functionName){
    $("#confirm").show();
    $("#confirmMsg").html(msg);
    $("#confirmA").off('click');
    $("#confirmA").on('click',functionName);
}
function showAreaText(content){
    var index=0;
    while((index=content.indexOf("\n"))!=-1){
        content=content.substring(0,index)+"<br>"+content.substring(index+1);
    }
    while((index=content.indexOf(" "))!=-1){
        content=content.substring(0,index)+"&nbsp"+content.substring(index+1);
    }
    return content;

}
function getLastScore(staffId,examItemId,indexItemId){
    if(getSession("examScoreInfo")!=null){
        var examScoreInfo = getSession("examScoreInfo");
        for(var i=0;i<examScoreInfo.length;i++){
            if(examScoreInfo[i].examStaffId == staffId
                &&examScoreInfo[i].examItemId == examItemId
                &&examScoreInfo[i].indexItemId == indexItemId){
                return examScoreInfo[i].indexItemScore;
            }

        }
    }else{
        return "";
    }
}
function checkTokenTime(){
    var tokenBeginTime = window.sessionStorage.getItem("tokenTime");
    var nowTime = new Date().getTime();
    if(nowTime-tokenBeginTime>2*60*60*1000){//2*60*60*1000
        showAlert("您未登录或已失效，请重新登录！");
        return false;
    }
    return true;
}
function checkQusLogin(callback){
    var url = "../../tower/staffinfo/checkQusLogin?number="+Math.random();
    $.ajax({
        //与服务器交互
        url:url,
        type:'post',
        dataType: 'json',
        data: {
            "token":window.sessionStorage.getItem("token")
        },
        complete: function (XMLHttpRequest, textStatus) {

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            showAlert("发生错误："+errorThrown);
        },
        success: function(res){
            if(res.msg!=undefined){
                showAlert(res.msg);
                if(res.msg.indexOf('未登录或已失效')!=-1){
                    window.location.href = HOST + "html/tower/login.html";
                }
            }else{
                window.sessionStorage.setItem("isSubmit",res.staffInfo.isSubmit);
                window.sessionStorage.setItem("isSelSubmit",res.staffInfo.isSelSubmit);
                window.sessionStorage.setItem("isSelection",res.staffInfo.isSelection);
                window.sessionStorage.setItem("isClear",res.staffInfo.isClear);
                window.sessionStorage.setItem("isClearSubmit",res.staffInfo.isClearSubmit);
                window.sessionStorage.setItem("isResponse",res.staffInfo.isResponse);
                window.sessionStorage.setItem("isResponseSubmit",res.staffInfo.isResponseSubmit);

                callback();
            }
        }
    });
}
//0-100分。90-100分A(优秀)、
// 70-90（不含）分B（优良）、
// 50-70（不含）分C（称职）、
// 30-50（不含）分D（须改进）、
// 0-30（不含）分E（不称职）
//0-100分。90-100分A(优秀)、 70-89分B（优良）、50-69分C（称职）、30-49分D（须改进）、0-29分E（不称职）
function getLevelName(score){
    if(score>=90&&score<=100){
        return "优秀";
    }
    if(score>=70&&score<90){
        return "优良";
    }
    if(score>=50&&score<70){
        return "称职";
    }
    if(score>=30&&score<50){
        return "须改进";
    }
    if(score>=0&&score<30){
        return "不称职";
    }
}
//90-100分为表现突出；70-90（不含）分为表现较好；50-70（不含）分为表现一般；40分及以下为表现较差。
function getLevelName2(score){
    if(score>=90&&score<=100){
        return "突出";
    }
    if(score>=70&&score<90){
        return "较好";
    }
    if(score>=50&&score<70){
        return "一般";
    }
    if(score>=0&&score<50){
        return "较差";
    }
}
function isExcellent(score){
    if(score>=90&&score<=100){
        return true;
    }
}
function isGood(score){
    if(score>=70&&score<90){
        return true;
    }
}
function setScoreInfo(objId,objValue,examItemId,examItemName,excellentCountLimit,goodCountLimit,normalCountLimit){
    if(!checkTokenTime()){
        return false;
    }
    var id = objId;
    var staffId = "";//被评价的用户ID
    //var indexItemId = "";//评价的指标项
    var scoreType = "";
    var staffInfo = getSession("staff_"+id);
    var indexItemInfo = getSession("index_"+id);
    if(id.indexOf("totalscore")==-1){//各项得分
        var beginIndex = id.indexOf('_')+1;
        var endIndex = id.lastIndexOf('_');
        staffId = id.substring(beginIndex,endIndex);
        //indexItemId = id.substring(endIndex+1,id.length);
        scoreType = '0';
    }else{//总得分
        var beginIndex = id.indexOf('_')+1;
        staffId = id.substring(beginIndex,id.length);
        scoreType = '1';
    }
    var score = new Object();
    score.pageId = id;
    score.examStation = staffInfo.stationId;
    score.examStationName = staffInfo.station;
    score.examName = staffInfo.staffName;
    score.examDept = staffInfo.dept;
    score.examCity = staffInfo.city;
    score.examStaffId = staffInfo.staffId;
    score.examDeptId = staffInfo.deptId;
    score.examCityId = staffInfo.cityId;
    score.indexItemId = indexItemInfo.indexItemId;
    score.indexItemName = indexItemInfo.indexItemName;
    score.indexItemPercent = indexItemInfo.percent;
    score.indexItemScore = objValue;
    score.scoreType = scoreType;
    score.examItemId = examItemId;
    score.examItemName = examItemName;
    var hasValue = false;
    var repeatIndex ;
    var totalIndex;
    var hasTotalValue = false;
    for(var i=0;i<scoreArr.length;i++){
        if(scoreArr[i].pageId==id){
            hasValue = true;
            repeatIndex = i;
            break;
        }

    }
    if(hasValue){
        scoreArr.splice(repeatIndex,1);
    }
    for(var i=0;i<scoreArr.length;i++){
        if(scoreArr[i].pageId=="totalscore_"+staffId+"_"+examItemId){
            totalIndex = i;
            hasTotalValue = true;
            break;
        }

    }
    if(hasTotalValue){
        scoreArr.splice(totalIndex,1);
    }
    scoreArr.push(score);
    if(examItemId!=97&&examItemId!=98){
        $("#totalscore_"+staffId+"_"+examItemId).val("");
        var totalScoreValue = 0;
        for(var i=0;i<scoreArr.length;i++){
            if(scoreArr[i].examStaffId==staffId&&scoreArr[i].examItemId==examItemId){
                if(scoreArr[i].indexItemScore==''||scoreArr[i].indexItemPercent==''){
                    continue;
                }
                totalScoreValue += scoreArr[i].indexItemScore*scoreArr[i].indexItemPercent/100;
            }
        }
        totalScoreValue = totalScoreValue.toFixed(2);
        $("#totalscore_"+staffId+"_"+examItemId).val(totalScoreValue);//&nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red'>
        var totalScore = new Object();
        totalScore.pageId = "totalscore_"+staffId+"_"+examItemId;
        totalScore.examStation = staffInfo.stationId;
        totalScore.examStationName = staffInfo.station;
        totalScore.examName = staffInfo.staffName;
        totalScore.examDept = staffInfo.dept;
        totalScore.examCity = staffInfo.city;
        totalScore.examStaffId = staffInfo.staffId;
        totalScore.examDeptId = staffInfo.deptId;
        totalScore.examCityId = staffInfo.cityId;
        totalScore.indexItemId = 99999999;
        totalScore.indexItemName = "合计";
        totalScore.indexItemPercent = 0;
        totalScore.indexItemScore = totalScoreValue;
        totalScore.scoreType = "1";
        totalScore.examItemId = examItemId;
        totalScore.examItemName = examItemName;
        if(isExcellent(totalScoreValue)){
            totalScore.isExcellent = "0";//0:是 1：否
        }else{
            totalScore.isExcellent = "1";//0:是 1：否
        }
        if(isGood(totalScoreValue)){
            totalScore.isGood = "0";//0:是 1：否
        }else{
            totalScore.isGood = "1";//0:是 1：否
        }
        scoreArr.push(totalScore);
    }
}

function getStaffInfo(examStaffInfo,examStationId){
    var retExamStaffInfo ={};
    var count = 0;
    for(var i=0;i<examStaffInfo.length;i++){
        if(examStaffInfo[i].stationId==examStationId){
            retExamStaffInfo[count++] = examStaffInfo[i];
        }
        // else{
        //     break;
        // }

    }
    return retExamStaffInfo;
}
function getExamIndexRels(examIndexRels,examItemId){
    var retExamIndexRels ={};
    var count = 0;
    for(var i=0;i<examIndexRels.length;i++){
        if(examIndexRels[i].examItemId==examItemId){
            retExamIndexRels[count++] = examIndexRels[i];
        }
        // else{
        //     break;
        // }

    }
    return retExamIndexRels;
}
function checkSubmit(isCommit){
    debugger;
    var examItemInfo = getSession("examItemInfo");
    for(var i=0;i<examItemInfo.length;i++) {
        var examItemId = examItemInfo[i].examItemId;
        var examItemName = examItemInfo[i].examItemName;
        var msgTip = $("#"+"msgTip"+"_"+examItemId).html();
        if(msgTip!=undefined&&msgTip.indexOf("请调整")!=-1){
            showAlert("'"+examItemName+"'"+"考核项评选人数不符合设定，请调整！")
            return false;
        }
    }
    var isCheck = true;
    $('#examInfo .grade-b select').each(function(){
        var scoreIdName = $(this).attr("id");
        var examStaffInfo = getSession("staff_"+scoreIdName);
        var examIndexInfo = getSession("index_"+scoreIdName);
        if(isCommit=='1'&&($(this).find("option:selected").text()=="")){
            //showAlert("员工'"+examStaffInfo.staffName+"'的'"+examIndexInfo.indexItemName+"'未评分，请填写后再提交！");
            showAlert("您对'"+examStaffInfo.staffName+"'未完成评价，请填写后再提交！");
            $(this).focus();
            isCheck = false;
            return false;
        }else if($.trim($(this).val())>100){
            showAlert("您对员工'"+examStaffInfo.staffName+"'的'"+examIndexInfo.indexItemName+"'评分超过100分，请重新填写后再提交！");
            isCheck = false;
            return false;
        }
    });
    return isCheck;
}

function doLogout(){
    showConfirm("是否确认退出？",logout);
}

/**
 * 退出
 */
function logout(){
    $("#confirm").hide();
    $(".load").hide();
    //清除服务端token信息
    var url = "../../tower/staffinfo/logout?number="+Math.random();
    $.ajax({
        //与服务器交互
        url: url,
        type: 'post',
        dataType: 'json',
        data: {
            "token": window.sessionStorage.getItem("token")
        },
        complete: function (XMLHttpRequest, textStatus) {

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            showAlert("发生错误：" + errorThrown);
        },
        success: function (res) {
            //清除浏览器缓存
            clearSession();
            window.location.href = HOST + "html/tower/login.html";
        }
    });
}
function selSubmit(){
    $("#confirm").hide();
    $(".load").show();
    var url = "../../tower/selectioninfo/save?number="+Math.random();
    console.log(JSON.stringify(selArr));
    $.ajax({
        //与服务器交互
        url:url,
        type:'post',
        dataType: 'json',
        data: {"selListStr":JSON.stringify(selArr),
            "token":window.sessionStorage.getItem("token")
        },
        complete: function (XMLHttpRequest, textStatus) {
            $(".load").hide();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            $(".load").hide();
            showAlert("发生错误："+errorThrown);
        },
        success: function(res){
            $(".load").hide(function(){
                toHandle();
                if(res.msg==undefined){
                    showAlert("提交成功!");
                }else{
                    showAlert(res.msg);
                    if(res.msg.indexOf('未登录或已失效')!=-1){
                        window.location.href = HOST + "html/tower/login.html";
                    }
                }
            });

        }
    });
}
function submit(isCommit){
    debugger;
    $("#confirm").hide();
    if(isCommit=='1'){
        $("#loadingP").html("正在提交中...");
    }else{
        $("#loadingP").html("正在保存中...填写完毕后请勿忘记提交哦！");
    }
    $(".load").show();
    var url = "../../tower/examscoreinfo/save?number="+Math.random();
    console.log(JSON.stringify(scoreArr));
    $.ajax({
        //与服务器交互
        url:url,
        type:'post',
        dataType: 'json',
        data: {"scoreListStr":JSON.stringify(scoreArr),
            "token":window.sessionStorage.getItem("token"),
            "isCommit":isCommit
        },
        complete: function (XMLHttpRequest, textStatus) {
            $(".load").hide();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            $(".load").hide();
            showAlert("发生错误："+errorThrown);
        },
        success: function(res){
            $(".load").hide(function(){
                if(isCommit=='1'){
                    toHandle();
                }
                if(res.msg==undefined){
                    setSession("examScoreInfo",res.examScoreInfo);
                    if(isCommit=='1'){
                        showAlert("提交成功!");
                    }else{
                        showAlert("保存成功!填写完毕后请勿忘记提交哦！");
                    }
                    //showConfirm("提交成功，是否退出问卷考核系统？",logout);
                }else{
                    showAlert(res.msg);
                    if(res.msg.indexOf('未登录或已失效')!=-1){
                        window.location.href = HOST + "html/tower/login.html";
                    }
                }
            });

        }
    });
}
/**
 * 提交
 */
function doSubmit(isCommit){
    if(!checkSubmit(isCommit)) return;
    if(isCommit=="1"){
        showConfirm("提交后无法再次修改，是否确认提交您的问卷信息？",function(){
            submit(isCommit);
        });
    }else{
        submit(isCommit);
    }
}

/**
 * 民主选举投票
 */
function doSelSubmit(){
    var hasRepeat = false;
    var tmpSelStaffId = "";
    for(var i=0;i<selArr.length;i++) {
        if(selArr[i].selStaffId=='99999999'&&(selArr[i].selName==undefined||selArr[i].selName.trim()=='')){
            showAlert("推荐人选择其他时，推荐人'姓名'必须填写！");
            return;
        }
        if(selArr[i].selStaffId=='99999999'&&$("#cityStation_" + selArr[i].selStationId + "_" + selArr[i].selIndex).val().trim()==''){
            showAlert("推荐人选择其他时，'现单位及职务'必须填写！");
            return;
        }
        if(selArr[i].selStaffId!='00000000'&&(selArr[i].reason==undefined||selArr[i].reason.trim()=='')){
            showAlert("推荐人不为空时，'推荐理由'必须填写！");
            return;
        }
        if(selArr[i].selStaffId!='99999999'
            &&selArr[i].selStaffId!='00000000'
            &&selArr[i].selStaffId!=''
            &&selArr[i].selStaffId==tmpSelStaffId){
            showAlert("选择的推荐人选不能重复，请重新选择！");
            return;
        }
        tmpSelStaffId = selArr[i].selStaffId;
    }
    showConfirm("提交后无法再次修改，是否确认提交您的民主投票信息？",selSubmit);
}
function checkRCSubmit(evaluateType){
    var selItemName = "#responseSel";
    if(evaluateType=='1'){
        selItemName = "#clearSel";
    }
    for(var i=1;i<=10;i++) {
        if ($(selItemName + i).val() == '') {
            return false;
        }
    }
    return true;
}
function doResponseSubmit(){
    if(checkRCSubmit('0')==false){
        showAlert("您有选项未评分，请选择评分后再提交！");
        return false;
    }
    showConfirm("提交后无法再次修改，是否确认提交您的'党建工作责任制考核民主测评打分表'？",function(){
        evaluateSubmit('0');
    });
}
function doClearSubmit(){
    if(checkRCSubmit('1')==false){
        showAlert("您有选项未评分，请选择评分后再提交！");
        return false;
    }
    showConfirm("提交后无法再次修改，是否确认提交您的'党风廉政建设和反腐败工作责任制考核民主测评表'？",function(){
        evaluateSubmit('1');
    });
}
function evaluateSubmit(evaluateType){
    $("#confirm").hide();
    $(".load").show();
    var url = "../../tower/evaluateinfo/save?number="+Math.random();
    var resArr=[];
    console.log(JSON.stringify(resArr));
    var totalScore = 0;
    var validCount = 0;
    var selItemName = "#responseSel";
    if(evaluateType=='1'){
        selItemName = "#clearSel";
    }
    for(var i=1;i<=10;i++){
        var resItem = new Object();
        if($(selItemName+i).val()!=''){
            resItem.score = new Number($(selItemName+i).val());
        }else{
            resItem.score = "";
        }
        resItem.scoreType='0';
        resItem.evaluateType = evaluateType;
        resItem.evaContent = i;
        if(evaluateType=='0'&&resItem.score!=''){
            totalScore += new Number($(selItemName+i).val());
        }else if(evaluateType=='1'&&resItem.score!=''){//[合格票累计得分/（合格票数×3）]×20
            validCount++;
            totalScore += new Number($(selItemName+i).val());
        }
        resArr.push(resItem);
    }
    var resItem = new Object();
    if(evaluateType=='0'){
        resItem.score = (totalScore/3).toFixed(2);
    }else{
        resItem.score = ((totalScore/(validCount*3))*20).toFixed(2);
    }
    resItem.scoreType='1';
    resItem.evaluateType = evaluateType;
    resItem.evaContent ='合计';
    resArr.push(resItem);

    $.ajax({
        //与服务器交互
        url:url,
        type:'post',
        dataType: 'json',
        data: {"scoreListStr":JSON.stringify(resArr),
            "token":window.sessionStorage.getItem("token")
        },
        complete: function (XMLHttpRequest, textStatus) {
            $(".load").hide();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            $(".load").hide();
            showAlert("发生错误："+errorThrown);
        },
        success: function(res){
            $(".load").hide(function(){
                toHandle();
                if(res.msg==undefined){
                    showAlert("提交成功!");
                }else{
                    showAlert(res.msg);
                    if(res.msg.indexOf('未登录或已失效')!=-1){
                        window.location.href = HOST + "html/tower/login.html";
                    }
                }
            });

        }
    });
}