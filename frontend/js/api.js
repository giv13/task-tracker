const user = JSON.parse(localStorage.getItem("user"));

const apiBaseUrl = "/api";

const api = {
    register: () => `${apiBaseUrl}/auth/register`,
    login: () => `${apiBaseUrl}/auth/login`,
    logout: () => `${apiBaseUrl}/auth/logout`,
    refresh: () => `${apiBaseUrl}/auth/refresh`,
    tasks: () => `${apiBaseUrl}/tasks`,
};

const toast = (title, body, cls) => {
    $(document).Toasts("create", {
        title: title,
        autohide: true,
        delay: 3000,
        body: body,
        class: cls,
    });
};

const toastError = (message) => {
    toast("Ошибка", message, "bg-danger");
};

const get = (url) => {
    return request(url, "GET");
};

const post = (url, form) => {
    return request(url, "POST", form);
};

let isRefreshRequesting = false;
let requestsToRefresh = [];
const request = async (url, method, form) => {
    const data = Object.fromEntries(new FormData(form));
    const options = {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
    };
    if (Object.keys(data).length > 0) {
        options.body = JSON.stringify(data);
    }
    return await fetch(url, options)
        .then((r) => r.json())
        .then((r) => {
            if (!r.success) throw r;
            return r.data;
        })
        .catch((r) => {
            $(form)
                .find(".is-invalid")
                .removeClass("is-invalid")
                .next(".error")
                .remove();
            const error = r.error;
            if (r.status === 401 && user !== null) {
                if (!isRefreshRequesting) {
                    isRefreshRequesting = true;
                    fetch(api.refresh(), {
                        method: "POST",
                        credentials: "include",
                    })
                        .then((r) => r.json())
                        .then((r) => {
                            if (r.status === 200) {
                                requestsToRefresh.forEach((r) => r());
                            } else {
                                localStorage.removeItem("user");
                                window.location.href = "login.html";
                            }
                        })
                        .finally(() => {
                            requestsToRefresh = [];
                            isRefreshRequesting = false;
                        });
                }
                return new Promise((resolve, reject) => {
                    requestsToRefresh.push(() => {
                        resolve(request(url, method, form));
                        reject(error);
                    });
                });
            } else if (typeof error === "string") {
                toastError(error);
            } else if (typeof error === "object") {
                for (const [field, message] of Object.entries(error)) {
                    $(form)
                        .find('[name="' + field + '"]')
                        .addClass("is-invalid")
                        .after(
                            '<span class="error invalid-feedback">' +
                            message.join("<br>") +
                            "</span>",
                        );
                }
            }
            throw r;
        });
};