[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0050bd17f07a4f988344e36131af43fe)](https://app.codacy.com/gh/drovocek/restmanager?utm_source=github.com&utm_medium=referral&utm_content=drovocek/restmanager&utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/drovocek/restmanager.svg?branch=Cleaned)](https://travis-ci.org/github/drovocek/restmanager)

# My diplom project
____

## Completion date 
31 December 2020

## Theme 
REST API

## Technology stack
- Java 8
- Spring(Security, MVC, Data, REST doc), Hibernate
- HSQLDB
- slf4j, junit4, Assertj, Hamcrest
- Jackson, Lombok
- Maven

## Documentation
[html (in progress)](https://htmlpreview.github.io/?https://github.com/drovocek/restmanager/blob/main/src/main/docs.asciidoc/final_doc.html)

[pdf (in progress)](https://github.com/drovocek/restmanager/blob/Cleaned/src/main/docs.asciidoc/final_doc.pdf)

## Task
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.

P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.
