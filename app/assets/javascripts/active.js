require(["jquery", "bootstrap", "bootstrap-select"], function( __jquery__ ) {
    $(document).ready(function() {
        $($(".breadcrumb-link").get().reverse()).each(function() {
            var href = $(this).attr("href");
            $("a[href='" + href +"']").each(function() {
                var parent = $(this).parent();
                if (parent.siblings(".active").size() === 0) {
                    parent.addClass("active");
                    if (parent.is("li")) {
                        parent.parentsUntil("body", "li").addClass("active");
                    }
                }
            });
        });
    });
});