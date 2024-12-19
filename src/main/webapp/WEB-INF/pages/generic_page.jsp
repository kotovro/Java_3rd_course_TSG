<%--
  Created by IntelliJ IDEA.
  User: kotvo
  Date: 08.12.2024
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Maintenance Company</title>
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
    <style>
        .select2-container--default .select2-selection--single .select2-selection__arrow {
            height: 36px;
        }

        .select2-container--default .select2-selection--single {
            height: 38px;
        }

        .select2-container--default .select2-selection--single .select2-selection__rendered {
            line-height: 36px;
        }

        .btn-primary {
            margin: 10px 10px 10px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Maintenance Company</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <% if (request.getAttribute("menuContent") != null) { %>
                    ${menuContent}
                    <% } %>
                </ul>
            </div>
        </div>
    </nav>

    <form id='formContent' method="post">
        <div class="alert alert-danger text-center" id="errorMessage" style="display:none;"></div>

        <div id="content"></div>
    </form>
    <script type="text/javascript">
        const addMultiSelects = function (list, target)  {
            const selectContainer = $("<div class='row g-3'></div>").appendTo(target);
            for (let i = 0; i < list.length; i++) {
                let optionsList = '';
                for (let  j = 0; j < list[i].options.length; j++) {
                    optionsList += "<option selected='selected' value='" + list[i].options[j].id + "' >" +
                        list[i].options[j].text + "</option>";
                }
                selectContainer.append(
                    "<div class=col-md-3>" +
                    "<label class='form-label' for='" + list[i].id + "'>" + list[i].label + "</label>" +
                    "<select class='form-control' id='" + list[i].id + "' data-close-on-select='false' data-ajax--cache='true' data-ajax--url='" +
                    list[i].optionsURL + "' multiple>"  + optionsList +
                    "</select>" +
                    "</div>"
                );
            }
        };
        const addSelects = function (list, target)  {
            const selectContainer = $("<div class='row g-3'></div>").appendTo(target);
            for (let i = 0; i < list.length; i++) {
                selectContainer.append(
                    "<div class=col-md-3>" +
                        "<label class='form-label' for='" + list[i].id + "'>" + list[i].label + "</label>" +
                        "<select class='form-control' id='" + list[i].id + "' data-ajax--cache='true' data-ajax--url='" +
                            list[i].optionsURL + "'>"  +
                            "<option selected='selected' value='" + list[i].value + "'>" + list[i].valueName + "</option>" +
                        "</select>" +
                    "</div>"
                );
            }
        };

        const addInputs = function (list, target) {
            const inputContainer = $("<div class='row g-3'></div>").appendTo(target);
            for (let i = 0; i < list.length; i++) {
                inputContainer.append(
                    "<div class=col-md-4>" +
                        "<label class='form-label' for='" + list[i].id + "'>" + list[i].label + "</label>" +
                        "<input type='text' class='form-control' value='" + list[i].value + "' id='" + list[i].id + "'" +
                            (!list[i].isEnabled ? " disabled='true'" : "") + "/>" +
                    "</div>"
                );
            }

        };

        const addButtons = function (list, target) {
            const buttonContainer = $("<div style='padding-top: 10px;padding-bottom: 10px'></div>").appendTo(target);
            for (let i = 0; i < list.length; i++) {
                buttonContainer.append("<button type='button' class='btn btn-primary btn-sm' onclick=" +
                    (list[i].buttonType == "GET" ? "'getContent(\"" : "'updateContent(\"") + list[i].url + "\");'>" +
                    list[i].text + "</button>");
            }
        };

        const addListItems = function (list, target) {
            const listContainer = $("<div class='list-group'></div>").appendTo(target);
            for (let i = 0; i < list.length; i++) {
                listContainer.append("<a class='list-group-item list-group-item-action' href='#' onclick='getContent(\"" +
                    list[i].url + "\");'>" + list[i].text + "</a>");
            }
        };
        const createContent = function (contentObj) {
            const contentDiv = $("#content");
            contentDiv.empty();
            if (contentObj) {
                contentDiv.append("<div style='padding-top: 10px;padding-bottom: 30px'></div>");
                const content = $("<div style='padding-top: 10px;padding-bottom: 10px'></div>").appendTo(contentDiv);
                if (contentObj.listItems.length > 0) {
                    addListItems(contentObj.listItems, content);
                }
                if (contentObj.inputs.length > 0) {
                    addInputs(contentObj.inputs, content);
                }
                if (contentObj.selects.length > 0) {
                    addSelects(contentObj.selects, content);
                }
                if (contentObj.multipleSelects.length > 0) {
                    addMultiSelects(contentObj.multipleSelects, content);
                }
                if (contentObj.buttons.length > 0) {
                    addButtons(contentObj.buttons, content);
                }
            }
        };
        const getContent = function (route) {
            $("#errorMessage").empty().css('display', 'none');

            $.ajax({
                url: route,
                async: false,
                cache: false,
                success: (data) => {
                    if (data) {
                        createContent(data);
                        $("select").select2();
                    }
                },
            });
        };
        const updateContent = function (route) {
            let postData = {};
            $("#errorMessage").empty().css('display', 'none');
            $("#content input, #content select").each(function (idx, el) {
                postData[$(el).prop("id")] = typeof $(el).val() === 'object' ? $(el).val().join(',') : $(el).val();
            });


            $.ajax({
                url: route,
                async: false,
                data: postData,
                cache: false,
                success: () => {
                    alert("Update successful");
                },
                error: (data) => {
                    if (data && data !== "success") {
                        $("#errorMessage")
                            .append(data)
                            .css('display', '');
                    }
                },
            });
        };
    </script>
</div>

<!-- Include Bootstrap JS and Dependencies -->
<script type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
</body>
</html>
