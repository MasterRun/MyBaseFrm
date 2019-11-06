
function onEnvReady() {
	var btn = document.getElementById('btn');
	Zepto('#btn').on('tap',function(){
			ajax1();
	});
	
	//设置topbar文字  参数都是可选
    ajs.topbar.title({
        text: '来个title',
        color: '#E8E8E8',
        size: 22
    }, function (data) {
        if (data['result'].toString() == "1") {
            console.log("topbar.title success")
        } else {
            console.log("topbar.title failed")
        }
    });
}

function ajax1(){
	var content = document.getElementById('content');
	$.ajax({
		url: 'http://47.94.249.25:18888/campusmessager/confession/confessions',
		type: 'post',
		data: {
			pageIndex:1
		},
		success: function(result){
			content.innerHTML = JSON.stringify(result.data);
		},
		error: function(err){
			content.innerHTML = err;
		}
	});
}
function regHandler(){
	
}
