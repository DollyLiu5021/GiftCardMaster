/*price range*/

if ($.fn.slider) {
    $('#sl2').slider();
}

var RGBChange = function () {
    $('#RGB').css('background', 'rgb(' + r.getValue() + ',' + g.getValue() + ',' + b.getValue() + ')')
};

/*scroll to top*/

$(document).ready(function () {
    $(function () {
        $.scrollUp({
            scrollName: 'scrollUp', // Element ID
            scrollDistance: 300, // Distance from top/bottom before showing element (px)
            scrollFrom: 'top', // 'top' or 'bottom'
            scrollSpeed: 300, // Speed back to top (ms)
            easingType: 'linear', // Scroll to top easing (see http://easings.net/)
            animation: 'fade', // Fade, slide, none
            animationSpeed: 200, // Animation in speed (ms)
            scrollTrigger: false, // Set a custom triggering element. Can be an HTML string or jQuery object
            //scrollTarget: false, // Set a custom target element for scrolling to the top
            scrollText: '<i class="fa fa-angle-up"></i>', // Text for element, can contain HTML
            scrollTitle: false, // Set a custom <a> title if required.
            scrollImg: false, // Set true to use image
            activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
            zIndex: 2147483647 // Z-Index for the overlay
        });
    });
});

// --- Login Page Functions ---
/**
 * @return {boolean}
 */
function CheckRegisterFormData() {
    // Check username
    var username = document.getElementById("registerUsername").value;
    if (username.length > 20 || username.length < 5) {
        alert("Username should longer than 5 and shorter than 20");
        return false;
    } else if (username.indexOf("#") !== -1) {
        alert("Username contains illegal character: #");
        return false;
    } else if (/.*[\u4e00-\u9fa5]+.*$/.test(username)) {
        alert("Username contains Chinese");
        return false;
    }

    // Check email
    var email = document.getElementById("registerEmail").value;
    if (email.length === 0) {
        alert("Email address cannot be empty");
        return false;
    }
    if (email.length > 45) {
        alert("Email address too long");
        return false;
    } else if (email.indexOf("#") !== -1) {
        alert("Email contains illegal character: #");
        return false;
    }

    var password = document.getElementById("registerPassword").value;
    var pattern = /^(?![A-z0-9]+$)(?=.[^%&',;=?$\x22])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$/;
    if (pattern.test(password) === false) {
        alert("Password should contain uppercase, lowercase, number and special character");
        return false;
    } else if (password.indexOf("#") !== -1) {
        alert("Password contains illegal character: #");
        return false;
    } else if (password.length < 5 || password.length > 20) {
        alert("Password should longer than 5 and shorter than 20");
        return false;
    }
    return true;
}

/**
 * @return {boolean}
 */
function CheckLoginFormData() {
    // Check username
    var username = document.getElementById("loginUsername").value;
    var password = document.getElementById("loginPassword").value;
    if (username.length === 0 || password.length === 0) {
        alert("Incomplete login information");
        return false;
    }
    if (username.indexOf("#") !== -1 || password.indexOf("#") !== -1) {
        alert("Input contains illegal character: #");
        return false;
    }
    return true;
}