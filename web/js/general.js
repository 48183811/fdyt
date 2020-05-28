/**
 * Created by LY on 2019/6/17.
 */

/**
 * 随机生成UUID
 * @param {int}len   生成的UUID的长度
 * @param {int}radix 生成的UUID的进制
 * @returns {string}
 */
function uuid(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random()*16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }

    return uuid.join('');
}
/**
 * 返回object中的属性个数
 * @param {Object} obj
 * @returns {int}
 */
function countProperties (obj) {
    var count = 0;
    for (var property in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, property)) {
            count++;
        }
    }
    return count;
}

/**
 * 返回URL中name的值
 * @param key
 * @returns {*}
 */
function GetQueryString(key)
{
    var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)
        return  decodeURI(r[2]);
    return null;
}

/**
 * 固定object的某个属性值
 * @param obj
 * @param prop
 * @param val
 */
function frozenProperty(obj,prop,val){
    if(typeof val != "undefined" || val !=null){
        delete obj[prop];
        Object.defineProperty(obj,prop,{configurable:false,value:val});
    }
    else{
        var value = obj[prop];
        delete obj[prop];
        Object.defineProperty(obj,prop,{configurable:false,value:value});
    }
}