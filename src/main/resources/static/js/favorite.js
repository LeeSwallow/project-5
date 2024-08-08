function requestQueryFavorite(obj,subject_id){

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(function() {
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    });

    console.log(subject_id);

    $.ajax({
        url : "/subject/main/favorite/"+subject_id,
        type : "GET",
        dataType : "text",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
    })

        .done(function (json){
            toggleFavorite(obj);
        })

        .fail(function (xhr, status, errorThrown){
            alert("Ajax failed")
        })

}

function toggleFavorite(button) {
    if (button.value === "true") {
        // Change to not favorite
        button.classList.remove("bg-yellow-400", "text-white", "border", "border-yellow-400", "hover:bg-transparent", "hover:text-yellow-400");
        button.classList.add("bg-white", "text-yellow-400", "border", "border-yellow-400", "hover:bg-yellow-400", "hover:text-white");
        button.value = "false";
    } else {
        // Change to favorite
        button.classList.remove("bg-white", "text-yellow-400", "border", "border-yellow-400", "hover:bg-yellow-400", "hover:text-white");
        button.classList.add("bg-yellow-400", "text-white", "border", "border-yellow-400", "hover:bg-transparent", "hover:text-yellow-400");
        button.value = "true";
    }
}