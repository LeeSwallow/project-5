taskList = {'tasks' : []};

document.addEventListener('DOMContentLoaded', function() {
    const listExpiredInputs = document.querySelectorAll('.listExpired');

    listExpiredInputs.forEach(input => {
        const originalValue = input.value;

        input.addEventListener('change', function() {
            modifyButton = document.getElementById('modifyButton');
            modifyButton.classList.remove('hidden');
            modfiyButton.classList.remove("opacity-0");
            modfiyButton.classList.add("opacity-100");

            const hasChild = input.closest('li').getAttribute('data-has-child') === 'true';
            const value = parseInt(input.value, 10);
            if (hasChild) {
                alert('세부목표가 있는 목표는 기한을 설정할 수 없습니다.');
                input.value = originalValue;

            } else if (isNaN(value)){
                alert('숫자를 입력해주세요.');
                input.value = originalValue;

            } else if (value < 1 || value > 365) {
                alert('1일부터 365일 사이의 숫자를 입력해주세요.');
                input.value = originalValue;
            }
        });
    });
});

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


function onModify() {
    const taskListItems = document.querySelectorAll('#taskList li');
    const taskData = {"tasks": []};

    taskListItems.forEach((item, index) => {
        const listId = item.querySelector('.listId').value;
        const expiredDateInput = item.querySelector('.listExpired');
        const expiredDate = expiredDateInput ? expiredDateInput.value : null;
        const priority = index + 1; // Assign priority based on the order

        taskData['tasks'].push({
            listId: listId,
            expiredDate: expiredDate,
            priority: priority
        });
    });

    const modifyForm = document.getElementById('modifyForm');
    modifyForm.elements['body'].value = JSON.stringify(taskData);
    modifyForm.submit();
}