

$(function () {
  audioAutoPlay('bgmusic');
  function audioAutoPlay(id){
    var audio = document.getElementById(id);
    audio.play();
    document.addEventListener("WeixinJSBridgeReady", function () {
        audio.play();
    }, false);
    document.addEventListener('YixinJSBridgeReady', function() {
        audio.play();
    }, false);
  }
    $('.home').hide();
    $('.loading_box').hide();
    $('.wenan').hide();
    $('.photoImgs').show();
  $('.music').on('click',function(){
    if($(this).hasClass('on')){
      $('audio').get(0).pause();
      $(this).removeClass('on music-off');
    }else {
      $('audio').get(0).play();
      $(this).addClass('on music-off');
    }
  })
    /*for (var r = (new Image,
        [
            "./images/home-1.png",
            "./images/home-2.png",
            "./images/home-3.png",
            "./images/home-4.png",
            "./images/home-5.png",
            "./images/home-6.png",
            "./images/home-7.png",
            "./images/two.png",
            "./images/join-1.png",
            "./images/join-2.png"
        ]), t = r.length, i = function () {
        if (1 == --t) {
            jQuery(".loading_box").length > 0 && jQuery(".loading_box").css("display", "none"),parseInt(1e3 * Math.random());
            jQuery('.home').show();
        }
    }, n = 0; n < r.length; n++) {
        var l = "__preload_image_" + n,
            c = window[l] = document.createElement("img");
        c.onload = c.oncomplete = c.onerror = function () {
            i(),
                c.onload = c.oncomplete = c.onerror = null,
                window[l] = null
        },
            c.src = r[n]
    }*/
    $('.home-3').tap(function () {
        $('.home').hide();
        $('.wenan').show();
    })
    $('.wenanLink').tap(function () {
        $('.wenan').hide();
        $('.photoImgs').show();
    })
    $('.alert-c,.alert-bg').tap(function () {
        $('.alert').hide();
    })
    $('.close').tap(function () {
        $('.sharePhotos').hide();
    })
    'use strict';
    var $image = $('#image');
    var $download = $('#download');
    var $dataX = $('#dataX');
    var $dataY = $('#dataY');
    var $dataHeight = $('#dataHeight');
    var $dataWidth = $('#dataWidth');
    var $dataRotate = $('#dataRotate');
    var $dataScaleX = $('#dataScaleX');
    var $dataScaleY = $('#dataScaleY');
    var $dataWH = $('#dataWH');
    var options = {
        aspectRatio: 1 / 1,//设置裁切框的宽高比。默认情况下，裁剪框是自由比例。
        //preview: '.img-preview',
        crop: function (e) {
            $dataX.html(Math.round(e.x));
            $dataY.html(Math.round(e.y));
            $dataHeight.html(Math.round(e.height));
            $dataWidth.html(Math.round(e.width));
            $dataRotate.html(e.rotate);
            $dataScaleX.html(e.scaleX);
            $dataScaleY.html(e.scaleY);
            var _$dataWH = reductionTo(Math.round(e.width), Math.round(e.height));
            $dataWH.html(_$dataWH[0] + '/' + _$dataWH[1]);
        }
    };
    // 初始化函数
    $image.cropper(options);
    $image.cropper({
        built: function () {
        }
    });

    // 修改裁剪比例函数
    $('#ratio_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataRatio = $(this).attr('data-ratio');
        $image.cropper('destroy').cropper({'aspectRatio': dataRatio});
    });
    // 移动函数
    $('#move_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataMovex = parseInt($(this).attr('data-movex'));
        var dataMovey = parseInt($(this).attr('data-movey'));
        $image.cropper('move', dataMovex, dataMovey)
    });

    // 移动函数
    $('#move_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataMovex = parseInt($(this).attr('data-movex'));
        var dataMovey = parseInt($(this).attr('data-movey'));
        $image.cropper('move', dataMovex, dataMovey)
    });

    // Keyboard
    $(document.body).on('keydown', function (e) {
        if (!$image.data('cropper') || this.scrollTop > 300) {
            return;
        }
        switch (e.which) {
            case 37:
                e.preventDefault();
                $image.cropper('move', -1, 0);
                break;

            case 38:
                e.preventDefault();
                $image.cropper('move', 0, -1);
                break;

            case 39:
                e.preventDefault();
                $image.cropper('move', 1, 0);
                break;

            case 40:
                e.preventDefault();
                $image.cropper('move', 0, 1);
                break;
        }

    });
    // 放大缩小
    $('#zoom_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataZoom = $(this).attr('data-zoom');
        $image.cropper('zoom', dataZoom);
    });
    // 旋转
    $('#rotate_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataRotate = $(this).attr('data-rotate');
        $image.cropper('rotate', dataRotate);
    });
    // 翻转
    var scalexVal = 1;
    var scaleyVal = 1;
    $('#scale_container .btn').tap(function (event) {
        event.stopPropagation();
        var dataScale = $(this).attr('data-scale');
        if (dataScale == 'x') {
            scalexVal = -scalexVal;
            $image.cropper('scaleX', scalexVal);
        } else if (dataScale == 'y') {
            scaleyVal = -scaleyVal;
            $image.cropper('scaleY', scaleyVal);
        }
    });
    // enable()：使cropper可用。
    $('#enable').tap(function (event) {
        event.stopPropagation();
        $image.cropper('enable');
    });
    // disable()：冻结cropper。
    $('#disable').tap(function (event) {
        event.stopPropagation();
        $image.cropper('disable');
    });
    // reset()：重置剪裁区域的图片到初始状态。
    $('#reset1').tap(function (event) {
        event.stopPropagation();
        $image.cropper('crop');
        $image.cropper('destroy').cropper({'preview': '.img-preview'});
    });
    $('#reset2').tap(function (event) {
        event.stopPropagation();
        $image.cropper('reset');
        $image.cropper('destroy').cropper({'preview': '.img-preview'});
    });
    // clear()：清空剪裁区域。
    $('#clear').tap(function (event) {
        event.stopPropagation();
        $image.cropper('clear');
    });
    // destroy()：销毁剪裁函数。
    $('#destroy').tap(function (event) {
        event.stopPropagation();
        $image.cropper('destroy');
    });
    //上传图片
    $('#upload').change(function (event) {
        var files = this.files;
        if (files && files.length) {
            var file = files[0];
            if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(file.name)) {
                var uploadedImageURL = window.URL.createObjectURL(file);
                $image.cropper('destroy').attr('src', uploadedImageURL).cropper(options);
                $download.attr('download', uploadedImageURL);
                $('#upload').val('');
                $('.imgsClick').hide();
                $('.imgsSuccess').show();
            } else {
                $("#msgTip").html("请选择正确的图片格式！")
                $(".alert").show();
            }
        }
    });
    //修改图片地址
    var imgUrl = 'images/picture.jpg';
    $('#replace').tap(function (event) {
        event.stopPropagation();
        imgUrl = (imgUrl == 'images/picture_new.jpg' ? 'images/picture.jpg' : 'images/picture_new.jpg');
        $image.cropper('replace', imgUrl);
        $download.attr('download', imgUrl);
    });
    // 输出裁剪好的图片
    $('#getCroppedCanvas').tap(function (event) {
        event.stopPropagation();
        var imgurl = $image.cropper("getCroppedCanvas");
        $("#canvas").html(imgurl);
        $download.attr('href', imgurl.toDataURL());
        //$('.fixed-canvas').removeClass('hiddle');
    });
    //点击取消或者下载之后
    $('#modal_canvas_btn .btn').tap(function (event) {
        $('.fixed-canvas').addClass('hiddle');
    });
    // 获取数据
    $('#getData').tap(function (event) {
        event.stopPropagation();
        var getData = $image.cropper("getData")
        console.log(getData);
    });
    function drawAndShareImage(base64Data){
        var canvas = document.createElement("canvas");
        canvas.width = 700;
        canvas.height = 700;
        var context = canvas.getContext("2d");

        context.rect(0 , 0 , canvas.width , canvas.height);

        var myImage = new Image();
        myImage.src = base64Data;    //背景图片  你自己本地的图片或者在线图片

        myImage.onload = function(){
            context.drawImage(myImage , 0 , 0 , 700 , 700);
            var myImage2 = new Image();
            myImage2.src = "images/tianan.png";   //你自己本地的图片或者在线图片
            myImage2.onload = function(){
                context.drawImage(myImage2 , 0 , 0 , 700 , 700);
                var base64 = canvas.toDataURL("image/png");  //"image/png" 这里注意一下
                var img = document.getElementById('finalImage');
                img.setAttribute('src' , base64);
                $("#cropDiv").hide();
                $("#finalImgDiv").show();
                window.sessionStorage.setItem("genPhoto",base64);
                // $(".loadering").hide();
                $("#rechoose").show();
                $("#msgTip").html("头像制作成功，可长按保存图片！")
                $(".alert").show();
            }
        }
    }
    function drawAndShareImage2(base64Data){
        var canvas = document.createElement("canvas");
        canvas.width = 700;
        canvas.height = 1000;
        var context = canvas.getContext("2d");

        context.rect(0 , 0 , canvas.width , canvas.height);

        var myImage = new Image();
        myImage.src = "images/photos.png";    //背景图片  你自己本地的图片或者在线图片

        myImage.onload = function(){
            context.drawImage(myImage , 0 , 0 , 700 , 1000);
            var myImage2 = new Image();
            myImage2.src = base64Data;   //你自己本地的图片或者在线图片
            myImage2.onload = function(){
                context.drawImage(myImage2 , 0 , 0 , 700 , 700);
                var base64 = canvas.toDataURL("image/png");  //"image/png" 这里注意一下
                $('#sharePhoto').attr("src",base64);
                $("#sharePhotoDiv").show();
            }
        }
    }
    $('.alert-c,.alert-bg').tap(function () {
        $('.alert').hide();
    })
    $('.close').tap(function () {
        $('.sharePhotos').hide();
    })
    $("#rechoose").tap(function(event){
        $image.cropper('destroy');
        $('#sharePhoto').attr("src","");
        $("#finalImage").attr("src","");
        $("#finalImgDiv").hide();
        $("#image").attr("src","images/picsbt.jpg");
        $(".imgsClick").show();
        $("#cropDiv").hide();
        window.sessionStorage.removeItem("genPhoto");
    })
    //var httpUrl = "http://192.168.0.103:8080/ring_framework_war_exploded/api/upload/";
    var httpUrl = "https://admin.jixuxiang.com/ring/api/upload/";
    //提交裁剪好的图片到后台
    $('#submit').tap(function (event) {
        if($('#image').attr("src")==""||$('#image').attr("src")=="images/picsbt.jpg"){
            $("#msgTip").html("请先选择图片再制作头像！")
            $(".alert").show();
            return;
        }
        $("#msgTip").html("头像制作中...请稍候...")
        $(".alert").show();
        var imgData = $image.cropper("getCroppedCanvas").toDataURL('image/jpeg', 1);
        // console.log(imgData);
        drawAndShareImage(imgData);
        return;
        var fileSize = showSize(imgData);
        if(fileSize>3){
            alert("您的图片大于3M，无法提交！")
            return;
        }else{
            imgData = $image.cropper("getCroppedCanvas").toDataURL('image/jpeg', getCompressRate(3,fileSize));
        }
        $.ajax({
            url: httpUrl+"upload3",
            dataType: 'json',
            type: "POST",
            data: {"image": imgData},
            success: function (res) {
                $('.imgsSuccess').hide();
                $('.imgsClick').show();
                $('#image').attr("src",res.data);
                var index = res.data.lastIndexOf("/");
                var fileName = res.data.substring(index,res.data.length);
                window.sessionStorage.setItem("genPhoto",fileName);
                console.log('Upload success');
                alert("头像制作成功，您可点击下载图片！")
            },
            error: function () {
                console.log('Upload error');
            }
        });

    });
    $('#genPhoto').tap(function (event) {
        if(window.sessionStorage.getItem("genPhoto")==null){
            $("#msgTip").html("请先制作头像再分享！")
            $(".alert").show();
            return;
        }
        drawAndShareImage2(window.sessionStorage.getItem("genPhoto"));
        return;
        $.ajax({
            url: httpUrl+"genShareImg",
            dataType: 'json',
            type: "POST",
            data: {"image": window.sessionStorage.getItem("genPhoto")},
            success: function (res) {
                $('#sharePhoto').attr("src",res.data);
                $("#sharePhotoDiv").show();
                //window.location.href=res.data;

            },
            error: function () {
                console.log('Upload error');
            }
        });

    });
    function getCompressRate(allowMaxSize,fileSize){ //计算压缩比率，size单位为MB
        var compressRate = 1;
        if(fileSize/allowMaxSize > 4){
            compressRate = 0.5;
        } else if(fileSize/allowMaxSize >3){
            compressRate = 0.6;
        } else if(fileSize/allowMaxSize >2){
            compressRate = 0.7;
        } else if(fileSize > allowMaxSize){
            compressRate = 0.8;
        } else{
            compressRate = 0.9;
        }
        return compressRate;
    }

    function showSize(base64url) {
        //获取base64图片大小，返回MB数字
        var str = base64url.replace('data:image/jpeg;base64,', '');
        var equalIndex = str.indexOf('=');
        if(str.indexOf('=')>0) {
            str=str.substring(0, equalIndex);
        }
        var strLength=str.length;
        var fileLength=parseInt(strLength-(strLength/8)*2);
        // 由字节转换为MB
        var size = "";
        size = (fileLength/(1024 * 1024)).toFixed(2);
        var sizeStr = size + "";                        //转成字符串
        var index = sizeStr.indexOf(".");                    //获取小数点处的索引
        var dou = sizeStr.substr(index + 1 ,2)            //获取小数点后两位的值
        if(dou == "00"){                                //判断后两位是否为00，如果是则删除00
            return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2)
        }
        return parseInt(size);
    }


});

function destory() {
    $image.cropper('destroy').cropper(options);
}

//m,n为正整数的分子和分母
function reductionTo(m, n) {
    var arr = [];
    if (!isInteger(m) || !isInteger(n)) {
        // console.log('m和n必须为整数');
        arr[0] = 0;
        arr[1] = 0;
        return arr;
    } else if (m <= 0 || n <= 0) {
        // console.log('m和n必须大于0');
        arr[0] = 0;
        arr[1] = 0;
        return arr;
    }
    var a = m;
    var b = n;
    (a >= b) ? (a = m, b = n) : (a = n, b = m);
    if (m != 1 && n != 1) {
        for (var i = b; i >= 2; i--) {
            if (m % i == 0 && n % i == 0) {
                m = m / i;
                n = n / i;
            }
        }
    }
    arr[0] = m;
    arr[1] = n;
    return arr;
}

//判断一个数是否为整数
function isInteger(obj) {
    return obj % 1 === 0
}
