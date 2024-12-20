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
    <link href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous" rel="stylesheet" />
    <style>
        .select2-container--default .select2-selection--single .select2-selection__arrow {
            height: 36px;
        }

        .select2-container--default .select2-selection--single {
            height: 38px;
        }

        .select2-container--default .select2-selection--single .select2-selection__rendered {
            line-height: 36px;
            font-size: 14px;
        }

        .pagination .select2-container--default .select2-selection--single .select2-selection__arrow {
            height: 20px;
        }

        .pagination .select2-container--default .select2-selection--single {
            height: 22px;
        }

        .pagination .select2-container--default .select2-selection--single .select2-selection__rendered {
            line-height: 20px;
        }
        .btn-primary {
            margin: 10px 10px 10px 0;
        }

        .pagination .fas {
            font-size: 24px;
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

        const addPagination = function (paginationInfo, target) {
            let nav = $("<nav aria-label='Page navigation example'></nav>").appendTo(target);
            let container = $("<ul class='pagination justify-content-center'></ul>").appendTo(nav);

            const isFirstPage = paginationInfo.curPageNumber === "1";

            const searchParams = new URLSearchParams(paginationInfo.lastPageRoute);
            let lastPageNumber = 0;
            if (searchParams.has("param"))
                searchParams
                    .get("param")
                    .split(";")
                    .forEach(
                        (el) => {
                            const arr = el.split(":")
                            if (arr[0] === "pn")
                                lastPageNumber = arr[1];
                        }
                    );
            const isLastPage = lastPageNumber === paginationInfo.curPageNumber;

            container
                .append("<li class='page-item" + (isFirstPage ? " disabled" : "") + "'>" +
                            "<a class='page-link' onclick='" + (isFirstPage
                                ? "function() { return false; }"
                                : "getContent(\"" + paginationInfo.firstPageRoute) + "\")'>" +
                                "<i class='fas fa-angle-double-left'></i>" +
                            "</a>" +
                        "</li>")
                .append("<li class='page-item" + (isFirstPage ? " disabled" : "") + "'>" +
                            "<a class='page-link' onclick='" + (isFirstPage
                                ? "function() { return false; }"
                                : "getContent(\"" + paginationInfo.prevPageRoute) + "\")'>" +
                            "<i class='fas fa-angle-left'></i>" +
                            "</a>" +
                        "</li>")
                .append("<li class='page-item active'>" +
                            "<span class='page-link'>" +
                                "Page " + paginationInfo.curPageNumber + " of " + lastPageNumber +
                            "</span>" +
                        "</li>")
                .append("<li class='page-item'>" +
                            "<span class='page-link' style='display:flex;align-items:center;'>" +
                                "<span style='padding-right:10px;'>Rows: </span><span>" + getSelectSizeHTML(paginationInfo.pageSize, paginationInfo.firstPageRoute) + "</span>" +
                            "</span>" +
                        "</li>")
                .append("<li class='page-item" + (isLastPage ? " disabled" : "") + "'>" +
                            "<a class='page-link' onclick='" + (isLastPage
                                ? "function() { return false; }"
                                : "getContent(\"" + paginationInfo.nextPageRoute) + "\")'>" +
                            "<i class='fas fa-angle-right'></i>" +
                            "</a>" +
                        "</li>")
                .append("<li class='page-item" + (isLastPage ? " disabled" : "") + "'>" +
                            "<a class='page-link' onclick='" + (isLastPage
                                ? "function() { return false; }"
                                : "getContent(\"" + paginationInfo.lastPageRoute) + "\")'>" +
                            "<i class='fas fa-angle-double-right'></i>" +
                            "</a>" +
                        "</li>");
        };

        const getSelectSizeHTML = function (curSize, firstPageRoute) {
               const availableSizes = [5, 10, 15, 20, 50, 100];
               let optionsText = "";
               for (let i = 0; i < availableSizes.length; i++) {
                   let el = availableSizes[i];
                   optionsText += "<option value='" + el + "'" + (+curSize === el ? "selected" : "") + ">" + el + "</option>";
               }
             return "<select style='width:60px;' onchange='changeRows(this, \"" + firstPageRoute + "\")'>"  + optionsText + "</select>"
        }

        const changeRows = function (elem, route)  {
            getContent(route.replace(new RegExp("ps:[0-9]+","gm"), "ps:" + elem.value));
        }

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
                if (contentObj.paginationInfo) {
                    addPagination(contentObj.paginationInfo, content);
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
