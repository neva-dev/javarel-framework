/**
 * Frame open
 */
J.module("adm.dev.frame", function () {

    return {

        init: function (context) {

            $('#system-frame', context).each(function () {
                var $frame = $(this),
                    $window = $(window),
                    offset = $frame.offset();

                // Load page using path specified in hash
                function updateSource() {
                    var path = window.location.hash;
                    if (path) {
                        $frame.attr('src', path.substr(1));
                    }
                }

                $window.on('hashchange', updateSource);
                updateSource();

                // Dynamically recalculate frame size on window resizing
                function updateDimensions() {
                    $frame.css({
                        display: 'block',
                        position: 'fixed',
                        top: offset.top,
                        width: '100%',
                        height: $(window).height() - offset.top
                    });
                }

                $window.on('resize', updateDimensions);
                updateDimensions();
            });

        }

    };

});