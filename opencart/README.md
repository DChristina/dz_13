# Run in development mode

### Go to this directory
`cd ./opencart/`

### Run docker compose

`docker-compose up -d`

### :exclamation: Export database (Do this once after fresh run)

1. Login to phphmyadmin: http://localhost:8000 with credentials
   - Username: `opencart`
   - Password: `opencart`
1. Select db `opencart-db`
1. Go to tab **Import**
1. Select file to import as `database/opencart-db.sql` relatively to this directory
1. Press **Import** button and wait until operation will be finished

### Access website

Website: http://localhost:8001

Admin: http://localhost:8001/adm/

### Shutdown containers

`docker-compose down`