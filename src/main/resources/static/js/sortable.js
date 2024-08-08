$(function() {
    const taskList = $("#taskList");

    taskList.sortable({
        update: function(event, ui) {
            modfiyButton = document.getElementById("modifyButton");
            modifyButton.classList.remove("hidden");
            modfiyButton.classList.remove("opacity-0");
            modfiyButton.classList.add("opacity-100");
            printOrder();
        }
    });

    function printOrder() {
        var taskIds = [];
        taskList.find('li').each(function() {
            taskIds.push($(this).data("id"));
        });
        console.log(taskIds);
    }


});