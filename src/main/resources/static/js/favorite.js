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
            if (json=="true"){
                $(obj).addClass("favorite_on");
            } else {
                $(obj).removeClass('favorite_on');
            }

        })

        .fail(function (xhr, status, errorThrown){
            alert("Ajax failed")
        })

}