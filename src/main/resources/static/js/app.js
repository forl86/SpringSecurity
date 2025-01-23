const closeModalBtn = document.getElementById('closeModalBtn');
const editModalWindow = document.getElementById('editWindow');
const deleteModalWindow = document.getElementById('deleteWindow')
const closeBtnUpdate = document.getElementById('BtnCloseUpdate')
const editBtnUpdate = document.getElementById('btnEditUser')
const closeBtnDelete = document.getElementById('closeDeleteWindowBtn')
const deleteBtnDelete = document.getElementById('btnDeleteUser')
const deleteBtnClose = document.getElementById('btnCloseDelete')
function closeModalEdit() {
    editModalWindow.style.display = 'none'
}
function closeModalDelete() {
    deleteModalWindow.style.display = 'none'
}
async function deleteModalDeleteWindow() {
    let inputId = document.getElementById('userIdDelete')
    let inputName = document.getElementById('userNameDelete')
    let inputLastName = document.getElementById('userLastNameDelete')
    let inputEmail = document.getElementById('userEmailDelete')
    let inputAge = document.getElementById('userAgeDelete')
    let inputRole = document.getElementById('userRolesDelete')
    if ( inputRole.value === '') {
        inputRole.value = 'ROLE_USER'
    }
    let roleResponse = await fetch('/api/role/name/'+inputRole.value )
    if (!roleResponse.ok) {
        throw new Error("No role in response!")
    }
    let dataRole = await roleResponse.json()
    let user = {
        id: Number(inputId.value),
        username: inputName.value,
        lastname: inputLastName.value,
        age: Number(inputAge.value),
        email: inputEmail.value,
        roles: dataRole
    }
    console.log(user)
    try {
        let response = await fetch('/api/delete/'+inputId.value, {
            method: 'DELETE',
        })
        if (response.ok) {
            console.log('Пользователь успешно удален')
        } else {
            console.error('Ошибка при удалении пользоваетеля:', response.statusText)
        }
    } catch (error) {
        console.log('Ошибка:', error)
    }
    closeModalDelete()
    var usersTable = document.getElementById("usersTable").getElementsByTagName('tbody')[0]
    const rowsNumber = usersTable.rows.length
    while (usersTable.rows.length > 0) {
        usersTable.deleteRow(usersTable.rows.length-1)
    }
    fetchUsers()
}
async function editModalEditWindow() {
    let inputId = document.getElementById('userIdUpdate')
    let inputName = document.getElementById('userNameUpdate')
    let inputLastName = document.getElementById('userLastNameUpdate')
    let inputEmail = document.getElementById('userEmailUpdate')
    let inputAge = document.getElementById('userAgeUpdate')
    let inputRole = document.getElementById('userRolesUpdate')
    if ( inputRole.value === '') {
            inputRole.value = 'ROLE_USER'
    }
    let roleResponse = await fetch('/api/role/name/'+inputRole.value )
    if (!roleResponse.ok) {
        throw new Error("No role in response!")
    }
    let dataRole = await roleResponse.json()
    let user = {
        id: Number(inputId.value),
        username: inputName.value,
        lastname: inputLastName.value,
        age: Number(inputAge.value),
        email: inputEmail.value,
        roles: dataRole
    }
    console.log(user)
    let response = await fetch('/api/user/'+inputId.value, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({id: Number(inputId.value), username: inputName.value, lastname: inputLastName.value,
        age: Number(inputAge.value), email: inputEmail.value, roles: [{id: dataRole.id, roleName: dataRole.roleName, users: null}], roleIds: 1})
    }).then((response) => response.json())
        .then((json) => console.log(json))
    closeModalEdit()
    var usersTable = document.getElementById("usersTable").getElementsByTagName('tbody')[0]
    const rowsNumber = usersTable.rows.length
    while (usersTable.rows.length > 0) {
        usersTable.deleteRow(usersTable.rows.length-1)
    }
    fetchUsers()
}
editBtnUpdate.addEventListener('click', editModalEditWindow)
closeModalBtn.addEventListener('click', closeModalEdit)
closeBtnUpdate.addEventListener('click', closeModalEdit)
deleteBtnClose.addEventListener('click', closeModalDelete)
deleteBtnDelete.addEventListener('click', deleteModalDeleteWindow)
closeBtnDelete.addEventListener('click', closeModalDelete)
var btnA = document.getElementById("btnAdmin");
var btnU = document.getElementById("btnUser");
var tableBody = document.getElementById("usersTable").getElementsByTagName('tbody')[0]
function addRow(id, name, lastname, age, email, role) {
    const newRow = tableBody.insertRow()
    const cell1 = newRow.insertCell(0)
    const cell2 = newRow.insertCell(1)
    const cell3 = newRow.insertCell(2)
    const cell4 = newRow.insertCell(3)
    const cell5 = newRow.insertCell(4)
    const cell6 = newRow.insertCell(5)
    cell1.textContent = id
    cell2.textContent = name
    cell3.textContent = lastname
    cell4.textContent = age
    cell5.textContent = email
    cell6.textContent = role
    let buttonEdit = document.createElement("button")
    let tdEdit = document.createElement("td")
    buttonEdit.innerText = "Edit"
    buttonEdit.className = "btn btn-sm btn-primary"
    buttonEdit.style.backgroundColor = 'darkcyan'
    buttonEdit.style.color = 'white'
    // buttonEdit.innerHTML = 'data-bs-toggle="modal"'+
    //             ' data-bs-target="#exampleModal" th:data-user-id="${user.getId()}"'+
    //             ' th:data-user-name="${user.getUsername()}" th:data-user-lastname="${user.getLastname()}"'+
    //             ' th:data-user-age="${user.getAge()}" th:data-user-email="${user.getEmail()}"></>'
    buttonEdit.addEventListener('click', async function () {
        editModalWindow.style.display = 'block';
        try {
            let url = 'http://localhost:8081/api/user/'
            let response = await fetch(url + id)
            if (!response.ok) {
                throw new Error('Ошибка HTTP: ${response.status} ${response.statusText}');
            }
            let data = await response.json()
            console.log('Данные:', data);
            let inputId = document.getElementById("userIdUpdate")
            let inputName = document.getElementById("userNameUpdate")
            let inputLastName = document.getElementById("userLastNameUpdate")
            let inputAge = document.getElementById("userAgeUpdate")
            let inputEmail = document.getElementById("userEmailUpdate")
            let inputRoles = document.getElementById("userRolesUpdate")
            if (inputId) {
                inputId.value = data.id;
            } else {
                console.log('Элемент с id "userIdUpdate" не найден');
            }
            if (inputName) {
                inputName.value = data.username
            } else {
                console.log('Эдемент с id "userNameUpdate" не найден')
            }
            if (inputLastName) {
                inputLastName.value = data.lastname
            } else {
                console.log('Элемент с id "userLastNameUpdate" не найден')
            }
            if (inputAge) {
                inputAge.value = data.age
            }
            if (inputEmail) {
                inputEmail.value = data.email;
            }
            if (inputRoles) {
                inputRoles.innerHTML = '';
                const response = await fetch('http://localhost:8081/api/roles')
                const dataRoles = await response.json()
                console.log(dataRoles)
                dataRoles.forEach((role) => {
                    console.log(role.roleName)
                    const opt = new Option(role.roleName, role.roleName)
                    inputRoles.add(opt)
                    console.log('добавляемая опция', opt)
                })
            }
            console.log('ID пользователя:', data.id)
            console.log('Имя пользователя:', data.username)
            console.log('Фамилия пользователя:', data.lastname)
            console.log('Возраст пользователя', data.age)
            console.log('Почта пользователя', data.email)
        } catch (error) {
            console.error('Ошибка:', error)
            alert('Произошла ошибка: ' + error.message)
        }
    })
    tdEdit.append(buttonEdit)
    newRow.append(tdEdit)
    let buttonDelete = document.createElement("button")
    let tdDelete = document.createElement("td")
    buttonDelete.innerText = 'Delete'
    buttonDelete.className = 'btn btn-sm btn-primary'
    buttonDelete.style.backgroundColor = 'red'
    buttonDelete.style.color = 'white'
    buttonDelete.addEventListener('click', async function() {
        deleteModalWindow.style.display = 'block'
        try {
            let url = 'http://localhost:8081/api/user/'
            let response = await fetch(url + id)
            if (!response.ok) {
                throw new Error('Ошибка HTTP: ${response.status} ${response.statusText}');
            }
            let data = await response.json()
            console.log('Данные:', data);
            let inputId = document.getElementById("userIdDelete")
            let inputName = document.getElementById("userNameDelete")
            let inputLastName = document.getElementById("userLastNameDelete")
            let inputAge = document.getElementById("userAgeDelete")
            let inputEmail = document.getElementById("userEmailDelete")
            let inputRoles = document.getElementById("userRolesDelete")
            if (inputId) {
                inputId.value = data.id;
            } else {
                console.log('Элемент с id "userIdUpdate" не найден');
            }
            if (inputName) {
                inputName.value = data.username
            } else {
                console.log('Эдемент с id "userNameUpdate" не найден')
            }
            if (inputLastName) {
                inputLastName.value = data.lastname
            } else {
                console.log('Элемент с id "userLastNameUpdate" не найден')
            }
            if (inputAge) {
                inputAge.value = data.age
            }
            if (inputEmail) {
                inputEmail.value = data.email;
            }
            if (inputRoles) {
                inputRoles.innerHTML = '';
                const response = await fetch('http://localhost:8081/api/roles')
                const dataRoles = await response.json()
                console.log(dataRoles)
                dataRoles.forEach((role) => {
                    console.log(role.roleName)
                    const opt = new Option(role.roleName, role.roleName)
                    inputRoles.add(opt)
                    console.log('добавляемая опция', opt)
                })
            }
            console.log('ID пользователя:', data.id)
            console.log('Имя пользователя:', data.username)
            console.log('Фамилия пользователя:', data.lastname)
            console.log('Возраст пользователя', data.age)
            console.log('Почта пользователя', data.email)
        } catch (error) {
            console.error('Ошибка:', error)
            alert('Произошла ошибка: ' + error.message)
        }
    })
    tdDelete.append(buttonDelete)
    newRow.append(tdDelete)
}
async function fetchUsers() {
    try {
        let url = 'http://localhost:8081/api/users'
        let response = await fetch(url)
        let data = await response.json()
        if (data.length + 1 !== document.getElementById('usersTable').rows.length) {
            data.forEach(item => {
                addRow(item.id, item.username, item.lastname, item.age, item.email, item.roles[0].roleName)
            })
        }
    } catch (error) {
        console.log('Ошибка в fetchUsers(): ', error)
    }
}
async function getCurrentUser() {
    try {
        let response = await fetch('http://localhost:8081/api/current')
        let currentUser = await response.json()
        $('#currentUserEmail').text(currentUser.email)
    } catch (error) {
        alert(error)
    }
}
btnA.onclick = function() {
    btnA.style.background = "blue";
    btnU.style.background = "white";
    btnU.style.outlineColor = "white";
    btnU.style.color = "blue";
    btnA.style.color = "white";
    var usersTable = document.getElementById("usersTable").getElementsByTagName('tbody')[0]
    const rowsNumber = usersTable.rows.length
    while (usersTable.rows.length > 0) {
        usersTable.deleteRow(usersTable.rows.length-1)
    }
    fetchUsers()//потом надо перенести
}
btnU.onclick = function() {
    btnU.style.background = "blue";
    btnA.style.background = "white";
    btnA.style.outlineColor = "white";
    btnU.style.color = "white";
    btnA.style.color = "blue";
    getCurrentUser()//потом надо перенести
}
// document.getElementById('exampleModal').addEventListener('show.bs.modal', function (event) {
//     const button = event.relatedTarget;
//     const userId = button.getAttribute('data-user-id');
//     const userName = button.getAttribute('data-user-name');
//     const userLastName = button.getAttribute('data-user-lastname');
//     const userAge = button.getAttribute('data-user-age');
//     const userEmail = button.getAttribute('data-user-email');
//     document.getElementById('userId').value = userId;
//     document.getElementById('userName').value = userName;
//     document.getElementById('userLastName').value = userLastName;
//     document.getElementById('userAge').value = userAge;
//     document.getElementById('userEmail').value = userEmail;
// });
// document.getElementById('deleteModalWindow').addEventListener('show.bs.modal', function (event) {
//     const button = event.relatedTarget;
//     const userId = button.getAttribute('data-user-id');
//     const userName = button.getAttribute('data-user-name');
//     const userLastName = button.getAttribute('data-user-lastname');
//     const userAge = button.getAttribute('data-user-age');
//     const userEmail = button.getAttribute('data-user-email');
//     document.getElementById('deleteUserId').value = userId;
//     document.getElementById('deleteUserName').value = userName;
//     document.getElementById('deleteLastName').value = userLastName;
//     document.getElementById('deleteAge').value = userAge;
//     document.getElementById('deleteEmail').value = userEmail;
// });
// document.getElementById('editUserForm').addEventListener('submit', function(event) {
//     const passwordField = document.getElementById('password');
//     if (passwordField.value === '') {
//         passwordField.disabled = true;
//     }
// });