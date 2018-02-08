# Flights-x. Application for searching the flights.
  
Your task is to implement web application and external API for searching flights (as Google 
Flights).
  
##  Data
You are provided with following modules to help you build up your applications:

1. _data-services_ - module used for working with data storage using DAO pattern.
2. _search-engine_ - module used as engine for searching routes and flights.

## Tasks

Implementation should consist of web application and external API service used 
for data selection. Consider next splitting for tasks.

### 1. Database scheme and ER model.

Provide database scheme and ER model for Flights application. 
You must cover next use cases for common searching of the flights:

1. Each flight must departure and arrive to some airport.
2. Each airline and airport have international flight code known as IATA.
3. Each flight must by operated by some airline (you can omit shared flights).
4. Each flight must flight on some plane, which define number of seats and classes.
5. Consider only regular flights. Each of regular flights operated with same name, code, 
   plane, time and very on the date of derarture.
6. Flight can departure and arrive from airports with different time zones.
 
### 2. Data services. Database integration.

Provide implementation of _data-services_ for selection of data from data storage.
Implementation should be based on Spring and Hibernate framework. 

You are provided with following data objects:

1. _Airport_ - holds information about airport abstraction entity.
2. _Airline_ - holds information about airline abstraction entity.
3. _FlightRoute_ - holds information about scheduled flight, times of arrival and departure, 
airports and operated airline.
4. _Flight_ - holds information about flight as FlightRoute on some defined date.
5. _Plane_ -  holds information about Plane abstraction entity.
6. _SeatClass_ -  holds information about plane seat class abstraction entity.

Services you have to implement:

1. _AirlineService_ - service which helps access airline data store.
2. _AirportService_ - service which helps access airport data store.
3. _FlightsService_ - service which helps access flights data store.
4. _RoutesService_ - service which helps access flight routes data store.
 
**Note! You cannot remove any methods from provided interfaces, but you can extend it any time.** 

### 3. (Optional) Data services. OpenFlights. 

Optionally provide implementation of data-services module which integrates
with OpenFlights database (https://openflights.org/data.html) 

You may use this data further in tests for search engines.

### 4. Search engine. 

Your task here is to provide implementation for _search-engine_ module. 
You are supplied with following interfaces:
 
1. _RoutesEngine_ - engine for searching of flight routes without any date between
  two defined airports and maximum number of stops. Several limitations in implementaion here are:
  same airport must not be passed twice, next plane should not departure before current open arrive.
  Optionally you can define minimum time for flights change.

2. FlightsEngine - engine for searching of flights on concrete day. It must use RoutesEngine for 
  routes selection and does not select any flights when there are not free seats left.
   
(Optionally in flight engine you can set class you want to search.)
   
### 5. External API. 

Your task here is to provide implementation for _external-api-services_ module.
You may use Spring MVC or Spring Boot in your implementation.

You are supplied by following classes:

1. _Airline_ - data object for airline entity.
2. _Airport_ - data object for airpot entity.
3. _FlightRoute_ - holds information about some flight route between airport on some date.

#### Search airport by city (/api/airports/byCity)

Resource for search information about airports by city name.
Implementation should search city by contains (like '%city%')

METHOD: _GET_

REQUEST PARAMETERS: _city_ (String, REQUIRED) - name of city.
   
Example:

GET http://localhost:8080/api/airports/byCity?city=Kiev HTTP/1.1

```json
[
      {
      "iata": "KBP",
      "name": "Boryspil International Airport",
      "city": "Kiev",
      "country": "Ukraine",
      "timeZone": "Europe/Kiev"
   },
      {
      "iata": "IEV",
      "name": "Kiev Zhuliany International Airport",
      "city": "Kiev",
      "country": "Ukraine",
      "timeZone": "Europe/Kiev"
   },
      {
      "iata": "GML",
      "name": "Gostomel Airport",
      "city": "Kiev",
      "country": "Ukraine",
      "timeZone": "Europe/Kiev"
   }
]  
```       

#### Search airport by IATA (/api/airports/byIata)

Resource for searching of the airports using internation flight identity (IATA).

METHOD: _GET_

REQUEST PARAMETERS: _iata_ (String, REQUIRED) - IATA code of airport.

Exampl**e: 
   GET http://localhost:8080/api/airports/byIata?iata=KBP HTTP/1.1
```json
[{
   "iata": "KBP",
   "name": "Boryspil International Airport",
   "city": "Kiev",
   "country": "Ukraine",
   "timeZone": "Europe/Kiev"
}]
```

#### Search for the flights (/api/flights/search)

Resource for searching the flights between airpots with defined number of stops and some date.

METHOD: _GET_

REQUEST PARAMETERS:

  1. _date_ (String, ISO formatted, REQUIRED) - date of the flight departure.
  2. _departure_ (String, REQUIRED) - IATA code of departure airport. 
  3. _arrival_ (String, REQUIRED) - IATA code of arrival airport.
  4. _stops_ (integer, REQUIRED) - max number of stops. 
  5. _page_ (integer, OPTIONAL, default - 0) - number of selection page, 0 based.
  6. _max_results_ (integer, OPTIONAL, default - 50) - number of results per selection page.
  
Example:
  GET http://localhost:8080/api/flights/search?date=2017-09-28&departure=KBP&arrival=AMS&stops=1 HTTP/1.1

```json
[
      {
      "price": 0,
      "flights": [      {
         "name": "KL1382",
         "departure":          {
            "iata": "KBP",
            "name": "Boryspil International Airport",
            "city": "Kiev",
            "country": "Ukraine",
            "timeZone": "Europe/Kiev"
         },
         "arrival":          {
            "iata": "AMS",
            "name": "Amsterdam Airport Schiphol",
            "city": "Amsterdam",
            "country": "Netherlands",
            "timeZone": "Europe/Amsterdam"
         },
         "airline":          {
            "name": "KLM Royal Dutch Airlines",
            "iata": "KL",
            "country": "Netherlands"
         },
         "departureTime": "2017-09-28T05:45:00+03:00[Europe/Kiev]",
         "arrivalTime": "2017-09-28T07:35:00+02:00[Europe/Amsterdam]"
      }]
   },
      {
      "price": 0,
      "flights": [      {
         "name": "KL3098",
         "departure":          {
            "iata": "KBP",
            "name": "Boryspil International Airport",
            "city": "Kiev",
            "country": "Ukraine",
            "timeZone": "Europe/Kiev"
         },
         "arrival":          {
            "iata": "AMS",
            "name": "Amsterdam Airport Schiphol",
            "city": "Amsterdam",
            "country": "Netherlands",
            "timeZone": "Europe/Amsterdam"
         },
         "airline":          {
            "name": "Ukraine International Airlines",
            "iata": "PS",
            "country": "Ukraine"
         },
         "departureTime": "2017-09-28T19:40:00+03:00[Europe/Kiev]",
         "arrivalTime": "2017-09-28T21:45:00+02:00[Europe/Amsterdam]"
      }]
   }
]
```
  
### 6. Web application.

Provide web application for searching flights between two airports. 
You must use Spring framework and any king of frond-end technology. (For example FreeMarker).

Follow common style of https://www.google.com.ua/flights/.

## Additional requirements.

Implementation should follow code conventions and Google checkstyle rules, satisfy all provided
tests.
For external API and WEB application you should enable logging for request and events happens 
during selection.

Good luck!
