(function () {
    function IsPC() {
        var userAgentInfo = navigator.userAgent;
        var Agents = ["Android", "iPhone",
            "SymbianOS", "Windows Phone",
            "iPad", "iPod"];
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    function setPcClass(){
        let pcshow = IsPC();
        if(pcshow){
            document.getElementsByTagName('body')[0].classList.add("ispc");
        }else{
            document.getElementsByTagName('body')[0].classList.remove("ispc");
        }
    }
    setPcClass();
    window.onresize=function(){
        setPcClass();
    }


}());
