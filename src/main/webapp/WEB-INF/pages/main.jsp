<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<html>
<head>
  <TITLE>Backofficer</TITLE>

  <script type="text/javascript" src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
  <script type="text/javascript" src="https://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>

  <link href="<c:url value="/resources/css/pivot.css" />" rel="stylesheet">
  <script src="<c:url value="/resources/js/pivot.js" />"></script>

  <script type="text/javascript">
    function refreshTable() {
      $.ajax({
        url : 'updatelist',
        type: "GET",
        dataType: "json",
        timeout:60000,

        success: function(data){
          $("#output").pivotUI(data);
          setTimeout(
                  refreshTable,
                  1000
          );
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
          setTimeout(
                  refreshTable,
                  1000);
        }
      });
    }
    $(document).ready(function(){

      $.getJSON("getlist", function(data) {
        $("#output").pivotUI(data);
      });

      refreshTable(); /* Start the inital request */
    });
  </script>
</head>

<body>
<table class="table table-striped table-bordered table-condensed">
  <div id="output" style="margin: 30px;"></div>
</table>
</body>
</html>
