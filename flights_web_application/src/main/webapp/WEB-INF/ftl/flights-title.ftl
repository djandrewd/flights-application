<html>
<head>
    <meta charset="UTF-8">
    <title>Search for flights</title>
    <link rel="stylesheet" href="styles.css" type="text/css">
</head>
<body>
<div id="search">
    <form id="search-form" method="post" action="search">
        <label for="departure">Departure airport</label>
        <input type="text" name="departure" id="departure"/>

        <label for="arrival">Arrival airport</label>
        <input type="text" name="arrival" id="arrival"/>

        <label for="date">Date</label>
        <input type="date" name="date" id="date"/>

        <label for="stops">Stops</label>
        <input type="number" name="stops" min="0" max="4"/>

        <input type="submit" name="submit" value="Search" id="search-btn"/>
    </form>
</div>
<div id="error">
<#if error??>
    <label id="error-msg">Error occurred during request processing : ${error}</label>
</#if>
</div>
<div id="results">
<#if flights??>
    <#list flights as entry>
        <table class="flight">
            <tr class="flight-route">
                <td><b>Departure</b></td>
                <td>${entry.departure.name}</td>

                <td><b>Arrival</b></td>
                <td>${entry.arrival.name}</td>

                <td><b>Departure time</b></td>
                <td>${entry.departureTime}</td>

                <td><b>Arrival time</b></td>
                <td>${entry.arrivalTime}</td>
            </tr>
            <tr class="flight-value">
                <td>Containing</td>
            </tr>
            <#list entry.flights as connect>
                <tr>
                    <td>
                        <table class="connect-flight">
                            <tr class="flight-part">
                                <td><b>${connect.name}</b></td>
                            </tr>
                            <tr class="flight-part">
                                <td><b>Departure</b></td>
                                <td>${connect.departure.name} (${connect.departure.city},
                                ${connect.departure.country})
                                </td>
                            </tr>
                            <tr class="flight-part">
                                <td><b>Arrival</b></td>
                                <td>${connect.arrival.name} (${connect.arrival.city},
                                ${connect.arrival.country})
                                </td>
                            </tr>
                            <tr class="flight-part">
                                <td><b>Departure time</b></td>
                                <td>${connect.departureTime}</td>
                            </tr>
                            <tr class="flight-part">
                                <td><b>Arrival time</b></td>
                                <td>${connect.arrivalTime}</td>
                            </tr>
                            <tr class="flight-part">
                                <td><b>Operated by</b></td>
                                <td>${connect.airline.name}</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </#list>
        </table>
    </#list>
</#if>
</div>
</body>
</html>
