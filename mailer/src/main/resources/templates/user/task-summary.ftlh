<#import "../layout.ftlh" as layout>

<@layout.text>
    <h2>${title}</h2>
    <h3>${userName}, здесь Ваша сводка по задачам.</h3>
    <#if completed ? has_content>
        <h3>Выполненные задачи за сегодня:</h3>
        <div class="product-wrapper">
            <table class="bg_white" role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                <#list completed as task>
                    <#if ((task ? counter) > 3)>
                        <tr>
                            <td>
                                <div class="product-entry">
                                    <p><strong>И еще ${(completed ? size) - 3}...</strong></p>
                                </div>
                            </td>
                        </tr>
                        <#break>
                    </#if>
                    <tr>
                        <td>
                            <h4>${task.name}</h4>
                            <p>${task.notes}</p>
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
    <#if uncompleted ? has_content>
        <h3<#if completed ? has_content> style="margin-top: 30px;"</#if>>Невыполненные задачи:</h3>
        <div class="product-wrapper">
            <table class="bg_white" role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                <#list uncompleted as task>
                    <#if ((task ? counter) > 3)>
                        <tr>
                            <td>
                                <div class="product-entry">
                                    <p><strong>И еще ${(uncompleted ? size) - 3}...</strong></p>
                                </div>
                            </td>
                        </tr>
                        <#break>
                    </#if>
                    <tr>
                        <td>
                            <h4>${task.name}</h4>
                            <p>${task.notes}</p>
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
</@layout.text>