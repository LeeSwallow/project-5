document.addEventListener('DOMContentLoaded', function () {
    var el = document.getElementById('taskList');
    var sortable = Sortable.create(el, {
        animation: 150,
        onEnd: function (evt) {
            var order = sortable.toArray();
            console.log('New order:', order);
            // You can send the new order to the server or handle it as needed
        }
    });
});