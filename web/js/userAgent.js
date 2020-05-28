/**
 * 判断浏览设备是手机还是电脑
 */
function userAgent() {
    let ua = navigator.userAgent,
        isWindowsPhone = /(?:Windows Phone)/.test(ua),
        isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone,
        isAndroid = /(?:Android)/.test(ua),
        isFireFox = /(?:Firefox)/.test(ua),
        isChrome = /(?:Chrome|CriOS)/.test(ua),
        isTablet = /(?:iPad|PlayBook)/.test(ua) || (isAndroid && !/(?:Mobile)/.test(ua)) || (isFireFox && /(?:Tablet)/.test(ua)),
        isPhone = /(?:iPhone)/.test(ua) && !isTablet,
        isPc = !isPhone && !isAndroid && !isSymbian;
    let agent={"device":"pc","browser":"IE"};
    if(isTablet){
        agent.device = "tablet";
    }
    if(isPhone){
        agent.device = "phone";
    }
    if(isPc){
        agent.device = "pc";
    }
    if(isFireFox){
        agent.browser = "FireFox";
    }
    if(isChrome){
        agent.browser = "Chrome"
    }
    return agent;

}