<h1 align="center">Wallet Service</h1>

> Spring Boot API that stores and distributes Thor wallet addresses, given a Terra wallet address

### Functional Components

## Running Locally

This project requires a database to function.  This section contains information on how to set that up.

### Installing Docker 

Docker provides an environment to run the database, that will work regardless of your operating system.
You can go here to [install docker](https://docs.docker.com/get-docker/ "Get Docker")

Once you have Docker installed, run `docker --help` in the terminal to confirm it was installed properly.

### Getting the official PostgreSQL Docker image  

Get the official docker image for Postgres.  Information about this image can be found [here](https://hub.docker.com/_/postgres "Docker PostgreSQL image").

All you need to do is open up the terinal and run:

`$ docker run --name demo-postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:alpine`

### Creating the Database 

Run the image in interactive mode: <br />
`docker exec -it demo-postgres /bin/bash`

Log into PostgreSQL using the username 'postgres' <br />
`psql -U postgres`

Create the database: <br />
`CREATE DATABASE league_database;`

Clone that project and set up your IntelliJ environment variables:

#### Adding environment variables in IntelliJ
- Select “Edit Configurations” from drop down next to play button at the top of the IDE window 
In the “Environment” section
- paste the environment member variable String into the “Environment Variables” text field.

#### Environment Variables:
- JWT_SECRET_KEY

#### Setting spring profile in IntelliJ
- Open the “Edit Configuration” menu.
- In the Environment > VM Options text field, set the active profile to 'development':  
  
-Dspring.profiles.active=development


### Good to Go!
Running the League Authentication service created all the database tables and ENUMs, and add some initial table content.
All that is left is to set up the Intellij environment for the League Survey Service as you did for the League Authentication Service above!


## Using the PostgreSQL Command Line Interface 
If you need to view the information currently in the database, here is how you can do that:

Run the image in interactive mode: <br />
`docker exec -it demo-postgres /bin/bash`

Log into PostgreSQL using the username 'postgres' <br />
`psql -U postgres`

Connect to the league_database <br />
`\c league_database`

### Here are some helpful commands:

>`\d` <br />
list the relations in the database 

>`\d student` <br />
describes the student table

You can also run queries such as:

>`SELECT * FROM student;` <br />
lists all of the rows in the student table

>`SELECT * FROM survey_order WHERE survey_order_status = 'ACTIVE'::survey_order_status;` <br />
Gets all active survey orders 

The postgreSQL CLI documentation can be found [here](https://www.postgresql.org/docs/9.4/app-psql.html "postgreSQL CLI documentation")
 
## Documentation

## Implementation Notes

## Scheduled Events

## Author

👤 **Matt Freedman**
- Email: [matt.freedman@jointheleague.org](mailto:matt.freedman@jointheleague.org)
- Github: [@mjfre](https://github.com/mjfre)
