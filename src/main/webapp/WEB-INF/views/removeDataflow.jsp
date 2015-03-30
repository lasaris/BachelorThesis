<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>
    <div align="center">
        <h3>Enter dataflow name</h3>
        <form:form action="/removeDataflow" method="POST" modelAttribute="DataflowModel">
            <table style="border:2px solid black;">
                <tr>
                    <th><form:label path="dataflowName">Dataflow name:</form:label></th>
                    <th><form:input path="dataflowName" /></th>
                </tr>
                <tr>
                    <th align="center"><input type="submit" value="Remove" /></th>
                    <th align="center"><input type="reset" value="Reset" /></th>
                </tr>
            </table>
        </form:form>
        <form action="/">
            <input type="submit" value="Return home">
        </form>
    </div>
</body>
</html>