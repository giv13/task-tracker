<#import "../layout.ftlh" as layout>

<@layout.text>
    <h2>${title}</h2>
    <h3>${userName}, здесь Ваша сводка по задачам.</h3>
    <h4><strong>Выполненных задач за сегодня: </strong>${completed}</h4>
    <h4><strong>Осталось невыполненных задач: </strong>${uncompleted}</h4>
</@layout.text>