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
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
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
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
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

    <form id='formContent' method="post" >
        <div class="alert alert-danger text-center" id="errorMessage" style="display:none;"></div>

        <div id="content"></div>
    </form>
    <script type="text/javascript">
        const getContent = function(route) {
            $("#errorMessage").empty().css('display', 'none');
            const contentDiv = $("#content");
            contentDiv.empty();

            $.ajax({
                url: route,
                async: false,
                cache: false,
                success: (data) => {
                    if (data) {
                        contentDiv.append(data);
                        $("select").select2();
                    }
                },
            });
        }
        const updateContent = function(route) {
            let postData = {};
            $("#errorMessage").empty().css('display', 'none');
            $("#content input, #content select").each(function (idx, el) {
                postData[$(el).prop("id")] = $(el).val();
                //console.log($(el).prop("id") + " : "+ $(el).val());
            });


            $.ajax({
                url: route,
                async: false,
                data: postData,
                // type: "POST",
                // processData: false,
                // contentType: false,
                cache: false,
                error: (data) => {
                    if (data && data !== "success") {
                        $("#errorMessage")
                            .append(data)
                            .css('display', '');
                    }
                },
            });

        }
    </script>
</div>

<!-- Include Bootstrap JS and Dependencies -->
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
</body>
</html>
