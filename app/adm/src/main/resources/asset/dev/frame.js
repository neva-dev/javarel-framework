/**
 * Frame open
 */
J.module("adm.dev.frame", function () {

    return {

        init: function (context) {

            $('#system-frame', context).each(function () {
                var $frame = $(this),
                    $window = $(window),
                    offset = $frame.offset(),
                    path = window.location.hash;

                // Load page using path specified in hash
                if (path) {
                    $frame.attr('src', path.substr(1));
                }

                // Dynamically recalculate frame size on window resizing
                function updatePageFrame() {
                    $frame.css({
                        display: 'block',
                        position: 'fixed',
                        top: offset.top,
                        width: '100%',
                        height: $(window).height() - offset.top
                    });
                }

                $window.on('resize', updatePageFrame);
                updatePageFrame();
            });

        }

    };

});