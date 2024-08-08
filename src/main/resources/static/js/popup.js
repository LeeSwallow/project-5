const savedSubject = {'subjectNo': []};

function showDivideForm() {
    const divideForm = document.getElementById('divideForm');
    divideForm.classList.remove('hidden');
}

function hideDivideForm() {
    window.location.reload();

}

function showLoading() {
    document.getElementById('loadingForm').classList.remove('hidden');
}

function hideLoading() {
    document.getElementById('loadingForm').classList.add('hidden');
}

function showSelectForm() {
    const form = document.getElementById('selectForm');
    form.classList.remove('hidden');
    form.classList.remove('opacity-0');
    form.classList.add('opacity-100');
}

function hideSelectForm() {
    const form = document.getElementById('selectForm');
    form.classList.remove('opacity-100');
    form.classList.add('opacity-0');
    setTimeout(() => form.classList.add('hidden'), 500); // Wait for the fade-out animation to complete
}

function requestQuery(id) {
    showSelectForm();
    console.log(id);
    document.getElementById('requestForm').elements['id'].value = id;
}

function confirmYes() {
    hideSelectForm();
    showLoading();
    setTimeout(() => {
        document.getElementById('requestForm').submit();
    }, 500); // Wait for the fade-out animation to complete
}

function onSelectSubject(check) {
    const list = check.parentElement.parentElement;
    const subjectNo = check.nextElementSibling.value;
    if (check.checked) {
        for (const no of savedSubject.subjectNo) {
            if (no === subjectNo){
                list.classList.remove('bg-gray-100');
                list.classList.add('bg-primary');
                return;
            }
        }
        savedSubject.subjectNo.push(subjectNo);
        list.classList.remove('bg-gray-100');
        list.classList.add('bg-primary');
    } else {
        savedSubject.subjectNo = savedSubject.subjectNo.filter(no => no !== subjectNo);
        list.classList.remove('bg-primary');
        list.classList.add('bg-gray-100');
    }
    console.log(savedSubject);
}

function onSave() {
    document.getElementById('saveForm').elements['body'].value = JSON.stringify(savedSubject);
    document.getElementById('saveForm').submit();
}