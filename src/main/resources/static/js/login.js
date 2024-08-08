function submitLoginForm(form) {
    form.username.value = form.username.value.trim();

    if (form.username.value.length === 0) {
        alert("아이디를 입력해주세요.");
        form.username.focus();
        return false;
    }

    if (form.password.value.length === 0) {
        alert("비밀번호를 입력해주세요.");
        form.password.focus();
        return false;
    }

    form.submit();
}