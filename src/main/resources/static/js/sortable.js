$(function() {
    const taskList = $("#taskList");

    taskList.sortable({
        update: function(event, ui) {
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