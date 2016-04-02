/**
 * Javarel framework setup
 */
window.J = $.extend(window.J || {}, {

    /**
     * Registered modules
     */
    modules: {},

    /**
     * Get or register new module
     */
    module: function (name, api) {
        if (typeof api === "undefined") {
            return this.modules[name];
        }

        this.modules[name] = api.apply(this);
    }

});