<#import "../layout.ftlh" as layout>

<@layout.text>
    <h2>${title}</h2>
    <h3>${userName}, Вы изменили пароль для доступа в систему.</h3>
    <h4><strong>Ваш логин: </strong>${userEmail}</h4>
    <h4><strong>Новый пароль: </strong>${userPassword}</h4>
</@layout.text>