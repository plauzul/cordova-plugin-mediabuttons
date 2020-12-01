var exec = require('cordova/exec');

exports.startActionMedia = function (arg0, success, error) {
    exec(success, error, 'MediaButtons', 'startActionMedia', [arg0]);
};

exports.stopActionMedia = function (arg0, success, error) {
    exec(success, error, 'MediaButtons', 'stopActionMedia', [arg0]);
};
