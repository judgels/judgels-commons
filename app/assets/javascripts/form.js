require(["jquery"], function( __jquery__ ) {
    $(document).ready(function() {
        $("form").submit(function(e) {
            var $form = $(this);

            if ($form.data('submitted') === true) {
                // Previously submitted - don't submit again
                e.preventDefault();
            } else {
                // Mark it so that the next submit can be ignored
                $form.data('submitted', true);
            }

            return this;
        });
    });
});