taskList = {'tasks' : []};


function toggleTask(checkbox) {
    var span = checkbox.nextElementSibling;
    var fullList = checkbox.parentElement.parentElement.parentElement;
    var id = fullList.querySelector('input[type="hidden"]').value;

    if (checkbox.checked) {
        span.classList.add('line-through');
        fullList.classList.remove('bg-gray-100');
        fullList.classList.add('bg-success');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                return;
            }
        }
        taskList.tasks.push(id);


    } else {
        span.classList.remove('line-through');
        fullList.classList.add('bg-gray-100');
        fullList.classList.remove('bg-success');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                taskList.tasks.splice(i, 1);
                return;
            }
        }
    }
    console.log(taskList);
}

function toggleDone(checkbox) {
    var span = checkbox.nextElementSibling;
    var fullList = checkbox.parentElement.parentElement.parentElement;
    var id = fullList.querySelector('input[type="hidden"]').value;

    if (checkbox.checked) {
        span.classList.add('line-through');
        fullList.classList.remove('bg-gray-100');
        fullList.classList.add('bg-error');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                return;
            }
        }
        taskList.tasks.push(id);


    } else {
        span.classList.remove('line-through');
        fullList.classList.add('bg-gray-100');
        fullList.classList.remove('bg-error');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                taskList.tasks.splice(i, 1);
                return;
            }
        }
    }
    console.log(taskList);
}





function addTask() {
    var inputForm = document.getElementById('inputForm');
    var task = document.getElementById('taskInput');

    if (task.value.trim() === '') {
        alert('목표를 입력해주세요.');
        task.value = '';
        return;
    } else if (task.value.length > 50) {
        alert('목표는 50자 이내로 입력해주세요.');
        task.value = '';
        return;
    } else if (task.value.length < 5) {
        alert('목표는 5자 이상으로 입력해주세요.');
        task.value = '';
        return;
    } else {
        inputForm.submit();
    }
}

function onDelete() {
    var delForm = document.getElementById('delForm');
    delForm.elements['body'].value = JSON.stringify(taskList);
    delForm.submit();
}

function onDone() {
    var doneForm = document.getElementById('doneForm');
    doneForm.elements['body'].value = JSON.stringify(taskList);
    doneForm.submit();
}


function onChangeSort() {
    var sortForm = document.getElementById('sortForm');
    sortForm.submit();
}

function toggleFavorite(star) {
    star.classList.toggle('text-yellow-500');


}