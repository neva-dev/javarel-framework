/**
 * Dispatch events for other modules.
 */
J.module("core.layout.event-dispatcher", function () {

    function eachModule(methodName, context) {
        _.forOwn(J.modules, function (module) {
            var method = module[methodName];

            if (_.isFunction(method)) {
                method.apply(module, [context]);
            }
        });
    }

    var api = {
        bootAll: function () {
            eachModule('boot');
        },

        initAll: function (context) {
            eachModule('init', context);
        },

        loadAll: function (context) {
            eachModule('load', context);
        }
    };

    $(document).ready(function () {
        api.bootAll(this);
        api.initAll(this);
    });

    $(window).load(function () {
        api.loadAll(this);
    });

    return api;
});