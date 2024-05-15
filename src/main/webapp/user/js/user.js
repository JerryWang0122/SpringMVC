
const $ = (id) => document.getElementById(id);

// 定義一個非同步函數來加載 html 內容
const loadHTML = async (url, containerId) => {
    const fullUrl = 'http://localhost:8080/SpringMVC_war_exploded' + url;
    try {
        const response = await fetch(fullUrl);  // 等待 fetch 請求完成
        const data = await response.text();     // 等待回應本文內容
        $(containerId).innerHTML = data;
    } catch (e) {
        console.error(e)
    }
};

// 渲染 User 資料的配置
const renderUser = ({ id, name, gender, age, birth, education, interestNames, interests, resume }) => `
<tr>
    <td>${id}</td>
    <td>${name}</td>
    <td>${gender.name}</td>
    <td>${age}</td>
    <td>${birth}</td>
    <td>${education.name}</td>
    <td>${interestNames}</td>
    <td>${resume}</td>
    <td>
        <span class="pure-button button-update update-user-button" data-id="${id}">Edit</span>
    </td>
    <td>
        <span class="pure-button button-delete delete-user-button" data-id="${id}">Delete</span>
    </td>
</tr>
`;

// 資料渲染
const fetchAndRenderData = async(url, containerId, renderFn) => {
    const fullUrl = 'http://localhost:8080/SpringMVC_war_exploded' + url;
    try {
        const response = await fetch(fullUrl);  // 等待 fetch 請求完成
        const { state, message, data } = await response.json();     // 等待回應本文內容
        console.log(state, message, data)

        // data 是一個陣列
        // console.log(renderFn(data[0]))
        // $(containerId).innerHTML = renderFn(data[0]) + '' + renderFn(data[1]) + '' + renderFn(data[2]);

        /*
        if (Arrays.isArray(data)) {
            $(containerId).innerHTML = data.map(renderFn).join('');
        } else {
            $(containerId).innerHTML = renderFn(data);
        }
        */
        $(containerId).innerHTML = Array.isArray(data) ? data.map(renderFn).join('') : renderFn(data);

    } catch (e) {
        console.error(e)
    }
};

const handleUpdateUser = (id) => {
    console.log('按下修改' + id)
};

const handleDeleteUser = async (id) => {
    console.log('按下刪除' + id);
    // 確認是否要刪除 ?
    /*
    if (!confirm('是否要刪除')){
        return;
    }
    */
    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        text: '刪除後將無法恢復',
        color: '#fff',
        background: '#005e75',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '是的, 刪除它！',
        cancelButtonText: '取消'
    })

    if (!result.isConfirmed) {
        return
    }

    // 進行刪除程序
    const fullUrl = 'http://localhost:8080/SpringMVC_war_exploded/mvc/rest/user/' + id;
    const response = await fetch(fullUrl, { method: 'DELETE'});  // 等待 fetch 請求完成
    const { state, message, data } = await response.json();     // 等待回應本文內容
    console.log(state, message, data);
    Swal.fire({
        title: "Deleted!",
        text: "刪除成功！",
        icon: "success",
        color: '#fff'
    })
    // 更新 user list
    fetchAndRenderData('/mvc/rest/user', 'user-list-body', renderUser);
};

const handleEvent = async(event, className, callback) => {
    if (!event.target.classList.contains(className)) {
        return;
    }
    const id = event.target.getAttribute('data-id');
    callback(id);
}

// 加載表單選項(學歷、興趣)
const loadFormOptions = async() => {
    // 加載學歷選項
    const educationOptions = await fetch('http://localhost:8080/SpringMVC_war_exploded/mvc/rest/user/educations');
    var { state, message, data } = await educationOptions.json();
    // console.log(data);
    data.forEach(education => {
        const opt = document.createElement('option');
        opt.value = education.id;
        opt.textContent = education.name;
        $('educationId').appendChild(opt);
    })

    // 加載興趣選項
    const interestOptions = await fetch('http://localhost:8080/SpringMVC_war_exploded/mvc/rest/user/interests');
    var { state, message, data } = await interestOptions.json();
    console.log(data);
    data.forEach(interest => {
        const opt = document.createElement('option');
        opt.value = interest.id;
        opt.textContent = interest.name;
        $('interestIds').appendChild(opt);
    })

};

// 表單提交事件處理
const handleFormSubmit = async(event) => {
    event.preventDefault();  // 停止表單的預設傳送行為

    // 改成自訂行為，邏輯如下
    // 表單資料
    const formData = {
        name: $('name').value,
        age: parseInt($('age').value),
        birth: $('birth').value,
        educationId: parseInt($('educationId').value),
        genderId: parseInt(document.querySelector('input[name="genderId"]:checked').value),
        interestIds: Array.from($('interestIds').selectedOptions).map(option => parseInt(option.value)),
        resume: $('resume').value
    };

    const response = await fetch('http://localhost:8080/SpringMVC_war_exploded/mvc/rest/user', {
        method: 'POST',
        headers: {  // 一定要加
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)  // 表單資料轉 json 字串
    });

    const { state, message, data } = await response.json();
    console.log(message);
    fetchAndRenderData('/mvc/rest/user', 'user-list-body', renderUser);
}

// 待 DOM 加載完成之後再執行
document.addEventListener("DOMContentLoaded", async() => {
    // 加上 await 關鍵字等待 loadHTML 函數完成才會進行下一個程序
    await loadHTML('/user/user-form.html', 'user-form-container');
    await loadHTML('/user/user-list.html', 'user-list-container');

    // 資料渲染(fetch)
    fetchAndRenderData('/mvc/rest/user', 'user-list-body', renderUser);

    // 監聽 User List 是否有被點擊
    $('user-list-table').addEventListener("click", async(event) => {
        // console.log(event)
        // 處理事件
        await handleEvent(event, 'update-user-button', handleUpdateUser)
        await handleEvent(event, 'delete-user-button', handleDeleteUser)
    })

    // 加載表單選項(學歷、興趣)
    loadFormOptions();

    // 監聽 user-form
    $('user-form').addEventListener('submit', handleFormSubmit)
});