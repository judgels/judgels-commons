require(["jquery"], function( __jquery__ ) {
    $(document).ready(function() {
        $("input[type='radio']").change(function(e) {
            var that = $(this);
            var all = $("input[type='radio'][name='" + that.attr("name") + "']");
            that.prop('onclick',null).off('click');
            that.click(function(e) {
                var that = $(this);
                var checked = $("input[type='radio'][name='" + that.attr("name") + "']:checked");
                if (checked.hasClass('singleCheck')) {
                    checked.attr("checked", false);
                    checked.removeClass("singleCheck");
                }
            });
            all.removeClass("singleCheck");
            setTimeout(function() {
                that.addClass("singleCheck");
            }, 100);
        });

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