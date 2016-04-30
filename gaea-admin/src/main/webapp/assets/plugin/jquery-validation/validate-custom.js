$.validator.setDefaults({
    errorElement: 'div',
    errorPlacement: function(error, element) {
        error.addClass('ui pointing red basic label');
        if (element.parent().hasClass('dropdown')) {
            element.parent().after(error);
        } else {
            element.after(error);
        }
    }
})

// 手机号码格式验证
jQuery.validator.addMethod("isMobile", function(value, element) {
    return !value || (/^(1)\d{10}$/.test(value));
}, "手机号格式错误！");


jQuery.validator.addMethod("checkStaffIdUnique", function(value, element, param) {
    var flag = false;
    $.ajax({
        url: '/user/queryByStaffId.json?staffId=' + value,
        async: false,
        dataType: 'json',
        success:function(res) {
            if (res.status == 200) {
                var data = res.data;
                if (!param && data && data.id) {
                    return;
                }
                if (param && data && data.id != param) {
                    return;
                }
                flag = true;
            }
        }
    });
    return flag;
}, "员工工号已存在！");

jQuery.validator.addMethod("checkAppNameUnique", function(value, element, param) {
    var flag = false;
    $.ajax({
        url: '/app/queryByName.json?name=' + value,
        async: false,
        dataType: 'json',
        success:function(res) {
            if (res.status == 200) {
                var data = res.data;
                if (!param && data && data.id) {
                    return;
                }
                if (param && data && data.id != param) {
                    return;
                }
                flag = true;
            }
        }
    });
    return flag;
}, "应用名称已存在！");
