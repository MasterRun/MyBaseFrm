function onEnvReady() {
    mui.init();
    
    console.log('Youyongma');

    Zepto('li').on('tap', function(){
        this.classList.add('active');
    });
}
function regHandler(){}
