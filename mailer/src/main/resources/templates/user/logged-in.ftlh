<#import "../layout.ftlh" as layout>

<@layout.text>
    <h2>${title}</h2>
    <h3>${userName}, Вы успешно вошли в систему.</h3>
    <h4><strong>IP-адрес: </strong>${userIp}</h4>
</@layout.text>