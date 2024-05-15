
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
        <span class="pure-button button-update update-user-button data-id="${id}">Edit</span>
    </td>
    <td>
        <span class="pure-button button-delete delete-user-button data-id="${id}">Delete</span>
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

const handleUpdateUser = () => {
    console.log('按下修改')
};
const handleDeleteUser = () => {
    console.log('按下刪除')
};

const handleEvent = async(event, className, callback) => {
    if (!event.target.classList.contains(className)) {
        return;
    }
    callback();
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
        console.log(event)
        // 處理事件
        await handleEvent(event, 'update-user-button', handleUpdateUser)
        await handleEvent(event, 'delete-user-button', handleDeleteUser)
    })
});